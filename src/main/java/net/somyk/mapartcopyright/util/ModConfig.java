package net.somyk.mapartcopyright.util;

import net.fabricmc.loader.api.FabricLoader;
import org.simpleyaml.configuration.comments.format.YamlCommentFormat;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.nio.file.Path;

import static net.somyk.mapartcopyright.MapArtCopyright.*;

public class ModConfig {

    private static final String CONFIG = MOD_ID + ".yml";
    private static final Path configDir = FabricLoader.getInstance().getConfigDir();
    private static final Path configFilePath = configDir.resolve(CONFIG);

    public static void registerConfigs() {

        final YamlFile config = new YamlFile(configFilePath.toFile().getAbsolutePath());
        try {
            if (!config.exists()) {
                config.createNewFile();
                LOGGER.info("[{}]: config has been created: {}", MOD_ID, configFilePath.toFile().getPath());
            } else {
                LOGGER.info("[{}]: loading configurations..", MOD_ID);
            }
            config.loadWithComments();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        config.addDefault("copyright", true);
        config.addDefault("disableCopy", false);
        config.addDefault("authorsCanCopy", true);
        config.addDefault("cleanMap", false);

        config.setCommentFormat(YamlCommentFormat.PRETTY);
        config.setComment("copyright", "Adds NBT 'authors' when creating a new filled map");
        config.setComment("disableCopy", "Nobody can make a copy of a map (except authors if 'authorsCanCopy' is 'true')");
        config.setComment("authorsCanCopy", "Works if 'copyright' is/was 'true'");
        config.setComment("cleanMap", "Allows to clean a map with a bucket of water in a cartography table");

        try {
            config.save();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean getBooleanValue(String key){
        final YamlFile config = new YamlFile((configFilePath.toFile()).getAbsolutePath());
        try {
            config.loadWithComments();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return config.getBoolean(key);
    }

    public static String getStringValue(String key){
        final YamlFile config = new YamlFile((configFilePath.toFile()).getAbsolutePath());
        try {
            config.loadWithComments();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return config.getString(key);
    }

    public static void setValue(String key, Object newValue){
        final YamlFile config = new YamlFile((configFilePath.toFile()).getAbsolutePath());

        try {
            config.loadWithComments();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        config.set(key, newValue);

        try {
            config.save();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
