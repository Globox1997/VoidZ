package net.voidz.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import net.voidz.init.DimensionInit;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    private void placeMixin(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> info) {
        if (DimensionInit.isRestricted(context.getPlayer(), context.getWorld())) {
            info.setReturnValue(ActionResult.FAIL);
        }
    }

}
