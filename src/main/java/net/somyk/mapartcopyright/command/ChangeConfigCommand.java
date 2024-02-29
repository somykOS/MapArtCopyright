package net.somyk.mapartcopyright.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.*;
import static net.somyk.mapartcopyright.MapArtCopyright.MOD_ID;
import static net.somyk.mapartcopyright.util.ModConfig.setValue;

public class ChangeConfigCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, RegistrationEnvironment registrationEnvironment){
        dispatcher.register(literal(MOD_ID)
                .requires(source -> source.hasPermissionLevel(2))
                .then(literal("copyright")
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    final boolean value = BoolArgumentType.getBool(context,"value");
                                    setValue("copyright", value);
                                    context.getSource().sendFeedback(() -> Text.literal("'copyright' set to '" + value + "'"), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("disableCopy")
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    final boolean value = BoolArgumentType.getBool(context,"value");
                                    setValue("disableCopy", value);
                                    context.getSource().sendFeedback(() -> Text.literal("'disableCopy' set to '" + value + "'"), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("authorsCanCopy")
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    final boolean value = BoolArgumentType.getBool(context,"value");
                                    setValue("authorsCanCopy", value);
                                    context.getSource().sendFeedback(() -> Text.literal("'authorsCanCopy' set to '" + value + "'"), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("authorsCanAddAuthors")
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    final boolean value = BoolArgumentType.getBool(context,"value");
                                    setValue("authorsCanAddAuthors", value);
                                    context.getSource().sendFeedback(() -> Text.literal("'authorsCanAddAuthors' set to '" + value + "'"), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("cleanMap")
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    final boolean value = BoolArgumentType.getBool(context,"value");
                                    setValue("cleanMap", value);
                                    context.getSource().sendFeedback(() -> Text.literal("'cleanMap' set to '" + value + "'"), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("displayAuthors")
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    final boolean value = BoolArgumentType.getBool(context,"value");
                                    setValue("displayAuthors", value);
                                    context.getSource().sendFeedback(() -> Text.literal("'displayAuthors' set to '" + value + "'"), true);
                                    return 1;
                                })
                        )
                )
        );
    }
}
