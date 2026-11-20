package net.voidz.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class VoidStoneBlock extends Block {

    public static final BooleanProperty ACTIVATED;
    public static final IntegerProperty DESTROYTIME;

    private static final int MAX_DESTROY_TIME = 7;
    private static final int STAGE_DELAY = 10;

    public VoidStoneBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVATED, false).setValue(DESTROYTIME, 0));
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (placer != null && placer.isShiftKeyDown()) {
            world.setBlockAndUpdate(pos, state.setValue(VoidStoneBlock.ACTIVATED, true));
        }
    }

    @Override
    protected void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClientSide() && state.getValue(ACTIVATED)) {
            world.scheduleTick(pos, this, STAGE_DELAY);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.getValue(ACTIVATED)) {
            return;
        }
        int destroyTime = state.getValue(DESTROYTIME);
        if (destroyTime >= MAX_DESTROY_TIME) {
            world.destroyBlock(pos, false);
        } else {
            world.setBlock(pos, state.setValue(DESTROYTIME, destroyTime + 1), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(ACTIVATED);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (state.getValue(ACTIVATED) && !world.getBlockTicks().hasScheduledTick(pos, this)) {
            world.scheduleTick(pos, this, STAGE_DELAY);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(ACTIVATED);
        stateManager.add(DESTROYTIME);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.PORTAL, (double) pos.getX() + random.nextDouble(), (double) pos.getY() + 1.08D, (double) pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }

    static {
        ACTIVATED = BooleanProperty.create("activated_void");
        DESTROYTIME = IntegerProperty.create("destroy_time", 0, MAX_DESTROY_TIME);
    }

}
