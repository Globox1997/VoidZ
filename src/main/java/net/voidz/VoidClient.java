package net.voidz;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.voidz.init.RenderInit;

@Environment(EnvType.CLIENT)
public class VoidClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        RenderInit.init();
    }

}
