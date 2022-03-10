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
        if (playerEntity != null && !playerEntity.isCreative() && playerEntity.world.getRegistryKey() == DimensionInit.VOID_WORLD)
            info.setReturnValue(false);
    }

    @Inject(method = "checkFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"), cancellable = true)
    public void checkFallFlyingMixin(CallbackInfoReturnable<Boolean> info) {
        PlayerEntity playerEntity = (PlayerEntity) (Object) this;
        if (playerEntity != null && !playerEntity.isCreative() && playerEntity.world.getRegistryKey() == DimensionInit.VOID_WORLD)
            info.setReturnValue(false);
    }

}
