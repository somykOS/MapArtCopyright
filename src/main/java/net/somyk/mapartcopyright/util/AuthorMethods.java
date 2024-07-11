package net.somyk.mapartcopyright.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.component.type.*;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

import static net.somyk.mapartcopyright.util.ModConfig.getBooleanValue;

public class AuthorMethods {

    public static boolean isAuthor(ItemStack itemStack, PlayerEntity playerEntity){
        NbtCompound tag = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        NbtList authors = (NbtList) tag.get("authors");
        if (authors == null) return false;
        return authors.contains(NbtString.of(playerEntity.getName().getString()));
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

    public static void createAuthorNBT(ItemStack itemStack, PlayerEntity playerEntity){
        NbtCompound tag = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();

        NbtList authors = new NbtList();
        authors.add(NbtString.of(playerEntity.getName().getString()));

        tag.put("authors", authors);
        itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
    }

    public static boolean modifyAuthorNBT(ItemStack itemStack, String playerName, int operation) {
        NbtCompound tag = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        NbtList authors = (NbtList) tag.get("authors");
        if (authors == null) authors = new NbtList();
        NbtString author = NbtString.of(playerName);

        if (operation == 1 && !authors.contains(author)) {
            authors.add(author);
            tag.put("authors", authors);
            itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
            return true;
        } else if (operation == 0 && authors.contains(author)) {
            authors.remove(author);
            if (!authors.isEmpty()) {
                tag.put("authors", authors);
            } else tag.remove("authors");

            itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
            return true;
        }
        return false;
    }

    public static void setAuthorLore(ItemStack itemStack){
        if(!getBooleanValue("displayAuthorsLore")) return;
        int maxAuthors = 5;

        Style LORE_STYLE = Style.EMPTY.withColor(Formatting.GRAY).withItalic(false);
        List<Text> loreLines = new ArrayList<>();

        NbtCompound tag = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        NbtList authors = (NbtList) tag.get("authors");

        String authorName;
        String authorName1;

        int authorsCount = authors != null ? authors.size() : 0;
        for (int i = 0; i < authorsCount; i += 2) {
            String line = "";
            if (i == 0) {
                authorName = authors.getString(i);
                if (i + 1 < authorsCount) {
                    loreLines.add(Text.translatable("book.byAuthor", authorName + ",").setStyle(LORE_STYLE));
                } else {
                    loreLines.add(Text.translatable("book.byAuthor", authorName).setStyle(LORE_STYLE));
                }
                i++;
            }
            if (i >= maxAuthors) break;
            if (i + 1 < authorsCount) {
                authorName = authors.getString(i);
                authorName1 = authors.getString(i+1);
                line += authorName + ", " + authorName1;
                if (i + 2 < authorsCount){
                    if ( i + 2 != maxAuthors) line += ",";
                    else line += "...";
                }
            } else {
                authorName = authors.getString(i);
                line += authorName;
            }
            if (!line.isEmpty()) loreLines.add(Text.literal(line).setStyle(LORE_STYLE));
        }

        LoreComponent lore = new LoreComponent(loreLines);
        itemStack.set(DataComponentTypes.LORE, lore);
    }
}
