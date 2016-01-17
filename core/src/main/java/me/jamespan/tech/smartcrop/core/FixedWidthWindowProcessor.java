package me.jamespan.tech.smartcrop.core;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.jamespan.tech.smartcrop.domain.*;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author panjiabang
 * @since  1/3/16
 */
@Singleton
public class FixedWidthWindowProcessor implements WindowProcessor {

    private ImageProcessor processor;

    private ThresholdProcessor thresholdMaster;

    @Inject
    public FixedWidthWindowProcessor(ImageProcessor processor, ThresholdProcessor thresholdMaster) {
        this.processor = processor;
        this.thresholdMaster = thresholdMaster;
    }

    public WindowMatrix imageToWindows(BufferedImage input, int windowWidthInPixel, int windowHeightInPixel) {
        BufferedImage image = processor.toBinary(input);

        IntMatrix binary = processor.toMatrix(image);

        int width = image.getWidth();
        int height = image.getHeight();

        int windowCntX = (int) Math.ceil(width * 1.0 / windowWidthInPixel);
        int windowCntY = (int) Math.ceil(height * 1.0 / windowHeightInPixel);

        Window[][] windows = new Window[windowCntY][windowCntX];

        for (int i = 0; i < windowCntY; ++i) {
            for (int j = 0; j < windowCntX; ++j) {
                int iStart = i * windowHeightInPixel;
                int jStart = j * windowWidthInPixel;
                int iEnd = Math.min(iStart + windowHeightInPixel, height);
                int jEnd = Math.min(jStart + windowWidthInPixel, width);

                Window window = new Window(jStart, iStart, jEnd, iEnd);
                windows[i][j] = window;
                List<Integer> isBackgroundPixel = binary.traversal(iStart, iEnd, jStart, jEnd, new Matrix.Action<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer e, int i, int j) {
                        return e / 255;
                    }
                });
                int backgroundPixelCnt = sum(isBackgroundPixel);
                window.calcRatio(backgroundPixelCnt);
            }
        }
        return new WindowMatrix(windows, windowCntX, windowCntY);
    }

    public WindowMatrix markBodyArea(WindowMatrix winMatrix, final double ratioThreshold) {
        winMatrix.traversal(new Matrix.Action<Window, Object>() {
            @Override
            public Object apply(Window e, int i, int j) {
                e.setInBodyArea(e.getRatio() > ratioThreshold);
                return null;
            }
        });
        return winMatrix;
    }

    public WindowMatrix markBodyArea(WindowMatrix winMatrix) {
        double[][] ratios = new double[winMatrix.getHeight()][winMatrix.getWidth()];
        for (int i = 0; i < winMatrix.getHeight(); ++i) {
            for (int j = 0; j < winMatrix.getWidth(); ++j) {
                ratios[i][j] = winMatrix.getData()[i][j].getRatio();
            }
        }

        final double ratioThreshold = thresholdMaster.boom(ratios, winMatrix.getWidth(), winMatrix.getHeight());

        return markBodyArea(winMatrix, ratioThreshold);
    }

    private int countBodyAreas(WindowMatrix winMatrix) {
        List<Integer> isBody = winMatrix.traversal(new Matrix.Action<Window, Integer>() {
            @Override
            public Integer apply(Window e, int i, int j) {
                return e.isInBodyArea() ? 1 : 0;
            }
        });
        return sum(isBody);
    }

    public void spreadBodyArea(WindowMatrix winMatrix, int maxLoop) {
        int preBodyCnt = 0;
        int currBodyCnt = countBodyAreas(winMatrix);

        int loopCnt = 0;
        while (preBodyCnt != currBodyCnt) {
            preBodyCnt = currBodyCnt;
            loopCnt++;
            if (maxLoop > 0 && loopCnt > maxLoop) {
                break;
            }

            int clusterCnt = clusterByConnectivity(winMatrix) + 1;

            Map<Integer, Range> tagIRangeMap = Maps.newHashMap();
            Map<Integer, Range> tagJRangeMap = Maps.newHashMap();

            for (int tag = 1; tag < clusterCnt; ++tag) {
                tagIRangeMap.put(tag, new Range(Integer.MAX_VALUE, Integer.MIN_VALUE));
                tagJRangeMap.put(tag, new Range(Integer.MAX_VALUE, Integer.MIN_VALUE));
            }

            for (int i = 0; i < winMatrix.getHeight(); ++i) {
                for (int j = 0; j < winMatrix.getWidth(); ++j) {
                    Window window = winMatrix.getData()[i][j];
                    if (window.getTag() == 0) {
                        continue;
                    }
                    Range iRange = tagIRangeMap.get(window.getTag());
                    iRange.setStartIncluded(Math.min(iRange.getStartIncluded(), i));
                    iRange.setEndExcluded(Math.max(iRange.getEndExcluded(), i + 1));
                    Range jRange = tagJRangeMap.get(window.getTag());
                    jRange.setStartIncluded(Math.min(jRange.getStartIncluded(), j));
                    jRange.setEndExcluded(Math.max(jRange.getEndExcluded(), j + 1));
                }
            }

            for (int tag = 1; tag < clusterCnt; ++tag) {
                final int tmpTag = tag;
                Range iRange = tagIRangeMap.get(tag);
                Range jRange = tagJRangeMap.get(tag);

                winMatrix.traversal(iRange.getStartIncluded(), iRange.getEndExcluded(), jRange.getStartIncluded(), jRange.getEndExcluded(),
                        new Matrix.Action<Window, Object>() {
                            @Override
                            public Object apply(Window e, int i, int j) {
                                e.setTag(tmpTag);
                                e.setInBodyArea(true);
                                return null;
                            }
                        });
            }

            currBodyCnt = countBodyAreas(winMatrix);
        }
    }

    private int clusterByConnectivity(WindowMatrix winMatrix) {
        boolean[][] visited = new boolean[winMatrix.getHeight()][winMatrix.getWidth()];
        for (int i = 0; i < winMatrix.getHeight(); ++i) {
            for (int j = 0; j < winMatrix.getWidth(); ++j) {
                visited[i][j] = false;
            }
        }

        int tag = 0;
        for (int i = 0; i < winMatrix.getHeight(); ++i) {
            for (int j = 0; j < winMatrix.getWidth(); ++j) {
                Window window = winMatrix.getData()[i][j];
                if (!visited[i][j] && window.isInBodyArea()) {
                    dfs(winMatrix, visited, i, j, ++tag, 2);
                } else {
                    visited[i][j] = true;
                }
            }
        }
        return tag;
    }

    private void dfs(WindowMatrix winMatrix, boolean[][] visited, int iStart, int jStart, int tag, int radius) {
        Stack<MatrixCoordinate> stack = new Stack<MatrixCoordinate>();
        stack.push(new MatrixCoordinate(iStart, jStart));
        while (!stack.isEmpty()) {
            MatrixCoordinate current = stack.pop();
            visited[current.getI()][current.getJ()] = true;
            Window window = winMatrix.getData()[current.getI()][current.getJ()];
            if (!window.isInBodyArea()) {
                continue;
            }
            window.setTag(tag);
            for (int i = Math.max(0, current.getI() - radius); i < Math.min(winMatrix.getHeight(), current.getI() + 1 + radius); ++i) {
                for (int j = Math.max(0, current.getJ() - radius); j < Math.min(winMatrix.getWidth(), current.getJ() + 1 + radius); ++j) {
                    if (!visited[i][j] && winMatrix.getData()[i][j].isInBodyArea()) {
                        stack.push(new MatrixCoordinate(i, j));
                    }
                }
            }
        }
    }

    private int sum(List<Integer> list) {
        int sum = 0;
        for (Integer i : list) {
            sum += i;
        }
        return sum;
    }
}
