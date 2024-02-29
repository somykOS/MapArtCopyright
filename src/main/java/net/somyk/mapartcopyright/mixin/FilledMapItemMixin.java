package net.somyk.mapartcopyright.mixin;


import net.minecraft.entity.Entity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import net.somyk.mapartcopyright.util.AuthorMethods;
import net.somyk.mapartcopyright.util.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(FilledMapItem.class)
public class FilledMapItemMixin {

    @Inject(method = "inventoryTick", at = @At("TAIL"))
    private void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci){
        if(ModConfig.getBooleanValue("displayAuthors") && stack.getSubNbt("display") == null
                && !Objects.equals(stack.getOrCreateNbt().getList("authors", NbtElement.COMPOUND_TYPE), new NbtList())){
            AuthorMethods.setLore(stack);
        } else if (!ModConfig.getBooleanValue("displayAuthors") && stack.getSubNbt("display") != null) {
            stack.removeSubNbt("display");
        }
    }
}
