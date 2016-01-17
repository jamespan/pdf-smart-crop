package me.jamespan.tech.smartcrop.domain;

/**
 *
 * @author panjiabang
 * @since  12/28/15
 */
public class Window extends Box {

    private double ratio = 0.0;
    private boolean inBodyArea;
    private Coordinate center;
    private int tag = 0;

    public Window(Coordinate upperLeft, Coordinate lowerRight) {
        super(upperLeft, lowerRight);
        this.inBodyArea = false;
        this.center = new Coordinate((upperLeft.getX() + lowerRight.getX()) / 2, (upperLeft.getY() + lowerRight.getY()) / 2);
    }

    public Window(int upperLeftX, int upperLeftY, int lowerRightX, int lowerRightY) {
        super(upperLeftX, upperLeftY, lowerRightX, lowerRightY);
        this.inBodyArea = false;
        this.center = new Coordinate((getUpperLeft().getX() + getLowerRight().getX()) / 2, (getUpperLeft().getY() + getLowerRight().getY()) / 2);
    }

    public double calcRatio(int backgroundPixelCnt) {
        int totalPixelCnt = getWidth() * getHeight();
        int bodyPixelCnt = totalPixelCnt - backgroundPixelCnt;
        ratio = 1.0 * bodyPixelCnt / totalPixelCnt;
        return ratio;
    }

    public double getRatio() {
        return ratio;
    }

    public Coordinate getCenter() {
        return center;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public boolean isInBodyArea() {
        return inBodyArea;
    }

    public void setInBodyArea(boolean inBodyArea) {
        this.inBodyArea = inBodyArea;
    }
}

