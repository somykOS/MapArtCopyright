package net.somyk.mapartcopyright.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(targets = "net/minecraft/screen/CartographyTableScreenHandler$5")
public class CartographyTableScreenHandler$5Mixin {

    // Gets empty bucket after a player takes an empty map
    @WrapOperation(method = "onTakeItem", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/screen/slot/Slot;takeStack(I)Lnet/minecraft/item/ItemStack;", ordinal = 1))
    private ItemStack setEmptyContainer(Slot instance, int amount, Operation<ItemStack> original){
        if(instance.getStack().isOf(Items.WATER_BUCKET)) {
            instance.setStack(Items.BUCKET.getDefaultStack());
            return null;
        } else if (Objects.equals(instance.getStack().getComponents(), PotionContentsComponent.createStack(Items.POTION, Potions.WATER).getComponents())){
            instance.setStack(Items.GLASS_BOTTLE.getDefaultStack());
            return null;
        } else {
            return original.call(instance, amount);
        }
    }
}
