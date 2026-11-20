package net.voidz.access;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.math.GlobalPos;

public interface ServerPlayerAccess {

    public void setVoidReturnPos(@Nullable GlobalPos pos);

    @Nullable
    public GlobalPos getVoidReturnPos();
}
