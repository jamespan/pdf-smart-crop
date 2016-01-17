package me.jamespan.tech.smartcrop.domain;

/**
 * 2D coordinate for matrix, like m[i][j]
 * @author panjiabang
 * @since  1/3/16
 */
public class MatrixCoordinate extends Coordinate {
    public MatrixCoordinate(int i, int j) {
        super(j, i);
    }

    public int getI() {
        return getY();
    }

    public int getJ() {
        return getX();
    }

    @Override
    public String toString() {
        return "[" + getI() + ", " + getJ() + ']';
    }
}
