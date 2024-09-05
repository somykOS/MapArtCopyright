package net.somyk.mapartcopyright.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.World;
import net.somyk.mapartcopyright.util.AuthorMethods;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {

    // Checking if a player can copy a map
    @ModifyExpressionValue(method = "updateResult", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/recipe/CraftingRecipe;craft(Lnet/minecraft/recipe/input/RecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack playerCanCopyCheck(ItemStack original, ScreenHandler handler, World world, PlayerEntity player, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory, @Nullable RecipeEntry<CraftingRecipe> recipe){
        if(original.isOf(Items.FILLED_MAP))
            if(!AuthorMethods.canCopy(original, player)) {
                return ItemStack.EMPTY;
            }
        return original;
    }
}
