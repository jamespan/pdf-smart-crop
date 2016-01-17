package me.jamespan.tech.smartcrop.shell;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultHistoryFileNameProvider;
import org.springframework.stereotype.Component;

/**
 * History Provider of Smart Crop CLI
 * @author panjiabang
 * @since  1/17/16
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SmartCropHistoryFileNameProvider extends DefaultHistoryFileNameProvider {

    public String getHistoryFileName() {
        return "smart-crop.log";
    }

    public String getProviderName() {
        return "smart crop history provider";
    }
}
