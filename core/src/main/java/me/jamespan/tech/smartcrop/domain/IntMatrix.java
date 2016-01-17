package me.jamespan.tech.smartcrop.domain;


/**
 * 2D matrix that holds Integer elements
 * @author panjiabang
 * @since  12/29/15
 */
public class IntMatrix extends AbstractMatrix<Integer> {

    public IntMatrix(Integer[][] data, int width, int height) {
        this.data = data;
        this.width = width;
        this.height = height;
    }

    public IntMatrix copy() {
        Integer[][] m = new Integer[height][width];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                m[i][j] = data[i][j];
            }
        }
        return new IntMatrix(m, width, height);
    }

    public IntMatrix binarization(int threshold) {
        IntMatrix m = this.copy();
        for (int i = 0; i < m.height; ++i) {
            for (int j = 0; j < m.width; ++j) {
                m.data[i][j] = m.data[i][j] > threshold ? 255 : 0;
            }
        }
        return m;
    }

}
