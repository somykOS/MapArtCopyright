package net.somyk.mapartcopyright.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.somyk.mapartcopyright.command.MapAuthorCommand;
import net.somyk.mapartcopyright.command.ChangeConfigCommand;

public class ModCommandsRegister {
    public static void registerCommands(){
        CommandRegistrationCallback.EVENT.register(ChangeConfigCommand::register);
        CommandRegistrationCallback.EVENT.register(MapAuthorCommand::register);
    }
}
