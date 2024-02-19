package net.somyk.banmapcopy.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.screen.CartographyTableScreenHandler$4")
public class CartographyTableScreenHandler$4Mixin {

    @Unique
    private boolean cleanMap = true;

    // Added water bucket to cartography table
    @ModifyReturnValue(method = "canInsert", at = @At("RETURN"))
    private boolean canInsert(boolean original, ItemStack stack) {
        return original || (cleanMap && stack.isOf(Items.WATER_BUCKET));
    }
}