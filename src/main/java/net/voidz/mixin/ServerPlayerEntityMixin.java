package net.voidz.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.voidz.access.ServerPlayerAccess;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ServerPlayerAccess {

    @Unique
    private static final String VOID_RETURN_POS_KEY = "VoidReturnPos";
    @Unique
    private static final String LEGACY_POS_KEY = "VoidPortingBlockPos";

    @Unique
    @Nullable
    private GlobalPos voidReturnPos;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbtMixin(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains(VOID_RETURN_POS_KEY)) {
            this.voidReturnPos = GlobalPos.CODEC.parse(NbtOps.INSTANCE, nbt.get(VOID_RETURN_POS_KEY)).result().orElse(null);
        } else if (nbt.contains(LEGACY_POS_KEY + "X")) {
            BlockPos legacyPos = new BlockPos(nbt.getInt(LEGACY_POS_KEY + "X"), nbt.getInt(LEGACY_POS_KEY + "Y"), nbt.getInt(LEGACY_POS_KEY + "Z"));
            this.voidReturnPos = legacyPos.equals(BlockPos.ORIGIN) ? null : GlobalPos.create(World.OVERWORLD, legacyPos);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbtMixin(NbtCompound nbt, CallbackInfo info) {
        if (this.voidReturnPos != null) {
            GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, this.voidReturnPos).result().ifPresent(element -> nbt.put(VOID_RETURN_POS_KEY, element));
        }
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void copyFromMixin(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info) {
        this.voidReturnPos = ((ServerPlayerAccess) oldPlayer).getVoidReturnPos();
    }

    @Override
    public void setVoidReturnPos(@Nullable GlobalPos pos) {
        this.voidReturnPos = pos;
    }

    @Override
    @Nullable
    public GlobalPos getVoidReturnPos() {
        return this.voidReturnPos;
    }
}
