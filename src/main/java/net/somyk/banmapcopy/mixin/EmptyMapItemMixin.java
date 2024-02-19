package net.somyk.banmapcopy.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EmptyMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EmptyMapItem.class)
public class EmptyMapItemMixin {

    // Adding player name as author NBT
    @ModifyExpressionValue(method = "use", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/item/FilledMapItem;createMap(Lnet/minecraft/world/World;IIBZZ)Lnet/minecraft/item/ItemStack;"))
    public ItemStack addAuthorNBT(ItemStack original, World world, PlayerEntity user, Hand hand){
        original.getOrCreateNbt().put("authors", new NbtList());

        NbtList authorsNBTList = original.getOrCreateNbt().getList("authors", NbtElement.COMPOUND_TYPE);

        NbtCompound author = new NbtCompound();
        author.putString("author", user.getEntityName());

        authorsNBTList.add(author);
        return original;
    }

}
