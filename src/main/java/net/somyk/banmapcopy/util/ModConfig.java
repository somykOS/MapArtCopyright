package net.somyk.banmapcopy.util;

import net.fabricmc.loader.api.FabricLoader;
import org.simpleyaml.configuration.comments.format.YamlCommentFormat;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static net.somyk.banmapcopy.BanMapCopy.*;

public class ModConfig {

    public static void registerConfigs() {
        Path path = (FabricLoader.getInstance().getConfigDir());
        Path path2 = path.resolve(Paths.get("BanMapCopy"));
        final YamlFile config = new YamlFile((path2.resolve( "config.yml" ).toFile()).getAbsolutePath());
        try {
            if (!config.exists()) {
                config.createNewFile();
                LOGGER.info("New file has been created: " + (path2.resolve( "config.yml" ).toFile()).getPath());
            } else {
                LOGGER.info((path2.resolve( "config.yml" ).toFile()).getPath() + " already exists, loading configurations...");
            }
            config.loadWithComments();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        config.addDefault("copyright", false);
        config.addDefault("authorsCanCopy", false);
        config.addDefault("cleanMap", false);

        config.setCommentFormat(YamlCommentFormat.PRETTY);
        config.setComment("copyright", "Adds NBT 'authors' while creating new filled map");
        config.setComment("authorsCanCopy", "Will work if 'copyright' is/was 'true'");
        config.setComment("cleanMap", "Allows to clean map with a bucket of water in a cartography table");

        try {
            config.save();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean getValue(String key){
        Path path = (FabricLoader.getInstance().getConfigDir());
        Path path2 = path.resolve(Paths.get("BanMapCopy"));
        final YamlFile config = new YamlFile((path2.resolve( "config.yml" ).toFile()).getAbsolutePath());
        try {
            config.loadWithComments();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return config.getBoolean(key);
    }

    public static void setValue(String key, Object newValue){
        Path path = (FabricLoader.getInstance().getConfigDir());
        Path path2 = path.resolve(Paths.get("BanMapCopy"));
        final YamlFile config = new YamlFile((path2.resolve( "config.yml" ).toFile()).getAbsolutePath());

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
