package net.voidz.access;

import net.minecraft.core.GlobalPos;
import org.jetbrains.annotations.Nullable;

public interface ServerPlayerAccess {

    public void setVoidReturnPos(@Nullable GlobalPos pos);

    @Nullable
    public GlobalPos getVoidReturnPos();
}
