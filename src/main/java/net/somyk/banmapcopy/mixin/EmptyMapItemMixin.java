package net.somyk.banmapcopy.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EmptyMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EmptyMapItem.class)
public class EmptyMapItemMixin {

    @ModifyExpressionValue(method = "use", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/FilledMapItem;createMap(Lnet/minecraft/world/World;IIBZZ)Lnet/minecraft/item/ItemStack;"))
    public ItemStack addAuthorNBT(ItemStack original, World world, PlayerEntity user, Hand hand){
        original.getOrCreateNbt().putString("author", user.getEntityName());
        return original;
    }

}
