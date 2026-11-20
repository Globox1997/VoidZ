package net.voidz.block.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.EndPortalRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.voidz.block.entity.PortalBlockEntity;

public class PortalBlockEntityRenderer extends AbstractEndPortalRenderer<PortalBlockEntity, EndPortalRenderState> {

    public PortalBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super();
    }

    @Override
    public EndPortalRenderState createRenderState() {
        return new EndPortalRenderState();
    }

    @Override
    public void submit(EndPortalRenderState state, PoseStack matrixStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        submitCube(state.facesToShow, RenderTypes.endPortal(), matrixStack, collector);
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

}
