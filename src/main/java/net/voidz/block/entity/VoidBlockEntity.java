package net.voidz.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import net.voidz.block.VoidBlock;
import net.voidz.init.BlockInit;

public class VoidBlockEntity extends BlockEntity implements Tickable {
    private int ticker;

    public VoidBlockEntity() {
        super(BlockInit.VOID_BLOCK_ENTITY);
    }

    @Override
    public void tick() {
        if (!this.world.isClient && this.getCachedState().get(VoidBlock.ACTIVATED)) {
            ticker++;
            if (ticker % 10 == 0 && this.getCachedState().get(VoidBlock.DESTROYTIME) < 7) {
                this.world.setBlockState(pos, this.getCachedState().with(VoidBlock.ACTIVATED, true)
                        .with(VoidBlock.DESTROYTIME, this.getCachedState().get(VoidBlock.DESTROYTIME) + 1));
            }
        }

    }

}
