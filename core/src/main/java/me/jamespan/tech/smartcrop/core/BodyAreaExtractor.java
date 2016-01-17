package me.jamespan.tech.smartcrop.core;

import me.jamespan.tech.smartcrop.domain.Box;
import me.jamespan.tech.smartcrop.domain.WindowMatrix;

/**
 *
 * @author panjiabang
 * @since  1/3/16
 */
public interface BodyAreaExtractor {

    Box extract(WindowMatrix windowMatrix);
}
