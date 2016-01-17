package me.jamespan.tech.smartcrop.domain;

/**
 * 2D matrix that holds Window elements
 * @author panjiabang
 * @since  12/30/15
 */
public class WindowMatrix extends AbstractMatrix<Window> {

    public WindowMatrix(Window[][] data, int width, int height) {
        this.data = data;
        this.width = width;
        this.height = height;
    }
}
