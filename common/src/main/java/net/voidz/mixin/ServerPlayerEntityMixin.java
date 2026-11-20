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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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

    public ServerPlayerEntityMixin(Level world, GameProfile gameProfile) {
        super(world, gameProfile);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readCustomDataFromNbtMixin(ValueInput input, CallbackInfo info) {
        this.voidReturnPos = input.read(VOID_RETURN_POS_KEY, GlobalPos.CODEC).orElse(null);
        if (this.voidReturnPos == null && input.getInt(LEGACY_POS_KEY + "X").isPresent()) {
            BlockPos legacyPos = new BlockPos(input.getIntOr(LEGACY_POS_KEY + "X", 0), input.getIntOr(LEGACY_POS_KEY + "Y", 0), input.getIntOr(LEGACY_POS_KEY + "Z", 0));
            this.voidReturnPos = legacyPos.equals(BlockPos.ZERO) ? null : GlobalPos.of(Level.OVERWORLD, legacyPos);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeCustomDataToNbtMixin(ValueOutput output, CallbackInfo info) {
        output.storeNullable(VOID_RETURN_POS_KEY, GlobalPos.CODEC, this.voidReturnPos);
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
