package net.somyk.mapartcopyright;

import net.fabricmc.api.ModInitializer;

import net.somyk.mapartcopyright.util.ModCommandsRegister;
import net.somyk.mapartcopyright.util.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapArtCopyright implements ModInitializer {
	public static final String MOD_ID = "mapartcopyright";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModConfig.registerConfigs();
		ModCommandsRegister.registerCommands();
	}
}