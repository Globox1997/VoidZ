package net.voidz;

import net.fabricmc.api.ModInitializer;
import net.voidz.init.BlockInit;
import net.voidz.init.ConfigInit;
import net.voidz.init.DimensionInit;

public class VoidMain implements ModInitializer {

    @Override
    public void onInitialize() {
        ConfigInit.init();
        DimensionInit.init();
        BlockInit.init();
    }
}

// You are LOVED!!!
// Jesus loves you unconditional!