package net.voidz.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.voidz.block.render.PortalBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class RenderInit {

    public static void init() {
        BlockEntityRendererFactories.register(BlockInit.PORTAL_BLOCK_ENTITY, PortalBlockEntityRenderer::new);
    }
}
