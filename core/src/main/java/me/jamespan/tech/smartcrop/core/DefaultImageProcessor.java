package me.jamespan.tech.smartcrop.core;

import com.google.inject.Singleton;
import me.jamespan.tech.smartcrop.domain.IntMatrix;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author panjiabang
 * @since 1/3/16
 */
@Singleton
public class DefaultImageProcessor implements ImageProcessor {

    @Override
    public BufferedImage toBinary(BufferedImage input) {
        if (input.getType() == BufferedImage.TYPE_BYTE_BINARY) {
            return input;
        }

        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics g = output.getGraphics();
        g.drawImage(input, 0, 0, null);
        g.dispose();

        return output;
    }

    @Override
    public IntMatrix toMatrix(BufferedImage input) {

        int width = input.getWidth();
        int height = input.getHeight();
        Integer[][] data = new Integer[height][width];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                Color color = new Color(input.getRGB(j, i));
                data[i][j] = color.getRed();
            }
        }

        return new IntMatrix(data, width, height);
    }

}
