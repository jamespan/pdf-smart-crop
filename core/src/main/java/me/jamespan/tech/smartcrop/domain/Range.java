package me.jamespan.tech.smartcrop.domain;

/**
 * Half open interval, close at the left
 * @author panjiabang
 * @since  12/30/15
 */
public class Range {
    int startIncluded;
    int endExcluded;

    public Range(int startIncluded, int endExcluded) {
        this.startIncluded = startIncluded;
        this.endExcluded = endExcluded;
    }

    public int getStartIncluded() {
        return startIncluded;
    }

    public int getEndExcluded() {
        return endExcluded;
    }

    public void setEndExcluded(int endExcluded) {
        this.endExcluded = endExcluded;
    }

    public void setStartIncluded(int startIncluded) {
        this.startIncluded = startIncluded;
    }

    @Override
    public String toString() {
        return "[" + startIncluded + ", " + endExcluded + ')';
    }
}
