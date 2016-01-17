package me.jamespan.tech.smartcrop.core;

import com.google.inject.AbstractModule;

/**
 *
 * @author panjiabang
 * @since  1/3/16
 */
public class CropModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ImageProcessor.class).to(DefaultImageProcessor.class);
        bind(BodyDetector.class).to(FixedSlideWindowDetector.class);
        bind(WindowProcessor.class).to(FixedWidthWindowProcessor.class);
        bind(ThresholdProcessor.class).to(ThresholdMaster.class);
        bind(BodyDetector.class).to(FixedSlideWindowDetector.class);
        bind(BodyAreaExtractor.class).to(DefaultBodyAreaExtractor.class);
    }
}
