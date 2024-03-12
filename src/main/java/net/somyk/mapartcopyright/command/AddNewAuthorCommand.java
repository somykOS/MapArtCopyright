package net.somyk.mapartcopyright.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import static net.minecraft.server.command.CommandManager.*;
import static net.somyk.mapartcopyright.util.AuthorMethods.*;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.somyk.mapartcopyright.util.ModConfig;

import java.util.regex.Pattern;

public class AddNewAuthorCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, RegistrationEnvironment registrationEnvironment){
        dispatcher.register(literal("newAuthor")
                .requires(source -> ModConfig.getBooleanValue("authorsCanAddAuthors"))
                .then(argument("nickname", StringArgumentType.greedyString())
                        .executes(context -> run(context, StringArgumentType.getString(context,"nickname")))
                )
        );
    }

    public static int run(CommandContext<ServerCommandSource> context, String playerName){
        Style style = Style.EMPTY.withColor(Formatting.GRAY).withItalic(true);

        PlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        ItemStack itemStack = player.getMainHandStack();

        if(!itemStack.isOf(Items.FILLED_MAP)){
            context.getSource().sendFeedback(() -> Text.literal("You should have filled map in main hand").setStyle(style), false);
            return -1;
        } else if (!isAuthor(itemStack, player)) {
            context.getSource().sendFeedback(() -> Text.literal("You`re not one of the authors, who are allowed to modify this map").setStyle(style), false);
            return -1;
        }

        Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);

        if (p.matcher(playerName).find()) {
            context.getSource().sendFeedback(() -> Text.literal("Player name should not have any special character").setStyle(style), false);
            return -1;
        } else if(!addAuthorNBT(itemStack, playerName)){
            context.getSource().sendFeedback(() -> Text.literal("There is already the author").setStyle(style), false);
            return -1;
        }

        setAuthorDisplayLore(itemStack);

        return 1;
    }
}