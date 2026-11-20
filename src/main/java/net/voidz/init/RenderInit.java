package net.voidz.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.voidz.block.render.PortalBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class RenderInit {

    public static void init() {
        BlockEntityRenderers.register(BlockInit.PORTAL_BLOCK_ENTITY, PortalBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.PORTAL, RenderType.cutoutMipped());
    }
}
