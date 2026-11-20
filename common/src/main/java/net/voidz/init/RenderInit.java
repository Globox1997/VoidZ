package net.voidz.init;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.voidz.block.render.PortalBlockEntityRenderer;

public class RenderInit {

    public static void registerBlockEntityRenderers(BlockEntityRendererRegistrar registrar) {
        registrar.register(BlockInit.PORTAL_BLOCK_ENTITY, PortalBlockEntityRenderer::new);
    }

    public interface BlockEntityRendererRegistrar {
        <T extends BlockEntity, S extends BlockEntityRenderState> void register(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T, S> provider);
    }
}
