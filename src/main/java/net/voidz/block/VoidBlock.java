package net.voidz.block;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.voidz.block.entity.VoidBlockEntity;

public class VoidBlock extends Block implements BlockEntityProvider {

    public static final BooleanProperty ACTIVATED;
    public static final IntProperty DESTROYTIME;

    public VoidBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState) ((BlockState) this.stateManager.getDefaultState()).with(ACTIVATED, false)
                .with(DESTROYTIME, 0));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new VoidBlockEntity();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(ACTIVATED);
        stateManager.add(DESTROYTIME);
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.PORTAL, (double) pos.getX() + random.nextDouble(),
                    (double) pos.getY() + 1.08D, (double) pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }

    static {
        ACTIVATED = BooleanProperty.of("activated_void");
        DESTROYTIME = IntProperty.of("destroy_time", 0, 7);
    }

}
