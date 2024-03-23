package io.github.pikibanana.features;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.awt.*;

@Environment(EnvType.CLIENT)
public record FinderConfiguration(boolean enabled, Color color) {
    public boolean isDisabled(){
        return !enabled;
    }
}
