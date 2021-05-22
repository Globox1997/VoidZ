package net.voidz;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.voidz.block.render.PortalBlockEntityRenderer;
import net.voidz.init.BlockInit;

public class VoidClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(BlockInit.PORTAL_BLOCK_ENTITY, PortalBlockEntityRenderer::new);
        // BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.PIGLIN_FLAG_BLOCK,
        // RenderLayer.getCutout());
    }

}
