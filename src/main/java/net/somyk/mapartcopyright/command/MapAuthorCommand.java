package net.somyk.mapartcopyright.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import static net.minecraft.server.command.CommandManager.*;
import static net.somyk.mapartcopyright.MapArtCopyright.MOD_ID;
import static net.somyk.mapartcopyright.util.AuthorMethods.*;
import static net.somyk.mapartcopyright.util.ModConfig.*;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class MapAuthorCommand {
    private static final Style STYLE_SUCCESS = Style.EMPTY.withColor(Formatting.GREEN);
    private static final Style STYLE_FAIL = Style.EMPTY.withColor(Formatting.RED);
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[a-z0-9_]{3,}$", Pattern.CASE_INSENSITIVE);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> mapArtNode = CommandManager.literal("map-art")
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("player", StringArgumentType.greedyString())
                                .executes(context -> modifyMapArt(context, StringArgumentType.getString(context, "player"), true))))
                .then(CommandManager.literal("remove")
                        .then(CommandManager.argument("player", StringArgumentType.greedyString())
                                .executes(context -> modifyMapArt(context, StringArgumentType.getString(context, "player"), false))))
                .then(CommandManager.literal("public-domain")
                        .requires(source -> getBooleanValue(publicDomain) && getBooleanValue(disableCopy))
                        .executes(MapAuthorCommand::publicDomain))
                .build();

        dispatcher.getRoot().addChild(mapArtNode);
    }

    private static int publicDomain(CommandContext<ServerCommandSource> context) {
        return executeWithPlayerAndMapArt(context, (player, itemStack) -> {
            if (!isMainAuthor(itemStack, player)) {
                return sendFeedback(context, getStringValue(messageNotAllowedToModify, LANG_CONFIG), STYLE_FAIL);
            }
            if (toPublicDomain(itemStack)) {
                return sendFeedback(context, getStringValue(messageAddedToPublicDomain, LANG_CONFIG), STYLE_SUCCESS);
            } else {
                return sendFeedback(context, getStringValue(messageRemovedFromPublicDomain, LANG_CONFIG), STYLE_SUCCESS);
            }
        });
    }

    private static int modifyMapArt(CommandContext<ServerCommandSource> context, String playerName, boolean isAdding) {
        return executeWithPlayerAndMapArt(context, (player, itemStack) -> {
            if (!canModifyMapArt(player, itemStack, isAdding)) {
                return sendFeedback(context, getStringValue(messageNotAllowedToModify, LANG_CONFIG), STYLE_FAIL);
            }
            if (!VALID_NAME_PATTERN.matcher(playerName).matches()) {
                return sendFeedback(context, getStringValue(messageInvalidName, LANG_CONFIG), STYLE_FAIL);
            }
            boolean success = modifyAuthorNBT(itemStack, playerName, isAdding ? 1 : 0);
            if (!success) {
                String key = isAdding ? getStringValue(messageAuthorExists, LANG_CONFIG) : getStringValue(messageAuthorNotFound, LANG_CONFIG);
                return sendFeedback(context, key, STYLE_FAIL);
            }
            String playerName1 = (isAdding ? "§6" : "§c") + playerName + "§a";
            //String key = (isAdding ? "§6" : "§c") + playerName + "§a successfully " + (isAdding ? "added to " : "removed from ") + "canvas authors";
            String key = isAdding ? getStringValue(messageAuthorAdded, LANG_CONFIG).formatted(playerName1) : getStringValue(messageAuthorRemoved, LANG_CONFIG).formatted(playerName1);
            return sendFeedback(context, key, null);
        });
    }

    private static int executeWithPlayerAndMapArt(CommandContext<ServerCommandSource> context, PlayerCanvasExecutor executor) {
        PlayerEntity player = context.getSource().getPlayer();
        if (player == null) return -1;

        ItemStack itemStack = player.getMainHandStack();
        if (!itemStack.isOf(Items.FILLED_MAP)) {
            return sendFeedback(context, getStringValue(messageNoMapArt, LANG_CONFIG), STYLE_FAIL);
        }

        return executor.execute(player, itemStack);
    }

    private static boolean canModifyMapArt(PlayerEntity player, ItemStack itemStack, boolean isAdding) {
        String permission = isAdding ? "add-author" : "remove-author";
        return isMainAuthor(itemStack, player) || Permissions.check(player, MOD_ID + "." + permission);
    }

    private static int sendFeedback(CommandContext<ServerCommandSource> context, String translationKey, Style style) {
        MutableText message = Text.literal(translationKey);
        if (style != null) {
            message.setStyle(style);
        }
        context.getSource().sendFeedback(() -> message, false);
        return (style == STYLE_FAIL) ? -1 : 1;
    }

    @FunctionalInterface
    private interface PlayerCanvasExecutor {
        int execute(PlayerEntity player, ItemStack itemStack);
    }
}