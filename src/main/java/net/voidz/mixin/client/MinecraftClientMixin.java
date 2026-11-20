package net.voidz.mixin.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.MusicType;
import net.voidz.init.DimensionInit;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "getMusicType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getBlockPos()Lnet/minecraft/util/math/BlockPos;"), cancellable = true)
    public void getMusicTypeMixin(CallbackInfoReturnable<MusicSound> info) {
        if (this.player.getWorld().getRegistryKey() == DimensionInit.VOID_WORLD) {
            info.setReturnValue(MusicType.DRAGON);
        }
    }

}
