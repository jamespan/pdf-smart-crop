package me.jamespan.tech.smartcrop.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.jamespan.tech.smartcrop.domain.Box;
import me.jamespan.tech.smartcrop.domain.Result;
import me.jamespan.tech.smartcrop.domain.WindowMatrix;

import java.awt.image.BufferedImage;

/**
 *
 * @author panjiabang
 * @since  12/28/15
 */
@Singleton
public class FixedSlideWindowDetector implements BodyDetector {

    private Double densityThreshold = null;
    private boolean autoThreshold = true;

    FixedWidthWindowProcessor windowProcessor;
    BodyAreaExtractor extractor;

    @Inject
    public FixedSlideWindowDetector(BodyAreaExtractor extractor, FixedWidthWindowProcessor windowProcessor) {
        this.extractor = extractor;
        this.windowProcessor = windowProcessor;
    }

    @Override
    public void setDensityThreshold(double threshold) {
        this.densityThreshold = threshold;
        this.autoThreshold = true;
    }

    @Override
    public Result detect(BufferedImage pageImage) {

        WindowMatrix windowMatrix = windowProcessor.imageToWindows(pageImage, 25, 25);
        if (autoThreshold) {
            windowMatrix = windowProcessor.markBodyArea(windowMatrix);
        } else {
            windowMatrix = windowProcessor.markBodyArea(windowMatrix, densityThreshold);
        }

        windowProcessor.spreadBodyArea(windowMatrix, 0);

        Box body = extractor.extract(windowMatrix);
        return body == null ? Result.notFound() : Result.with(body);

    }
}
