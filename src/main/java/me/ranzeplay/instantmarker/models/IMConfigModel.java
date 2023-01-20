package me.ranzeplay.instantmarker.models;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

@Modmenu(modId = "instantmarker")
@Config(name = "instantmarker-config", wrapperName = "IMConfig")
public class IMConfigModel {
    // Prevent sharing markers to others across multiplayer server
    public boolean localMode = false;

    // Enable a ding sound on marking position
    public boolean enableSound = true;

    // Share nearby items of your marker to others
    public boolean shareItems = true;

    public IMConfigModel(boolean localMode, boolean enableSound, boolean shareItems) {
        this.localMode = localMode;
        this.enableSound = enableSound;
        this.shareItems = shareItems;
    }

    public static IMConfigModel load(me.ranzeplay.instantmarker.models.IMConfig config) {
        return new IMConfigModel(config.localMode(), config.enableSound(), config.shareItems());
    }

    public IMConfigModel() {
    }
}
