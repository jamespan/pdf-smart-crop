package me.jamespan.tech.smartcrop.core;


import me.jamespan.tech.smartcrop.domain.WindowMatrix;

import java.awt.image.BufferedImage;

/**
 *
 * @author panjiabang
 * @since  1/3/16
 */
public interface WindowProcessor {

    WindowMatrix imageToWindows(BufferedImage input, int windowWidthInPixel, int windowHeightInPixel);

    WindowMatrix markBodyArea(WindowMatrix winMatrix, final double ratioThreshold);

    WindowMatrix markBodyArea(WindowMatrix winMatrix);
}
