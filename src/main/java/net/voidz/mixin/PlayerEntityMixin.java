package net.voidz.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.voidz.init.DimensionInit;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "canPlaceOn", at = @At(value = "HEAD"), cancellable = true)
    public void canPlaceOnMixin(BlockPos pos, Direction facing, ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        PlayerEntity playerEntity = (PlayerEntity) (Object) this;
        if (playerEntity != null && !playerEntity.isCreative() && playerEntity.getWorld().getRegistryKey() == DimensionInit.VOID_WORLD) {
            info.setReturnValue(false);
        }
    }

}
