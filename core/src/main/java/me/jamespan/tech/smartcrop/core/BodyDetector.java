package me.jamespan.tech.smartcrop.core;

import me.jamespan.tech.smartcrop.domain.Result;

import java.awt.image.BufferedImage;

/**
 *
 * @author panjiabang
 * @since  1/3/16
 */
public interface BodyDetector {

    void setDensityThreshold(double threshold);

    Result detect(BufferedImage pageImage);

}
