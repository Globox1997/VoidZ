package net.voidz;

import net.fabricmc.api.ModInitializer;
import net.voidz.init.BlockInit;
import net.voidz.init.ConfigInit;
import net.voidz.init.DimensionInit;
import net.voidz.init.EventInit;
import net.voidz.init.ItemInit;

public class VoidMain implements ModInitializer {

    @Override
    public void onInitialize() {
        ConfigInit.init();
        DimensionInit.init();
        ItemInit.init();
        BlockInit.init();
        EventInit.init();
    }
}

// You are LOVED!!!
// Jesus loves you unconditional!