package net.somyk.mapartcopyright.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.somyk.mapartcopyright.util.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.screen.CartographyTableScreenHandler$4")
public class CartographyTableScreenHandler$4Mixin {

    // Added water bucket to cartography table`s canInsert
    @ModifyReturnValue(method = "canInsert", at = @At("RETURN"))
    private boolean canInsert(boolean original, ItemStack stack) {
        return original || (ModConfig.getBooleanValue("cleanMap") && stack.isOf(Items.WATER_BUCKET));
    }
}