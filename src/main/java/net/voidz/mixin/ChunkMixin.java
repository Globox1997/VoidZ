package net.voidz.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkAccess.class)
public class ChunkMixin {

    // TODO: REMOVE THIS on mc 26+ onward
    @Unique
    private static final String LEGACY_VOID_STONE_BLOCK_ENTITY = "voidz:void_block_entity";

    @Inject(method = "setBlockEntityNbt", at = @At("HEAD"), cancellable = true)
    private void addPendingBlockEntityNbtMixin(CompoundTag nbt, CallbackInfo info) {
        if (LEGACY_VOID_STONE_BLOCK_ENTITY.equals(nbt.getStringOr("id", ""))) {
            info.cancel();
        }
    }

}
