package net.somyk.mapartcopyright.util;

import net.fabricmc.loader.api.FabricLoader;
import org.simpleyaml.configuration.comments.format.YamlCommentFormat;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.configuration.implementation.api.QuoteStyle;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static net.somyk.mapartcopyright.MapArtCopyright.*;

public class ModConfig {

    private static final Path configDir = FabricLoader.getInstance().getConfigDir().resolve(Paths.get(MOD_ID));

    public static final String CONFIG = MOD_ID + ".yml";
    private static final Path configFilePath = configDir.resolve(CONFIG);

    public static final String maxPlayerLore = "maxPlayersInLore";
    public static final String disableCopy = "disableCopy";
    public static final String authorsCopy = "authorsCanCopy";
    public static final String publicDomain = "publicDomainFeature";
    public static final String cleanMap = "cleanMap";


    private static final String LANG = "lang";
    public static final String LANG_CONFIG = LANG + ".yml";
    private static final Path langFilePath = configDir.resolve(LANG_CONFIG);

    public static final String messageConfigValue = "message.configValue";
    public static final String messageConfigValueUpdated = "message.configValueUpdated";
    public static final String messageNotAllowedToModify = "message.notAllowedToModify";
    public static final String messageInvalidName = "message.invalidName";
    public static final String messageAuthorExists = "message.authorExists";
    public static final String messageAuthorNotFound = "message.authorNotFound";
    public static final String messageAuthorAdded = "message.authorAdded";
    public static final String messageAuthorRemoved = "message.authorRemoved";
    public static final String messageNoMapArt = "message.noMapArt";
    public static final String messageAddedToPublicDomain = "message.addedToPublicDomain";
    public static final String messageRemovedFromPublicDomain = "message.removedFromPublicDomain";
    public static final String lorePublicDomain = "lore.publicDomain";

    public static void registerConfigs() {

        final YamlFile config = new YamlFile(configFilePath.toFile().getAbsolutePath());
        final YamlFile lang = new YamlFile(langFilePath.toFile().getAbsolutePath());
        try {
            if (!config.exists()) {
                config.createNewFile();
                LOGGER.info("[{}]: config has been created: {}", MOD_ID, configFilePath.toFile().getPath());
            } else {
                LOGGER.info("[{}]: loaded config", MOD_ID);
            }
            config.loadWithComments();

            if (!lang.exists()) {
                lang.createNewFile();
                LOGGER.info("[{}]: lang file has been created: {}", MOD_ID, langFilePath.toFile().getPath());
            } else {
                LOGGER.info("[{}]: loaded lang file", MOD_ID);
            }
            lang.loadWithComments();

        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        config.addDefault(maxPlayerLore, 3);
        config.addDefault(disableCopy, true);
        config.addDefault(authorsCopy, true);
        config.addDefault(publicDomain, true);
        config.addDefault(cleanMap, false);

        config.setCommentFormat(YamlCommentFormat.PRETTY);
        config.setComment(maxPlayerLore, "You can change how many of them are displayed");
        config.setComment(disableCopy, "Nobody can make a copy of a map (except authors if 'authorsCanCopy' is 'true')");
        config.setComment(publicDomain, "Main author can transfer a canvas to the public domain (all players can copy the canvas).\n" +
                "Doesn't work if `disableCopy` is `false`");
        config.setComment(cleanMap, "Allows to clean a map with a bucket of water in a cartography table");

        lang.addDefault(messageConfigValue, "'%s' currently is '%s'");
        lang.addDefault(messageConfigValueUpdated, "'%s' updated to '%s'");
        lang.addDefault(messageNotAllowedToModify, "You're not allowed to modify this map");
        lang.addDefault(messageInvalidName, "Player name must be at least 3 characters and have no special symbols");
        lang.addDefault(messageAuthorExists, "There is already the author");
        lang.addDefault(messageAuthorNotFound, "This author is not found");
        lang.addDefault(messageAuthorAdded, "%s successfully added to art authors");
        lang.addDefault(messageAuthorRemoved, "%s successfully removed from art authors");
        lang.addDefault(messageNoMapArt, "You should have map in main hand");
        lang.addDefault(messageAddedToPublicDomain, "Transferred to the public domain");
        lang.addDefault(messageRemovedFromPublicDomain, "Removed from the public domain (only this stack)");
        lang.addDefault(lorePublicDomain, "Public domain");

        lang.options().quoteStyleDefaults().setDefaultQuoteStyle(QuoteStyle.DOUBLE);
//        lang.setCommentFormat(YamlCommentFormat.PRETTY);
//        lang.setComment(messageConfigValue, "'disableCopy' currently is 'false'");
//        lang.setComment(messageConfigValueUpdated, "'disableCopy' updated to 'true'");

        try {
            config.save();
            lang.save();
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

    public static int getIntValue(String key) {
        final YamlFile config = new YamlFile((configFilePath.toFile()).getAbsolutePath());
        try {
            config.loadWithComments();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return config.getInt(key);
    }

    public static String getStringValue(String key, String file){
        YamlFile yamlFile = new YamlFile();
        if(file.equals(CONFIG)) yamlFile = new YamlFile((configFilePath.toFile()).getAbsolutePath());
        else if (file.equals(LANG_CONFIG)) yamlFile = new YamlFile((langFilePath.toFile()).getAbsolutePath());

        try {
            yamlFile.loadWithComments();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return yamlFile.getString(key);
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
