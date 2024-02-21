package net.somyk.banmapcopy.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.*;
import static net.somyk.banmapcopy.BanMapCopy.MOD_ID;
import static net.somyk.banmapcopy.util.ModConfig.setValue;

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
        );
    }
}
