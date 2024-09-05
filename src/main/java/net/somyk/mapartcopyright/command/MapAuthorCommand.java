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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class MapAuthorCommand {
    private static final Style STYLE_SUCCESS = Style.EMPTY.withColor(Formatting.DARK_GREEN);
    private static final Style STYLE_FAIL = Style.EMPTY.withColor(Formatting.DARK_RED);
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> mapArtNode = CommandManager.literal("map-art").build();

        mapArtNode.addChild(buildSubCommand("add", MapAuthorCommand::add));
        mapArtNode.addChild(buildSubCommand("remove", MapAuthorCommand::remove));

        dispatcher.getRoot().addChild(mapArtNode);
    }

    private static LiteralCommandNode<ServerCommandSource> buildSubCommand(String name, CommandExecutor executor) {
        return CommandManager.literal(name)
                .then(CommandManager.argument("player", StringArgumentType.greedyString())
                        .executes(context -> executor.execute(context, StringArgumentType.getString(context, "player"))))
                .build();
    }

    @FunctionalInterface
    private interface CommandExecutor {
        int execute(CommandContext<ServerCommandSource> context, String playerName);
    }

    private static int add(CommandContext<ServerCommandSource> context, String playerName) {
        return modifyMapArt(context, playerName, true);
    }

    private static int remove(CommandContext<ServerCommandSource> context, String playerName) {
        return modifyMapArt(context, playerName, false);
    }

    private static int modifyMapArt(CommandContext<ServerCommandSource> context, String playerName, boolean isAdding) {
        PlayerEntity player = context.getSource().getPlayer();
        if (player == null) return -1;

        ItemStack itemStack = player.getMainHandStack();

        if (!isValidMapArt(itemStack)) {
            sendFeedback(context, "You should have map art in main hand", STYLE_FAIL);
            return -1;
        }

        if (!canModifyMapArt(player, itemStack, isAdding)) {
            notAllowedModify(context);
            return -1;
        }

        if (!VALID_NAME_PATTERN.matcher(playerName).matches()) {
            sendFeedback(context, "Player name should not have any special character", STYLE_FAIL);
            return -1;
        }

        boolean success = modifyAuthorNBT(itemStack, playerName, isAdding ? 1 : 0);
        if (!success) {
            String message = isAdding ? "There is already the author" : "This author is not found";
            sendFeedback(context, message, STYLE_FAIL);
            return -1;
        }


        String message = playerName + " successfully " + (isAdding ? "added to " : "removed from ") + "map art authors";
        sendFeedback(context, message, STYLE_SUCCESS);

        return 1;
    }

    private static boolean isValidMapArt(ItemStack itemStack) {
        return itemStack.isOf(Items.FILLED_MAP);
    }

    private static boolean canModifyMapArt(PlayerEntity player, ItemStack itemStack, boolean isAdding) {
        String permission = isAdding ? "add-author" : "remove-author";
        return isMainAuthor(itemStack, player) || Permissions.check(player, MOD_ID + "." + permission);
    }

    private static void sendFeedback(CommandContext<ServerCommandSource> context, String message, Style style) {
        context.getSource().sendFeedback(() -> Text.literal(message).setStyle(style), false);
    }

    private static void notAllowedModify(CommandContext<ServerCommandSource> context) {
        sendFeedback(context, "You're not allowed to modify this map", STYLE_FAIL);
    }
}