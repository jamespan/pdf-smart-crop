package me.jamespan.tech.smartcrop.core;

/**
 *
 * @author panjiabang
 * @since  1/3/16
 */
public interface ThresholdProcessor {

    int boom(int[][] matrix, int width, int height);

    double boom(double[][] matrix, int width, int height);

}
