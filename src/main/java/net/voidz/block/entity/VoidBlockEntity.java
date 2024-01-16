package net.voidz.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.voidz.block.VoidStoneBlock;
import net.voidz.init.BlockInit;

public class VoidBlockEntity extends BlockEntity {
    private int ticker;

    public VoidBlockEntity(BlockPos pos, BlockState state) {
        super(BlockInit.VOID_BLOCK_ENTITY, pos, state);
    }

    public void tick() {
        if (!this.world.isClient() && this.getCachedState().get(VoidStoneBlock.ACTIVATED)) {
            ticker++;
            if (ticker % 10 == 0) {
                if (this.getCachedState().get(VoidStoneBlock.DESTROYTIME) == 7) {
                    this.world.breakBlock(pos, false);
                }
                if (this.getCachedState().get(VoidStoneBlock.DESTROYTIME) < 7) {
                    this.world.setBlockState(pos,
                            this.getCachedState().with(VoidStoneBlock.ACTIVATED, true).with(VoidStoneBlock.DESTROYTIME, this.getCachedState().get(VoidStoneBlock.DESTROYTIME) + 1));
                }
            }
        }

    }

}
