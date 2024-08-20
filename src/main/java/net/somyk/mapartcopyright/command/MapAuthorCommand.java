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
import net.somyk.mapartcopyright.util.ModConfig;

import java.util.regex.Pattern;

public class MapAuthorCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> mapAuthorNode = CommandManager
                .literal("mapAuthor")
                .requires(source -> ModConfig.getBooleanValue("authorsCanAddAuthors") ||
                        Permissions.check(source, MOD_ID + ".addauthor") ||
                        Permissions.check(source, MOD_ID + ".removeauthor"))
                .build();

        LiteralCommandNode<ServerCommandSource> addNode = CommandManager
                .literal("add")
                .requires(source -> ModConfig.getBooleanValue("authorsCanAddAuthors") ||
                        Permissions.check(source, MOD_ID + ".addauthor"))
                .then(CommandManager.argument("player", StringArgumentType.greedyString())
                        .executes(context -> run(context, StringArgumentType.getString(context, "player"), 1)))
                .build();

        LiteralCommandNode<ServerCommandSource> removeNode = CommandManager
                .literal("remove")
                .requires(Permissions.require(MOD_ID + ".removeauthor"))
                .then(CommandManager.argument("player", StringArgumentType.greedyString())
                        .executes(context -> run(context, StringArgumentType.getString(context, "player"), 0)))
                .build();

        dispatcher.getRoot().addChild(mapAuthorNode);
        mapAuthorNode.addChild(addNode);
        mapAuthorNode.addChild(removeNode);
    }

    private static final Style STYLE = Style.EMPTY.withColor(Formatting.GRAY);
    private static final Pattern PATTERN = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);

    private static int run(CommandContext<ServerCommandSource> context, String playerName, int operation) {


        PlayerEntity player = context.getSource().getPlayer();
        if (player == null) return -1;

        ItemStack itemStack = player.getMainHandStack();

        if (!itemStack.isOf(Items.FILLED_MAP)) {
            context.getSource().sendFeedback(() -> Text.literal("You should have filled map in main hand").setStyle(STYLE), false);
            return -1;
        } else if (!(isAuthor(itemStack, player) || Permissions.check(player, MOD_ID + ".addauthor"))) {
            context.getSource().sendFeedback(() -> Text.literal("You're not allowed to modify this map").setStyle(STYLE), false);
            return -1;
        }

        if (PATTERN.matcher(playerName).find()) {
            context.getSource().sendFeedback(() -> Text.literal("Player name should not have any special character").setStyle(STYLE), false);
            return -1;
        } else if (operation == 1 && !modifyAuthorNBT(itemStack, playerName, operation)) {
            context.getSource().sendFeedback(() -> Text.literal("There is already the author").setStyle(STYLE), false);
            return -1;
        } else if (operation == 0 && !modifyAuthorNBT(itemStack, playerName, operation)) {
            context.getSource().sendFeedback(() -> Text.literal("This author is not found").setStyle(STYLE), false);
            return -1;
        }

        setAuthorLore(itemStack);

        return 1;
    }
}