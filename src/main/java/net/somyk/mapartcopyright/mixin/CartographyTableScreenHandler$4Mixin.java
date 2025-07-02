package net.somyk.mapartcopyright.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

import static net.somyk.mapartcopyright.util.ModConfig.*;

@Mixin(targets = "net.minecraft.screen.CartographyTableScreenHandler$4")
public class CartographyTableScreenHandler$4Mixin {

    // Added water bucket to cartography table`s canInsert
    @ModifyReturnValue(method = "canInsert", at = @At("RETURN"))
    private boolean canInsert(boolean original, ItemStack stack) {
        return original || (getBooleanValue(cleanMap) && (stack.isOf(Items.WATER_BUCKET) || Objects.equals(stack.getComponents(), PotionContentsComponent.createStack(Items.POTION, Potions.WATER).getComponents())));
    }
}