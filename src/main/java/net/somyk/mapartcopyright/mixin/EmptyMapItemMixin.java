package net.somyk.mapartcopyright.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EmptyMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.somyk.mapartcopyright.util.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.somyk.mapartcopyright.util.AuthorMethods.*;

@Mixin(EmptyMapItem.class)
public class EmptyMapItemMixin {

    // Adding 'authors' NBT and Lore while creating a filled map item
    @ModifyExpressionValue(method = "use", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/FilledMapItem;createMap(Lnet/minecraft/world/World;IIBZZ)Lnet/minecraft/item/ItemStack;"))
    private ItemStack addAuthorsNBT(ItemStack original, World world, PlayerEntity player, Hand hand){
        if (!ModConfig.getBooleanValue("copyright")) return original;

        createAuthorNBT(original, player);
        setAuthorLore(original);

        return original;
    }

}
