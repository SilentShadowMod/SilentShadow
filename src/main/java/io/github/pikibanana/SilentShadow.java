package io.github.pikibanana;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SilentShadow implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("SilentShadow");

	@Override
	public void onInitialize() {
		LOGGER.info("[SilentShadow] SilentShadow is loaded!");
	}
}