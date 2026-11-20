package net.voidz.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.adventurez.init.SoundInit;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.voidz.init.BlockInit;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {

    @Shadow
    private BlockState blockState;

    public FallingBlockEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;cancelDrop:Z"))
    private void tickMixin(CallbackInfo info) {
        if (!this.level().isClientSide()) {
            Block block = this.blockState.getBlock();
            if (block == BlockInit.VOID_STONE) {
                this.level().playSound(null, this.blockPosition(), SoundInit.ROCK_IMPACT_EVENT, SoundSource.BLOCKS, 0.7F, 0.5F + (this.level().getRandom().nextFloat() / 2.0F));
            }
        }
    }

}
