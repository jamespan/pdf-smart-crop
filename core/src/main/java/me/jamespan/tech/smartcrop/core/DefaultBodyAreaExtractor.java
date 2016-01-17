package me.jamespan.tech.smartcrop.core;

import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import me.jamespan.tech.smartcrop.domain.Box;
import me.jamespan.tech.smartcrop.domain.Range;
import me.jamespan.tech.smartcrop.domain.Window;
import me.jamespan.tech.smartcrop.domain.WindowMatrix;

import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author panjiabang
 * @since  1/3/16
 */
@Singleton
public class DefaultBodyAreaExtractor implements BodyAreaExtractor {

    @Override
    public Box extract(WindowMatrix windowMatrix) {
        Range widthRange = bestWidth(windowMatrix);
        Range heightRange = bestHeight(windowMatrix);

        if (widthRange == null || heightRange == null) {
            return null;
        }

        return new Box(widthRange.getStartIncluded(), heightRange.getStartIncluded()
                , widthRange.getEndExcluded(), heightRange.getEndExcluded());
    }

    private Range bestWidth(WindowMatrix winMatrix) {
        Map<Integer, Integer> widthCnt = Maps.newHashMap();
        Map<Integer, Range> widthRange = Maps.newHashMap();
        for (int i = 0; i < winMatrix.getHeight(); ++i) {
            int start = Integer.MAX_VALUE;
            int end = 0;
            for (int j = 0; j < winMatrix.getWidth(); ++j) {
                Window window = winMatrix.getData()[i][j];
                if (!window.isInBodyArea()) {
                    continue;
                }
                start = Math.min(start, window.getUpperLeft().getX());
                end = Math.max(end, window.getLowerRight().getX());
            }
            int bodyWidth = end - start;
            if (bodyWidth > 0) {
                if (!widthCnt.containsKey(bodyWidth)) {
                    widthCnt.put(bodyWidth, 0);
                }
                int cnt = widthCnt.get(bodyWidth);
                widthCnt.put(bodyWidth, cnt + 1);

                if (!widthRange.containsKey(bodyWidth)) {
                    widthRange.put(bodyWidth, new Range(start, end));
                }
                Range range = widthRange.get(bodyWidth);
                widthRange.put(bodyWidth,
                        new Range(Math.min(start, range.getStartIncluded()),
                                Math.max(end, range.getEndExcluded())));
            }
        }

        return widthRange.get(keyWithMaxValue(widthCnt));
    }

    private Range bestHeight(WindowMatrix winMatrix) {
        Map<Integer, Integer> heightCnt = Maps.newHashMap();
        Map<Integer, Range> heightRange = Maps.newHashMap();
        for (int j = 0; j < winMatrix.getWidth(); ++j) {
            int start = Integer.MAX_VALUE;
            int end = 0;
            for (int i = 0; i < winMatrix.getHeight(); ++i) {
                Window window = winMatrix.getData()[i][j];
                if (!window.isInBodyArea()) {
                    continue;
                }
                start = Math.min(start, window.getUpperLeft().getY());
                end = Math.max(end, window.getLowerRight().getY());
            }
            int bodyHeight = end - start;
            if (bodyHeight > 0) {
                if (!heightCnt.containsKey(bodyHeight)) {
                    heightCnt.put(bodyHeight, 0);
                }
                int cnt = heightCnt.get(bodyHeight);
                heightCnt.put(bodyHeight, cnt + 1);

                if (!heightRange.containsKey(bodyHeight)) {
                    heightRange.put(bodyHeight, new Range(start, end));
                }
                Range range = heightRange.get(bodyHeight);
                heightRange.put(bodyHeight,
                        new Range(Math.min(start, range.getStartIncluded()),
                                Math.max(end, range.getEndExcluded())));
            }
        }

        return heightRange.get(keyWithMaxValue(heightCnt));
    }

    private Integer keyWithMaxValue(Map<Integer, Integer> map) {
        Integer key = null;
        Integer value = null;
        Iterator<Map.Entry<Integer, Integer>> it = map.entrySet().iterator();
        if (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            key = entry.getKey();
            value = entry.getKey() * entry.getValue();
        }
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            if (entry.getKey() * entry.getValue() > value) {
                key = entry.getKey();
            }
        }
        return key;
    }
}
