package net.voidz.block;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VoidBlock extends Block {

    public VoidBlock(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.PORTAL, (double) pos.getX() + random.nextDouble(),
                    (double) pos.getY() + 1.08D, (double) pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }

}
