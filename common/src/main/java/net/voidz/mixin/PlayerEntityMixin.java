package net.voidz.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.voidz.init.DimensionInit;

@Mixin(Player.class)
public class PlayerEntityMixin {

    @Inject(method = "mayUseItemAt", at = @At(value = "HEAD"), cancellable = true)
    public void canPlaceOnMixin(BlockPos pos, Direction facing, ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        Player playerEntity = (Player) (Object) this;
        if (DimensionInit.isRestricted(playerEntity, playerEntity.level())) {
            info.setReturnValue(false);
        }
    }

}
