package net.somyk.banmapcopy.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class AuthorCheck {

    public static boolean authorCheck(PlayerEntity playerEntity, ItemStack itemStack){
        NbtList authorsNBTList = itemStack.getOrCreateNbt().getList("authors", NbtElement.COMPOUND_TYPE);
        String playerName = playerEntity.getName().getString();
        boolean canCopy = false;

        for(int i = 0; i < authorsNBTList.size(); i++){
            String authorName = authorsNBTList.getCompound(i).getString("author");
            canCopy = canCopy || ( authorName != null && authorName.equals(playerName) );
        }

        return canCopy;
    }
}
