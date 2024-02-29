package net.somyk.mapartcopyright.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.*;
import static net.somyk.mapartcopyright.MapArtCopyright.MOD_ID;
import static net.somyk.mapartcopyright.util.ModConfig.*;

public class ChangeConfigCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, RegistrationEnvironment registrationEnvironment){
        Style style = Style.EMPTY.withColor(Formatting.GRAY).withItalic(true);

        dispatcher.register(literal(MOD_ID)
                .requires(Permissions.require("mapartcopyright.configparameters"))
                .then(literal("copyright")
                        .executes(context -> {
                            context.getSource().sendFeedback(() -> Text.literal("'copyright' is '" + getStringValue("copyright") + "'").setStyle(style), false);
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    final boolean value = BoolArgumentType.getBool(context,"value");
                                    setValue("copyright", value);
                                    context.getSource().sendFeedback(() -> Text.literal("'copyright' set to '" + value + "'").setStyle(style), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("disableCopy")
                        .executes(context -> {
                            context.getSource().sendFeedback(() -> Text.literal("'disableCopy' is '" + getStringValue("disableCopy") + "'").setStyle(style), false);
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    final boolean value = BoolArgumentType.getBool(context,"value");
                                    setValue("disableCopy", value);
                                    context.getSource().sendFeedback(() -> Text.literal("'disableCopy' set to '" + value + "'").setStyle(style), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("authorsCanCopy")
                        .executes(context -> {
                            context.getSource().sendFeedback(() -> Text.literal("'authorsCanCopy' is '" + getStringValue("authorsCanCopy") + "'").setStyle(style), false);
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    final boolean value = BoolArgumentType.getBool(context,"value");
                                    setValue("authorsCanCopy", value);
                                    context.getSource().sendFeedback(() -> Text.literal("'authorsCanCopy' set to '" + value + "'").setStyle(style), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("authorsCanAddAuthors")
                        .executes(context -> {
                            context.getSource().sendFeedback(() -> Text.literal("'authorsCanAddAuthors' is '" + getStringValue("authorsCanAddAuthors") + "'").setStyle(style), false);
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    final boolean value = BoolArgumentType.getBool(context,"value");
                                    setValue("authorsCanAddAuthors", value);
                                    context.getSource().sendFeedback(() -> Text.literal("'authorsCanAddAuthors' set to '" + value + "'").setStyle(style), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("cleanMap")
                        .executes(context -> {
                            context.getSource().sendFeedback(() -> Text.literal("'cleanMap' is '" + getStringValue("cleanMap") + "'").setStyle(style), false);
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    final boolean value = BoolArgumentType.getBool(context,"value");
                                    setValue("cleanMap", value);
                                    context.getSource().sendFeedback(() -> Text.literal("'cleanMap' set to '" + value + "'").setStyle(style), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("displayAuthors")
                        .executes(context -> {
                            context.getSource().sendFeedback(() -> Text.literal("'displayAuthors' is '" + getStringValue("displayAuthors") + "'").setStyle(style), false);
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    final boolean value = BoolArgumentType.getBool(context,"value");
                                    setValue("displayAuthors", value);
                                    context.getSource().sendFeedback(() -> Text.literal("'displayAuthors' set to '" + value + "'").setStyle(style), true);
                                    return 1;
                                })
                        )
                )
        );
    }
}
