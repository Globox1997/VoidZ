package net.voidz.block;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.voidz.init.BlockInit;

public class InfestedVoidStoneBlock extends Block {

    public InfestedVoidStoneBlock(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.PORTAL, (double) pos.getX() + random.nextDouble(), (double) pos.getY() + 1.0D + (random.nextDouble() / 10D), (double) pos.getZ() + random.nextDouble(),
                    0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClient() && entity instanceof PlayerEntity) {
            if (world.getRandom().nextInt(12) == 0) {
                ((PlayerEntity) entity).damage(createDamageSource(entity), 2);
            }
        }
    }

    private DamageSource createDamageSource(Entity entity) {
        return entity.getDamageSources().create(BlockInit.INFESTED, entity);
    }

}
