package net.somyk.banmapcopy;

import net.fabricmc.api.ModInitializer;

import net.somyk.banmapcopy.util.ModCommandsRegister;
import net.somyk.banmapcopy.util.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BanMapCopy implements ModInitializer {
	public static final String MOD_ID = "banmapcopy";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModConfig.registerConfigs();
		ModCommandsRegister.registerCommands();
	}
}