package net.voidz.mixin;

import java.util.Arrays;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.voidz.access.ServerPlayerAccess;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerAccess {

    @Nullable
    private BlockPos voidPortingBlockPos;

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbtMixin(NbtCompound nbt, CallbackInfo info) {
        int[] coordinateList = nbt.getIntArray("VoidPortingBlockPos");
        voidPortingBlockPos = new BlockPos(coordinateList[0], coordinateList[1], coordinateList[2]);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbtMixin(NbtCompound nbt, CallbackInfo info) {
        nbt.putIntArray("VoidPortingBlockPos", Arrays.asList(voidPortingBlockPos.getX(), voidPortingBlockPos.getY(), voidPortingBlockPos.getZ()));
    }

    @Override
    public void setVoidPortingBlockPos(BlockPos pos) {
        this.voidPortingBlockPos = pos;
    }

    @Nullable
    @Override
    public BlockPos getVoidPortingBlockPos() {
        return voidPortingBlockPos;
    }
}
