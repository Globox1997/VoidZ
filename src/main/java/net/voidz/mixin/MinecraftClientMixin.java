package net.voidz.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MusicType;
import net.minecraft.sound.MusicSound;
import net.voidz.init.DimensionInit;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "getMusicType", at = @At(value = "HEAD"), cancellable = true)
    public void getMusicTypeMixin(CallbackInfoReturnable<MusicSound> info) {
        if (this.player != null && this.player.world.getRegistryKey() == DimensionInit.VOID_WORLD) {
            info.setReturnValue(MusicType.END);
        }

    }

}
