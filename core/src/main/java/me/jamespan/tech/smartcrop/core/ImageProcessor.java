package me.jamespan.tech.smartcrop.core;

import me.jamespan.tech.smartcrop.domain.IntMatrix;

import java.awt.image.BufferedImage;

/**
 * @author panjiabang
 * @since 1/3/16
 */
public interface ImageProcessor {

    BufferedImage toBinary(BufferedImage input);

    IntMatrix toMatrix(BufferedImage input);

}
