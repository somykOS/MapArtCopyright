package net.somyk.mapartcopyright.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.component.type.*;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.somyk.mapartcopyright.util.ModConfig.*;

public class AuthorMethods {
    public static final String AUTHORS_KEY = "authors";
    public static final String PUBLIC_KEY = "public";
    private static final Style TOOLTIP_STYLE = Style.EMPTY.withColor(Formatting.GRAY).withItalic(false);

    public static boolean isAuthor(ItemStack itemStack, PlayerEntity playerEntity) {
        return getAuthors(itemStack)
                .map(authors -> authors.contains(NbtString.of(playerEntity.getName().getString())))
                .orElse(false);
    }

    public static boolean isMainAuthor(ItemStack itemStack, PlayerEntity playerEntity) {
        return getAuthors(itemStack)
                .map(authors -> !authors.isEmpty() && authors.getString(0).equals(playerEntity.getName().getString()))
                .orElse(false);
    }

    public static boolean canCopy(ItemStack itemStack, PlayerEntity playerEntity) {
        if (!getBooleanValue(disableCopy)) return true;

        NbtCompound tag = getCustomData(itemStack);
        if (tag.getBoolean(PUBLIC_KEY)) return true;

        return getBooleanValue(authorsCopy) && playerEntity != null && isAuthor(itemStack, playerEntity);
    }

    public static boolean toPublicDomain(ItemStack itemStack) {
        NbtCompound tag = getCustomData(itemStack);
        if(tag.contains(PUBLIC_KEY)) {
            tag.putBoolean(PUBLIC_KEY, !tag.getBoolean(PUBLIC_KEY));
        } else {
            tag.putBoolean(PUBLIC_KEY, true);
        }
        itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
        addLore(itemStack);
        return tag.getBoolean(PUBLIC_KEY);
    }

    public static void createAuthorNBT(ItemStack itemStack, PlayerEntity playerEntity) {
        NbtCompound tag = getCustomData(itemStack);
        NbtList authors = new NbtList();
        authors.add(NbtString.of(playerEntity.getName().getString()));
        tag.put(AUTHORS_KEY, authors);
        itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
        addLore(itemStack);
    }

    public static boolean modifyAuthorNBT(ItemStack itemStack, String playerName, int operation) {
        NbtCompound tag = getCustomData(itemStack);
        NbtList authors = tag.getList(AUTHORS_KEY, NbtElement.STRING_TYPE);

        int index = findAuthorIndex(authors, playerName);

        boolean modified = false;
        if (operation == 1 && index == -1) {
            authors.add(NbtString.of(playerName));
            modified = true;
        } else if (operation == 0 && index != -1) {
            authors.remove(index);
            modified = true;
        }

        if (modified) {
            updateAuthorsTag(itemStack, tag, authors);
            addLore(itemStack);
        }

        return modified;
    }

    public static void addLore(ItemStack itemStack) {
        getAuthors(itemStack).ifPresent(authors -> {
            if (!authors.isEmpty()) {
                addAuthorsToLore(itemStack, authors);
            } else itemStack.remove(DataComponentTypes.LORE);
        });

        NbtCompound tag = getCustomData(itemStack);
        if (tag.getBoolean(PUBLIC_KEY)) {
            //LoreComponent lore = (LoreComponent) itemStack.getOrDefault(DataComponentTypes.LORE, NbtComponent.DEFAULT);
            LoreComponent lore = itemStack.getComponents().get(DataComponentTypes.LORE);
            if(lore == null) lore = new LoreComponent(new ArrayList<>());
            lore.lines().add(Text.literal(getStringValue(lorePublicDomain, LANG_CONFIG)).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY).withItalic(false)));
            itemStack.set(DataComponentTypes.LORE, lore);
        } else {
            LoreComponent lore = itemStack.getComponents().get(DataComponentTypes.LORE);
            if (lore != null) {
                lore.lines().remove(Text.literal(getStringValue(lorePublicDomain, LANG_CONFIG)).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY).withItalic(false)));
                itemStack.set(DataComponentTypes.LORE, lore);
            }
        }
    }

    private static Optional<NbtList> getAuthors(ItemStack itemStack) {
        NbtCompound tag = getCustomData(itemStack);
        return Optional.ofNullable(tag.getList(AUTHORS_KEY, NbtElement.STRING_TYPE));
    }

    public static NbtCompound getCustomData(ItemStack itemStack) {
        return itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
    }

    private static void addAuthorsToLore(ItemStack itemStack, NbtList authors) {
        int maxPlayers = getIntValue(maxPlayerLore);
        int authorCount = Math.min(authors.size(), maxPlayers);
        List<Text> loreLines = new ArrayList<>();
        loreLines.add(Text.translatable("book.byAuthor", authors.getString(0) + (authorCount > 1 ? "," : "")).setStyle(TOOLTIP_STYLE));
        StringBuilder line;

        for (int i = 1; i < authorCount; i += 2) {
            line = new StringBuilder(authors.getString(i));

            if (i + 1 < authorCount && i + 1 < authors.size()) {
                line.append(", ").append(authors.getString(i + 1));
                if (i + 2 < authorCount && i + 2 < authors.size()) line.append(",");
                else if (i + 2 >= authorCount && i + 2 < authors.size()) line.append("...");
            } else if (i + 1 >= authorCount && i + 1 < authors.size()) line.append("...");

            loreLines.add(Text.literal(line.toString()).setStyle(TOOLTIP_STYLE));
        }

        itemStack.set(DataComponentTypes.LORE, new LoreComponent(loreLines));
    }

    private static int findAuthorIndex(NbtList authors, String playerName) {
        for (int i = 0; i < authors.size(); i++) {
            if (authors.getString(i).equalsIgnoreCase(playerName)) {
                return i;
            }
        }
        return -1;
    }

    private static void updateAuthorsTag(ItemStack itemStack, NbtCompound tag, NbtList authors) {
        if (authors.isEmpty()) {
            tag.remove(AUTHORS_KEY);
        } else {
            tag.put(AUTHORS_KEY, authors);
        }
        itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
    }
}