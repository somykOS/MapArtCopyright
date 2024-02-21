package net.somyk.banmapcopy.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.somyk.banmapcopy.command.AddNewAuthorCommand;
import net.somyk.banmapcopy.command.ChangeConfigCommand;

public class ModCommandsRegister {
    public static void registerCommands(){
        CommandRegistrationCallback.EVENT.register(ChangeConfigCommand::register);
        CommandRegistrationCallback.EVENT.register(AddNewAuthorCommand::register);
    }
}
