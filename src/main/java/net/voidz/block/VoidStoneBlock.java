package net.voidz.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class VoidStoneBlock extends Block {

    public static final BooleanProperty ACTIVATED;
    public static final IntProperty DESTROYTIME;

    private static final int MAX_DESTROY_TIME = 7;
    private static final int STAGE_DELAY = 10;

    public VoidStoneBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(ACTIVATED, false).with(DESTROYTIME, 0));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (placer != null && placer.isSneaking()) {
            world.setBlockState(pos, state.with(VoidStoneBlock.ACTIVATED, true));
        }
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient() && state.get(ACTIVATED)) {
            world.scheduleBlockTick(pos, this, STAGE_DELAY);
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.get(ACTIVATED)) {
            return;
        }
        int destroyTime = state.get(DESTROYTIME);
        if (destroyTime >= MAX_DESTROY_TIME) {
            world.breakBlock(pos, false);
        } else {
            world.setBlockState(pos, state.with(DESTROYTIME, destroyTime + 1), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return state.get(ACTIVATED);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(ACTIVATED) && !world.getBlockTickScheduler().isQueued(pos, this)) {
            world.scheduleBlockTick(pos, this, STAGE_DELAY);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(ACTIVATED);
        stateManager.add(DESTROYTIME);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.PORTAL, (double) pos.getX() + random.nextDouble(), (double) pos.getY() + 1.08D, (double) pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }

    static {
        ACTIVATED = BooleanProperty.of("activated_void");
        DESTROYTIME = IntProperty.of("destroy_time", 0, MAX_DESTROY_TIME);
    }

}
