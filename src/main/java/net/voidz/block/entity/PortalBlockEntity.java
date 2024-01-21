package net.voidz.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.voidz.dimension.VoidPlacementHandler;
import net.voidz.init.BlockInit;
import net.voidz.init.ConfigInit;
import net.voidz.init.DimensionInit;

public class PortalBlockEntity extends EndPortalBlockEntity {

    private float particleTicker;
    private int spawnTicker;
    public int bossTime;

    public PortalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockInit.PORTAL_BLOCK_ENTITY, pos, state);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, PortalBlockEntity blockEntity) {
        blockEntity.update();
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, PortalBlockEntity blockEntity) {
        blockEntity.update();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.bossTime = nbt.getInt("VoidShadowKilledTime");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("VoidShadowKilledTime", this.bossTime);
    }

    @Override
    public boolean shouldDrawSide(Direction direction) {
        return true;
    }

    private void update() {
        if (this.world.isClient()) {
            this.particleTicker += (float) Math.PI / 128F;
            double angle = 2 * Math.PI * particleTicker;
            this.world.addParticle(ParticleTypes.PORTAL, (double) this.getPos().getX() + 0.5F + 1.5F * Math.sin(angle), (double) this.getPos().getY() + 0.5F + Math.sin(angle) / 2.0F,
                    (double) this.getPos().getZ() + 0.5F + 1.5F * Math.cos(angle), 0.0D, 0.0D, 0.0D);
            this.world.addParticle(ParticleTypes.PORTAL, (double) this.getPos().getX() + 0.5F + 1.5F * Math.sin(angle), (double) this.getPos().getY() + 0.5F + Math.sin(angle + Math.PI) / 2.0F,
                    (double) this.getPos().getZ() + 0.5F + 1.5F * Math.cos(angle), 0.0D, 0.0D, 0.0D);
        } else {
            this.spawnTicker++;
            if (this.spawnTicker % 100 == 0) {
                if (this != null && ConfigInit.CONFIG.allow_boss_respawn && this.bossTime != 0 && this.world.getRegistryKey() == DimensionInit.VOID_WORLD
                        && (int) this.world.getLevelProperties().getTime() > this.bossTime + ConfigInit.CONFIG.boss_respawn_time) {
                    ((ServerWorld) this.world).playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.HOSTILE, 2.0F, 1.0F);
                    this.bossTime = 0;
                    VoidPlacementHandler.spawnVoidBoss((ServerWorld) this.world, pos.up());
                }
            }
        }

    }

}
