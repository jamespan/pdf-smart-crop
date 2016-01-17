package me.jamespan.tech.smartcrop.shell;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultPromptProvider;
import org.springframework.stereotype.Component;

/**
 * Prompt Provider of Smart Crop CLI
 * @author panjiabang
 * @since  1/3/16
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SmartCropPromptProvider extends DefaultPromptProvider {

    @Override
    public String getPrompt() {
        return "smart-crop>";
    }

    @Override
    public String getProviderName() {
        return "smart crop prompt provider";
    }
}
