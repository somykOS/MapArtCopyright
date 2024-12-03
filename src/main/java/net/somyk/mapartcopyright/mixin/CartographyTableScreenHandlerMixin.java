package net.somyk.mapartcopyright.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.CartographyTableScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.somyk.mapartcopyright.util.AuthorMethods;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CartographyTableScreenHandler.class)
public class CartographyTableScreenHandlerMixin {

    @Unique
    private PlayerEntity playerEntity;

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At("TAIL"))
    private void getPlayerEntity(int syncId, PlayerInventory inventory, ScreenHandlerContext context, CallbackInfo ci) {
        playerEntity = inventory.player;
    }

    @ModifyExpressionValue(method = "method_17382", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;copyWithCount(I)Lnet/minecraft/item/ItemStack;", ordinal = 2))
    private ItemStack playerCanCopyCheck(ItemStack original) {
        if (AuthorMethods.canCopy(original, playerEntity)) {
            return original.copyWithCount(2);
        }
        return ItemStack.EMPTY;
    }

    @WrapOperation(method = "method_17382", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/inventory/CraftingResultInventory;removeStack(I)Lnet/minecraft/item/ItemStack;"))
    private ItemStack checkIfWaterBucket(CraftingResultInventory instance, int slot, Operation<ItemStack> original, ItemStack map, ItemStack item, ItemStack oldResult) {
        if (item.isOf(Items.WATER_BUCKET)) {
            instance.setStack(slot, Items.MAP.getDefaultStack());
            return null;
        } else {
            return original.call(instance, slot);
        }
    }
}