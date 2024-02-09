package net.somyk.banmapcopy.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/screen/CartographyTableScreenHandler$5")
public class CartographyTableScreenHandler$5Mixin {

    @WrapOperation(method = "onTakeItem", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/screen/slot/Slot;takeStack(I)Lnet/minecraft/item/ItemStack;", ordinal = 1))
    private ItemStack setEmptyBucket(Slot instance, int amount, Operation<ItemStack> original){
        if(instance.getStack().isOf(Items.WATER_BUCKET)) {
            instance.setStack(Items.BUCKET.getDefaultStack());
            return null;
        } else {
            return original.call(instance, amount);
        }
    }
}
