package net.voidz.access;

import net.minecraft.util.math.BlockPos;

public interface ServerPlayerAccess {

    public void setVoidPortingBlockPos(BlockPos pos);

    public BlockPos getVoidPortingBlockPos();
}
