package me.jamespan.tech.smartcrop.core;

import com.google.inject.Singleton;

/**
 *
 * @author panjiabang
 * @since  1/3/16
 */
@Singleton
public class ThresholdMaster implements ThresholdProcessor {

    @Override
    public int boom(int[][] matrix, int width, int height) {
        int[] histogram = new int[256];
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                histogram[matrix[i][j]]++;
            }
        }

        return find(histogram, width * height);
    }

    @Override
    public double boom(double[][] matrix, int width, int height) {

        int[] histogram = new int[256];
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }

        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                min = Math.min(min, matrix[i][j]);
                max = Math.max(max, matrix[i][j]);
            }
        }

        double step = (max - min) / 255;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                histogram[(int) ((matrix[i][j] - min) / step)] += 1;
            }
        }

        int threshold = find(histogram, width * height);
        return min + threshold * step;
    }

    private int find(int[] histogram, int total) {
        float sum = 0;
        for (int i = 0; i < 256; ++i) {
            sum += i * histogram[i];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold1 = 0;
        int threshold2 = 0;

        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;

            if(wF == 0) break;

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if(varBetween >= varMax) {
                threshold1 = i;
                if (varBetween > varMax) {
                    threshold2 = i;
                }
                varMax = varBetween;
            }
        }

        return (threshold1 + threshold2) / 2;
    }

}
