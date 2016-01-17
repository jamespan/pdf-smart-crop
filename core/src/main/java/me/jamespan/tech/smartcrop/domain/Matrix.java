package me.jamespan.tech.smartcrop.domain;

import java.util.List;

/**
 * 2D Matrix, with traversal operation
 * @author panjiabang
 * @since  1/3/16
 */
public interface Matrix<T> {

    public static interface Action<T, R> {
        R apply(T e, int i, int j);
    }

    T[][] getData();

    int getWidth();

    int getHeight();

    <R> List<R> traversal(Action<T, R> action);

    <R> List<R> traversal(int iStartIncluded, int iEndExcluded
            , int jStartIncluded, int jEndExcluded
            , Action<T, R> action);
}
