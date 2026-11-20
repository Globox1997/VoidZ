package net.voidz.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.voidz.access.ServerPlayerAccess;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player implements ServerPlayerAccess {

    @Unique
    private static final String VOID_RETURN_POS_KEY = "VoidReturnPos";
    @Unique
    private static final String LEGACY_POS_KEY = "VoidPortingBlockPos";

    @Unique
    @Nullable
    private GlobalPos voidReturnPos;

    public ServerPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readCustomDataFromNbtMixin(CompoundTag nbt, CallbackInfo info) {
        if (nbt.contains(VOID_RETURN_POS_KEY)) {
            this.voidReturnPos = GlobalPos.CODEC.parse(NbtOps.INSTANCE, nbt.get(VOID_RETURN_POS_KEY)).result().orElse(null);
        } else if (nbt.contains(LEGACY_POS_KEY + "X")) {
            BlockPos legacyPos = new BlockPos(nbt.getInt(LEGACY_POS_KEY + "X"), nbt.getInt(LEGACY_POS_KEY + "Y"), nbt.getInt(LEGACY_POS_KEY + "Z"));
            this.voidReturnPos = legacyPos.equals(BlockPos.ZERO) ? null : GlobalPos.of(Level.OVERWORLD, legacyPos);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeCustomDataToNbtMixin(CompoundTag nbt, CallbackInfo info) {
        if (this.voidReturnPos != null) {
            GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, this.voidReturnPos).result().ifPresent(element -> nbt.put(VOID_RETURN_POS_KEY, element));
        }
    }

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void copyFromMixin(ServerPlayer oldPlayer, boolean alive, CallbackInfo info) {
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
