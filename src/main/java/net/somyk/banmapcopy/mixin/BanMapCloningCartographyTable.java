package net.somyk.banmapcopy.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.screen.CartographyTableScreenHandler$4")
public class BanMapCloningCartographyTable {
	@Inject(method = "canInsert", at = @At("RETURN"), cancellable = true)
	public void canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		boolean bl = stack.isOf(Items.PAPER) || stack.isOf(Items.GLASS_PANE);
		cir.setReturnValue(bl);
	}
}
