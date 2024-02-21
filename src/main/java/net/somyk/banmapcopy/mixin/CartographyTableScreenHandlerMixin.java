package net.somyk.banmapcopy.mixin;

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
import net.somyk.banmapcopy.util.AuthorCheck;
import net.somyk.banmapcopy.util.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CartographyTableScreenHandler.class)
public class CartographyTableScreenHandlerMixin {

    @Unique
    private PlayerEntity playerEntity;

    // Extracting PlayerEntity
    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at= @At("TAIL"))
    private void getPlayerEntity(int syncId, PlayerInventory inventory, ScreenHandlerContext context, CallbackInfo ci){
        this.playerEntity = inventory.player;
    }

    // Checking author while copying map
    @ModifyExpressionValue(method = "method_17382", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;copyWithCount(I)Lnet/minecraft/item/ItemStack;", ordinal = 2))
    private ItemStack checkAuthorNBT(ItemStack original){
        if(AuthorCheck.authorCheck(playerEntity, original) && ModConfig.getValue("authorsCanCopy")) {
            return original.copyWithCount(2);
        }
        return ItemStack.EMPTY;
    }

    // Gets empty map if second slot is water bucket
    @WrapOperation(method = "method_17382", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/inventory/CraftingResultInventory;removeStack(I)Lnet/minecraft/item/ItemStack;"))
    private ItemStack checkIfWaterBucket(CraftingResultInventory instance, int slot, Operation<ItemStack> original, ItemStack map, ItemStack item, ItemStack oldResult){
        if(item.isOf(Items.WATER_BUCKET)) {
            instance.setStack(slot,Items.MAP.getDefaultStack());
            return null;
        } else {
            return original.call(instance, slot);
        }
    }
}