package net.somyk.mapartcopyright.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.*;
import static net.somyk.mapartcopyright.MapArtCopyright.MOD_ID;
import static net.somyk.mapartcopyright.util.ModConfig.*;

public class ChangeConfigCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> mapartCopyrightNode = CommandManager
                .literal("map-art")
                .build();

        LiteralCommandNode<ServerCommandSource> configNode = CommandManager
                .literal("config")
                .requires(Permissions.require( MOD_ID + ".change-config"))
                .build();

        LiteralCommandNode<ServerCommandSource> copyrightNode = buildBooleanCommand("copyright", "copyright");
        LiteralCommandNode<ServerCommandSource> disableCopyNode = buildBooleanCommand("disableCopy", "disableCopy");
        LiteralCommandNode<ServerCommandSource> authorsCanCopyNode = buildBooleanCommand("authorsCanCopy", "authorsCanCopy");
        LiteralCommandNode<ServerCommandSource> cleanMapNode = buildBooleanCommand("cleanMap", "cleanMap");

        dispatcher.getRoot().addChild(mapartCopyrightNode);
        mapartCopyrightNode.addChild(configNode);
        configNode.addChild(copyrightNode);
        configNode.addChild(disableCopyNode);
        configNode.addChild(authorsCanCopyNode);
        configNode.addChild(cleanMapNode);
    }

    private static LiteralCommandNode<ServerCommandSource> buildBooleanCommand(String name, String configKey) {
        Style style = Style.EMPTY.withColor(Formatting.GRAY);

        return CommandManager
                .literal(name)
                .executes(context -> {
                    context.getSource().sendFeedback(() -> Text.literal("'" + name + "' is '" + getStringValue(configKey) + "'").setStyle(style), false);
                    return 1;
                })
                .then(CommandManager.argument("value", BoolArgumentType.bool())
                        .executes(context -> {
                            final boolean value = BoolArgumentType.getBool(context, "value");
                            setValue(configKey, value);
                            context.getSource().sendFeedback(() -> Text.literal("'" + name + "' set to '" + value + "'").setStyle(style), true);
                            return 1;
                        }))
                .build();
    }
}