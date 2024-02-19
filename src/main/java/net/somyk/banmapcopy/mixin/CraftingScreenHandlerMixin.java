package net.somyk.banmapcopy.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.World;
import net.somyk.banmapcopy.util.AuthorCheck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {

    @Unique
    private static PlayerEntity playerEntity;

    // Extracting PlayerEntity
    @Inject(method = "updateResult", at= @At("HEAD"))
    private static void getPlayerEntity(ScreenHandler handler, World world, PlayerEntity player, RecipeInputInventory craftingInventory,
                                        CraftingResultInventory resultInventory, CallbackInfo ci){
        playerEntity = player;
    }

    // Checking author while copying map
    @ModifyExpressionValue(method = "updateResult", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/recipe/CraftingRecipe;craft(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack checkAuthorNBT(ItemStack original){
        if(original.isOf(Items.FILLED_MAP) && !AuthorCheck.authorCheck(playerEntity, original)) {
            return ItemStack.EMPTY;
        }
        return original;
    }
}
