package me.jamespan.tech.smartcrop.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author panjiabang
 * @since  1/3/16
 */
public abstract class AbstractMatrix<T> implements Matrix<T> {

    protected T[][] data;
    protected int width;
    protected int height;

    @Override
    public T[][] getData() {
        return data;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public <R> List<R> traversal(Action<T, R> action) {
        return traversal(0, height, 0, width, action);
    }

    @Override
    public <R> List<R> traversal(int iStartIncluded, int iEndExcluded, int jStartIncluded, int jEndExcluded, Action<T, R> action) {
        List<R> l = new ArrayList<R>(/*height * width*/);
        for (int i = iStartIncluded; i < iEndExcluded; ++i) {
            for (int j = jStartIncluded; j < jEndExcluded; ++j) {
                R result = action.apply(data[i][j], i, j);
                if (result != null) {
                    l.add(result);
                }
            }
        }
        return l;
    }
}
