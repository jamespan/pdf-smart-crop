package me.jamespan.tech.smartcrop.shell;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import me.jamespan.tech.smartcrop.core.BodyDetector;
import me.jamespan.tech.smartcrop.core.CropModule;
import me.jamespan.tech.smartcrop.domain.Box;
import me.jamespan.tech.smartcrop.domain.Result;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.shell.ShellException;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author panjiabang
 * @since  1/3/16
 */
@Component
public class CropCommand implements CommandMarker {

    private Injector injector = Guice.createInjector(new CropModule());

    private class CropTask implements Callable<Result> {

        BodyDetector detector;
        PDFRenderer renderer;
        int pageIndex = 0;

        public CropTask(BodyDetector detector, PDFRenderer renderer, int pageIndex) {
            this.detector = detector;
            this.renderer = renderer;
            this.pageIndex = pageIndex;
        }

        @Override
        public Result call() throws Exception {
            BufferedImage image = renderer.renderImage(pageIndex, 1f, ImageType.BINARY);
            return detector.detect(image);
        }
    }

    @CliCommand(value = "crop", help = "Smart crop the specified PDF file")
    public String crop(
            @CliOption(key = {"input"}, mandatory = true, help = "The path of PDF file to crop") final String inputPath
            , @CliOption(key = {"output"}, mandatory = false, help = "The path to save the cropped PDF file", unspecifiedDefaultValue = "out.pdf", specifiedDefaultValue = "out.pdf") final String outputPath
            , @CliOption(key = {"use-multi-core"}, mandatory = false, help = "Use multi core to speed up the crop process", unspecifiedDefaultValue = "false", specifiedDefaultValue = "true") boolean useMultiCore
            , @CliOption(key = {"body-pixel-density"}, mandatory = false, help = "Specify the minimal pixel density of body area", unspecifiedDefaultValue = "0.0") double densityThreshold
    ) {

        ExecutorService pool = Executors.newFixedThreadPool(useMultiCore ? Runtime.getRuntime().availableProcessors() - 1 : 1);

        PDDocument input = null;
        try {
            input = PDDocument.load(new File(inputPath));
            PDFRenderer renderer = new PDFRenderer(input);
            BodyDetector bodyDetector = injector.getInstance(BodyDetector.class);
            if (densityThreshold > 0) {
                bodyDetector.setDensityThreshold(densityThreshold);
            }

            int pageNum = input.getNumberOfPages();

            List<Future<Result>> futures = Lists.newArrayListWithCapacity(input.getNumberOfPages());

            for (int i = 0; i < pageNum; ++i) {
                String message = "\rpreparing crop tasks";
                System.out.write((message + Strings.repeat(".", i % 3) + Strings.repeat(" ", 3 - i % 3)).getBytes());
                futures.add(pool.submit(new CropTask(bodyDetector, renderer, i)));
            }
            System.out.println();

            boolean somethingWrong = false;
            for (int pageIndex = 0; pageIndex < pageNum; ++pageIndex) {
                String message = "\rworking";
                System.out.write(String.format("%s %3d%%", message, (pageIndex + 1) * 100 / pageNum).getBytes());
                Future<Result> future = futures.get(pageIndex);
                try {
                    Result result = future.get();
                    if (result.isBodyFound()) {
                        PDPage page = input.getPage(pageIndex);
                        Box bodyBox = result.getBody();
                        PDRectangle mb = page.getMediaBox();
                        float height = mb.getHeight();
                        mb.setLowerLeftX(bodyBox.getUpperLeft().getX());
                        mb.setLowerLeftY(height - bodyBox.getLowerRight().getY());
                        mb.setUpperRightX(bodyBox.getLowerRight().getX());
                        mb.setUpperRightY(height - bodyBox.getUpperLeft().getY());
                        page.setMediaBox(mb);
                    }
                } catch (Exception e) {
                    somethingWrong = true;
                    System.err.println(String.format("\r\nsomething went wrong when dealing page %d", pageIndex + 1));
                }
            }
            System.out.println();
            File output = new File(outputPath + (somethingWrong ? "problem.pdf" : ""));
            input.save(output);
            return "Done!";
        } catch (IOException e) {
            throw new ShellException(e);
        } finally {
            pool.shutdown();
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ignore) {}
            }
        }
    }
}
