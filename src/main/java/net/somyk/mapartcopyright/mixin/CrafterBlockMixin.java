package net.somyk.mapartcopyright.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.CrafterBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.somyk.mapartcopyright.util.AuthorMethods.PUBLIC_KEY;
import static net.somyk.mapartcopyright.util.AuthorMethods.getCustomData;
import static net.somyk.mapartcopyright.util.ModConfig.disableCopy;
import static net.somyk.mapartcopyright.util.ModConfig.getBooleanValue;

@Mixin(CrafterBlock.class)
public class CrafterBlockMixin {

    @WrapOperation(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    private boolean craft(ItemStack instance, Operation<Boolean> original){
        if (original.call(instance)) {
            return true;
        }

        if (instance.isOf(Items.FILLED_MAP) && getBooleanValue(disableCopy)) {
            NbtCompound tag = getCustomData(instance);
            return !tag.getBoolean(PUBLIC_KEY);
        }

        return false;
    }
}
