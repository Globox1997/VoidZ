package net.voidz.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.voidz.init.BlockInit;

public class InfestedVoidStoneBlock extends Block {

    public InfestedVoidStoneBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.PORTAL, (double) pos.getX() + random.nextDouble(), (double) pos.getY() + 1.0D + (random.nextDouble() / 10D), (double) pos.getZ() + random.nextDouble(),
                    0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClient() && entity instanceof PlayerEntity playerEntity && world.getRandom().nextInt(12) == 0) {
            playerEntity.damage(world.getDamageSources().create(BlockInit.INFESTED), 2);
        }
        super.onSteppedOn(world, pos, state, entity);
    }

}
