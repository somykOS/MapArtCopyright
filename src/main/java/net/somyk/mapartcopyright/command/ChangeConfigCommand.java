package net.somyk.mapartcopyright.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Function;

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

        LiteralCommandNode<ServerCommandSource> maxPlayerLoreNode = buildIntegerCommand(maxPlayerLore);
        LiteralCommandNode<ServerCommandSource> disableCopyNode = buildBooleanCommand(disableCopy);
        LiteralCommandNode<ServerCommandSource> authorsCanCopyNode = buildBooleanCommand(authorsCopy);
        LiteralCommandNode<ServerCommandSource> publicDomainNode = buildBooleanCommand(publicDomain);
        LiteralCommandNode<ServerCommandSource> cleanMapNode = buildBooleanCommand(cleanMap);

        dispatcher.getRoot().addChild(mapartCopyrightNode);
        mapartCopyrightNode.addChild(configNode);
        configNode.addChild(maxPlayerLoreNode);
        configNode.addChild(disableCopyNode);
        configNode.addChild(authorsCanCopyNode);
        configNode.addChild(publicDomainNode);
        configNode.addChild(cleanMapNode);
    }

    private static final Style style = Style.EMPTY.withColor(Formatting.GRAY);

    private static <T> LiteralCommandNode<ServerCommandSource> buildGenericCommand(
            String configKey,
            ArgumentType<T> argumentType,
            Function<CommandContext<ServerCommandSource>, T> valueGetter) {

        return CommandManager
                .literal(configKey)
                .executes(context -> {
                    context.getSource().sendFeedback(
                            () -> Text.literal(getStringValue(messageConfigValue, LANG_CONFIG)
                                            .formatted(configKey, getStringValue(configKey, CONFIG)))
                                    .setStyle(style),
                            false
                    );
                    return 1;
                })
                .then(CommandManager.argument("value", argumentType)
                        .executes(context -> {
                            final T value = valueGetter.apply(context);
                            setValue(configKey, value);
                            context.getSource().sendFeedback(
                                    () -> Text.literal(getStringValue(messageConfigValueUpdated, LANG_CONFIG)
                                                    .formatted(configKey, value))
                                            .setStyle(style),
                                    true
                            );
                            return 1;
                        }))
                .build();
    }

    private static LiteralCommandNode<ServerCommandSource> buildBooleanCommand(String configKey) {
        return buildGenericCommand(
                configKey,
                BoolArgumentType.bool(),
                context -> BoolArgumentType.getBool(context, "value")
        );
    }

    private static LiteralCommandNode<ServerCommandSource> buildIntegerCommand(String configKey) {
        return buildGenericCommand(
                configKey,
                IntegerArgumentType.integer(),
                context -> IntegerArgumentType.getInteger(context, "value")
        );
    }
}