package net.somyk.banmapcopy.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FilledMapItem.class)
public class FilledMapItemMixin {

    @Inject(method = "appendTooltip", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;",
            ordinal = 3, shift = At.Shift.AFTER))
    private void authorTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci){
        NbtList authorsNBTList = stack.getOrCreateNbt().getList("authors", NbtElement.COMPOUND_TYPE);
        if(authorsNBTList!=null) {
            String string = "";

            for (int i = 0; i < authorsNBTList.size(); i++) {
                String authorName = authorsNBTList.getCompound(i).getString("author");
                string = string.concat(authorName);
                if(i+1<authorsNBTList.size()){
                    if(i%2==0){
                        string = string.concat("\n");
                    }
                    string = string.concat(", ");
                }
            }
            tooltip.add(Text.translatable("book.byAuthor", string).formatted(Formatting.GRAY));
        }
    }
}
