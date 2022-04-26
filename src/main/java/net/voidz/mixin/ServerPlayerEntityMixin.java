package net.voidz.mixin;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.voidz.access.ServerPlayerAccess;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ServerPlayerAccess {

    private BlockPos voidPortingBlockPos = new BlockPos(0, 0, 0);

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbtMixin(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("VoidPortingBlockPosX"))
            voidPortingBlockPos = new BlockPos(nbt.getInt("VoidPortingBlockPosX"), nbt.getInt("VoidPortingBlockPosY"), nbt.getInt("VoidPortingBlockPosZ"));
        else if (this.world != null && this.world instanceof ServerWorld)
            voidPortingBlockPos = ((ServerWorld) this.world).getSpawnPos();

    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbtMixin(NbtCompound nbt, CallbackInfo info) {
        nbt.putInt("VoidPortingBlockPosX", voidPortingBlockPos.getX());
        nbt.putInt("VoidPortingBlockPosY", voidPortingBlockPos.getY());
        nbt.putInt("VoidPortingBlockPosZ", voidPortingBlockPos.getZ());
    }

    @Override
    public void setVoidPortingBlockPos(BlockPos pos) {
        this.voidPortingBlockPos = pos;
    }

    @Override
    public BlockPos getVoidPortingBlockPos() {
        return voidPortingBlockPos;
    }
}
