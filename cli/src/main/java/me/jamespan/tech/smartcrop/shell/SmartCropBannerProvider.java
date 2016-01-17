package me.jamespan.tech.smartcrop.shell;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

/**
 * Banner Provider of Smart Crop CLI
 * @author panjiabang
 * @since  1/3/16
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SmartCropBannerProvider extends DefaultBannerProvider {

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String getBanner() {
        StringBuilder buf = new StringBuilder();
        buf.append(" _____                      _     _____                 ").append(OsUtils.LINE_SEPARATOR);
        buf.append("/  ___|                    | |   /  __ \\                ").append(OsUtils.LINE_SEPARATOR);
        buf.append("\\ `--. _ __ ___   __ _ _ __| |_  | /  \\/_ __ ___  _ __  ").append(OsUtils.LINE_SEPARATOR);
        buf.append(" `--. \\ '_ ` _ \\ / _` | '__| __| | |   | '__/ _ \\| '_ \\ ").append(OsUtils.LINE_SEPARATOR);
        buf.append("/\\__/ / | | | | | (_| | |  | |_  | \\__/\\ | | (_) | |_) |").append(OsUtils.LINE_SEPARATOR);
        buf.append("\\____/|_| |_| |_|\\__,_|_|   \\__|  \\____/_|  \\___/| .__/ ").append(OsUtils.LINE_SEPARATOR);
        buf.append("                                                 | |    ").append(OsUtils.LINE_SEPARATOR);
        buf.append("                                                 |_|    ").append(OsUtils.LINE_SEPARATOR);
        buf.append("Version: ").append(this.getVersion());
        return buf.toString();
    }

    @Override
    public String getWelcomeMessage() {
        return "Welcome to " + getProviderName() + ". For assistance press TAB or type \"help\" then hit ENTER.";
    }

    @Override
    public String getProviderName() {
        return "Smart Crop CLI";
    }
}
