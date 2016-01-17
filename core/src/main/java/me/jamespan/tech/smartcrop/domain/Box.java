package me.jamespan.tech.smartcrop.domain;

/**
 * Describe a rectangle area with coordinate of upper-left and lower-right corner
 * @author panjiabang
 * @since  12/29/15
 */
public class Box {
    private Coordinate upperLeft;
    private Coordinate lowerRight;

    public Box(Coordinate upperLeft, Coordinate lowerRight) {
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
    }

    public Box(int upperLeftX, int upperLeftY, int lowerRightX, int lowerRightY) {
        this.upperLeft = new Coordinate(upperLeftX, upperLeftY);
        this.lowerRight = new Coordinate(lowerRightX, lowerRightY);
    }

    public int getWidth() {
        return lowerRight.getX() - upperLeft.getX();
    }

    public int getHeight() {
        return lowerRight.getY() - upperLeft.getY();
    }

    public Coordinate getUpperLeft() {
        return upperLeft;
    }

    public Coordinate getLowerRight() {
        return lowerRight;
    }
}
