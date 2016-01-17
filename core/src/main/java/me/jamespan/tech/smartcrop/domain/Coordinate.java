package me.jamespan.tech.smartcrop.domain;

/**
 * 2D coordinate
 * @author panjiabang
 * @since  12/29/15
 */
public class Coordinate {

    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }
}
