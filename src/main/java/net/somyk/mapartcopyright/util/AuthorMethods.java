package net.somyk.mapartcopyright.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

import static net.somyk.mapartcopyright.util.ModConfig.*;

public class AuthorMethods {

    public static boolean isAuthor(ItemStack itemStack, PlayerEntity playerEntity){
        NbtList authorsNBTList = itemStack.getOrCreateNbt().getList("authors", NbtElement.COMPOUND_TYPE);
        String playerName = playerEntity.getName().getString();
        boolean bl = false;

        for (int i = 0; i < authorsNBTList.size(); i++) {
            String authorName = authorsNBTList.getCompound(i).getString("author");
            bl = bl || (authorName != null && authorName.equals(playerName));
        }

        return bl;
    }

    public static boolean canCopy(ItemStack itemStack, PlayerEntity playerEntity){
        if(!getBooleanValue("disableCopy")){
            return true;
        }
        if(getBooleanValue("authorsCanCopy")) {
            return isAuthor(itemStack, playerEntity);
        } else
            return false;
    }

    public static void setAuthorNBT(ItemStack itemStack, PlayerEntity playerEntity){
        itemStack.getOrCreateNbt().put("authors", new NbtList());
        NbtList authorsNBTList = itemStack.getOrCreateNbt().getList("authors", NbtElement.COMPOUND_TYPE);

        NbtCompound author = new NbtCompound();
        author.putString("author", playerEntity.getEntityName());

        authorsNBTList.add(author);
    }

    public static void setAuthorDisplayLore(ItemStack itemStack){
        if(!getBooleanValue("displayAuthorsLore")) return;
        int maxAuthors = 5;

        Style LORE_STYLE = Style.EMPTY.withColor(Formatting.GRAY).withItalic(false);
        List<Text> lore = new ArrayList<>();
        NbtList authorsNBTList = itemStack.getOrCreateNbt().getList("authors", NbtElement.COMPOUND_TYPE);
        String authorName;
        String authorName1;

        int authorsCount = authorsNBTList.size();
        for (int i = 0; i < authorsCount; i += 2) {
            String line = "";
            if (i == 0) {
                authorName = authorsNBTList.getCompound(i).getString("author");
                if (i + 1 < authorsCount) {
                    lore.add(Text.translatable("book.byAuthor", authorName + ","));
                } else {
                    lore.add(Text.translatable("book.byAuthor", authorName));
                }
                i++;
            }
            if(i >= maxAuthors) break;
            if (i + 1 < authorsCount) {
                authorName = authorsNBTList.getCompound(i).getString("author");
                authorName1 = authorsNBTList.getCompound(i + 1).getString("author");
                line += authorName + ", " + authorName1;
                if (i + 2 < authorsCount){
                    if ( i + 2 != maxAuthors) line += ",";
                    else line += "...";
                }
            } else {
                authorName = authorsNBTList.getCompound(i).getString("author");
                line += authorName;
            }
            if(!line.isEmpty()) lore.add(Text.of(line));
        }

        NbtCompound display = itemStack.getOrCreateSubNbt("display");
        NbtList loreItems = new NbtList();

        for (Text tline : lore) {
            tline = tline.copy().setStyle(LORE_STYLE);
            loreItems.add(NbtString.of(Text.Serializer.toJson(tline)));
        }

        display.put("Lore", loreItems);
    }

    public static boolean addAuthorNBT(ItemStack itemStack, String playerName) {
        NbtList authorsNBTList = itemStack.getOrCreateNbt().getList("authors", NbtElement.COMPOUND_TYPE);
        NbtCompound author = new NbtCompound();
        author.putString("author", playerName);

        for (int i = 0; i < authorsNBTList.size(); i++) {
            String authorName = authorsNBTList.getCompound(i).getString("author");
            if (authorName!=null && authorName.equals(playerName)) {
                // There is already the author
                return false;
            }
        }
        authorsNBTList.add(author);

        return true;
    }
}
