package net.somyk.banmapcopy.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import static net.minecraft.server.command.CommandManager.*;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.somyk.banmapcopy.util.AuthorCheck;

public class AddNewAuthorCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, RegistrationEnvironment registrationEnvironment){
        dispatcher.register(literal("newAuthor")
                .then(argument("nickname", StringArgumentType.greedyString())
                        .executes(context -> run(context, StringArgumentType.getString(context,"nickname")))
                )
        );
    }

    public static int run(CommandContext<ServerCommandSource> context, String nickname){
        PlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        ItemStack itemStack = player.getMainHandStack();

        if(!itemStack.isOf(Items.FILLED_MAP)){
            context.getSource().sendFeedback(() -> Text.literal("You should have filled map item in main hand"), false);
            return -1;
        } else if (!AuthorCheck.authorCheck(player, itemStack)) {
            context.getSource().sendFeedback(() -> Text.literal("You`re not one of authors to be allowed to modify this map"), false);
            return -1;
        }

        NbtList authorsNBTList = itemStack.getOrCreateNbt().getList("authors", NbtElement.COMPOUND_TYPE);

        for(int i = 0; i < authorsNBTList.size(); i++){
            String authorName = authorsNBTList.getCompound(i).getString("author");
            if(authorName != null && authorName.equals(nickname)){
                context.getSource().sendFeedback(() -> Text.literal("There is the author"), false);
                return -1;
            }
        }
        NbtCompound newAuthor = new NbtCompound();
        newAuthor.putString("author", nickname);
        authorsNBTList.add(newAuthor);

        return 1;
    }
}
