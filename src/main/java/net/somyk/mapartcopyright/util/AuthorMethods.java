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

import static net.somyk.mapartcopyright.util.ModConfig.getBooleanValue;

public class AuthorMethods {
    public static final String AUTHORS_KEY = "authors";
    private static final Style TOOLTIP_STYLE = Style.EMPTY.withColor(Formatting.GRAY).withItalic(false);
    private static final int MAX_AUTHORS_DISPLAYED = 5;

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
        return !getBooleanValue("disableCopy") || (getBooleanValue("authorsCanCopy") && isAuthor(itemStack, playerEntity));
    }

    public static void createAuthorNBT(ItemStack itemStack, PlayerEntity playerEntity) {
        NbtCompound tag = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        NbtList authors = new NbtList();
        authors.add(NbtString.of(playerEntity.getName().getString()));
        tag.put(AUTHORS_KEY, authors);
        itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
        addLore(itemStack);
    }

    public static boolean modifyAuthorNBT(ItemStack itemStack, String playerName, int operation) {
        NbtCompound tag = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        NbtList authors = tag.getList(AUTHORS_KEY, NbtElement.STRING_TYPE);
        NbtString author = NbtString.of(playerName);

        boolean modified = false;
        if (operation == 1 && !authors.contains(author)) {
            authors.add(author);
            modified = true;
        } else if (operation == 0 && authors.remove(author)) {
            modified = true;
        }

        if (modified) {
            if (authors.isEmpty()) {
                tag.remove(AUTHORS_KEY);
            } else {
                tag.put(AUTHORS_KEY, authors);
            }
            itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
            addLore(itemStack);
        }

        return modified;
    }

    private static Optional<NbtList> getAuthors(ItemStack itemStack) {
        NbtCompound tag = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        return Optional.ofNullable(tag.getList(AUTHORS_KEY, NbtElement.STRING_TYPE));
    }

    public static void addLore(ItemStack itemStack) {
        getAuthors(itemStack).ifPresent(authors -> {
            if (!authors.isEmpty()) {
                addAuthorsToLore(itemStack, authors);
            } else itemStack.remove(DataComponentTypes.LORE);
        });
    }

    private static void addAuthorsToLore(ItemStack itemStack, NbtList authors) {
        List<Text> loreLines = new ArrayList<>();
        String firstAuthor = authors.getString(0);
        loreLines.add(Text.translatable("book.byAuthor", firstAuthor + (authors.size() > 1 ? "," : "")).setStyle(TOOLTIP_STYLE));

        StringBuilder line = new StringBuilder();
        for (int i = 1; i < Math.min(authors.size(), MAX_AUTHORS_DISPLAYED); i++) {
            if (!line.isEmpty()) {
                line.append(", ");
            }
            line.append(authors.getString(i));
            if (i % 2 == 0 || i == Math.min(authors.size(), MAX_AUTHORS_DISPLAYED) - 1) {
                loreLines.add(Text.literal(line.toString()).setStyle(TOOLTIP_STYLE));
                line.setLength(0);
            }
        }

        if (authors.size() > MAX_AUTHORS_DISPLAYED) {
            loreLines.add(Text.literal("...").setStyle(TOOLTIP_STYLE));
        }

        itemStack.set(DataComponentTypes.LORE, new LoreComponent(loreLines));
    }
}