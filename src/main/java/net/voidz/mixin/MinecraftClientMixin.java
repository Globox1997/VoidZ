package net.voidz.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.sound.MusicType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.Hand;
import net.voidz.init.DimensionInit;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    @Nullable
    public ClientPlayerInteractionManager interactionManager;

    @Inject(method = "getMusicType", at = @At(value = "HEAD"), cancellable = true)
    public void getMusicTypeMixin(CallbackInfoReturnable<MusicSound> info) {
        if (this.player != null && this.player.world.getRegistryKey() == DimensionInit.VOID_WORLD) {
            info.setReturnValue(MusicType.DRAGON);
        }

    }

    @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getCount()I"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void doItemUseMixin(CallbackInfo info, Hand[] var1, int var2, int var3, Hand hand, ItemStack itemStack) {
        if (player != null && !player.isCreative() && itemStack.getItem() instanceof BlockItem && player.world.getRegistryKey() == DimensionInit.VOID_WORLD) {
            info.cancel();
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/BlockHitResult;getSide()Lnet/minecraft/util/math/Direction;"), cancellable = true)
    private void handleBlockBreakingMixin(boolean bl, CallbackInfo info) {
        if (player != null && !player.isCreative() && player.world.getRegistryKey() == DimensionInit.VOID_WORLD) {
            interactionManager.cancelBlockBreaking();
            info.cancel();
        }
    }

}
