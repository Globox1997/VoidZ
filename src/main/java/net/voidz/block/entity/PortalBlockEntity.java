package net.voidz.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.voidz.init.BlockInit;

public class PortalBlockEntity extends BlockEntity {

    private float ticker;
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
        bossTime = nbt.getInt("Boss_Killed_Time");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Boss_Killed_Time", bossTime);
        return nbt;
    }

    private void update() {
        if (this.world.isClient) {
            ticker += (float) Math.PI / 128F;
            double angle = 2 * Math.PI * ticker;
            this.world.addParticle(ParticleTypes.PORTAL, (double) this.getPos().getX() + 0.5F + 1.5F * Math.sin(angle), (double) this.getPos().getY() + 0.5F + Math.sin(angle) / 2.0F,
                    (double) this.getPos().getZ() + 0.5F + 1.5F * Math.cos(angle), 0.0D, 0.0D, 0.0D);
            this.world.addParticle(ParticleTypes.PORTAL, (double) this.getPos().getX() + 0.5F + 1.5F * Math.sin(angle), (double) this.getPos().getY() + 0.5F + Math.sin(angle + Math.PI) / 2.0F,
                    (double) this.getPos().getZ() + 0.5F + 1.5F * Math.cos(angle), 0.0D, 0.0D, 0.0D);
        }

    }

}
