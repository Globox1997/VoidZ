package net.voidz.block.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.voidz.block.entity.PortalBlockEntity;

@Environment(EnvType.CLIENT)
public class PortalBlockEntityRenderer extends EndPortalBlockEntityRenderer<PortalBlockEntity> {

    public PortalBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(PortalBlockEntity endPortalBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        super.render(endPortalBlockEntity, f, matrixStack, vertexConsumerProvider, i, j);
    }

    @Override
    protected float getTopYOffset() {
        return 1.0f;
    }

    @Override
    protected float getBottomYOffset() {
        return 0.0f;
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

}
