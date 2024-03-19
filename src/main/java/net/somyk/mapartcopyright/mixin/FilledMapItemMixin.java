package net.somyk.mapartcopyright.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;
import net.somyk.mapartcopyright.util.AuthorMethods;
import net.somyk.mapartcopyright.util.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FilledMapItem.class)
public class FilledMapItemMixin {

    // Checks if map has lore and config value 'displayAuthorsLore' is true or false
    @Inject(method = "inventoryTick", at = @At("TAIL"))
    private void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci){
        boolean shouldDisplayAuthorsLore = ModConfig.getBooleanValue("displayAuthorsLore");
        boolean stackHasAuthors = !stack.getOrCreateNbt().getList("authors", NbtElement.COMPOUND_TYPE).isEmpty();
        boolean stackHasDisplay = stack.getSubNbt("display") != null;

        if (shouldDisplayAuthorsLore && !stackHasDisplay && stackHasAuthors) {
            AuthorMethods.setAuthorDisplayLore(stack);
        } else if (stackHasDisplay && (!shouldDisplayAuthorsLore || !stackHasAuthors)) {
            stack.removeSubNbt("display");
        }
    }
}
