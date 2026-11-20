package net.voidz.block.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.voidz.block.entity.PortalBlockEntity;

@Environment(EnvType.CLIENT)
public class PortalBlockEntityRenderer extends TheEndPortalRenderer<PortalBlockEntity> {

    public PortalBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(PortalBlockEntity endPortalBlockEntity, float f, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, int j) {
        super.render(endPortalBlockEntity, f, matrixStack, vertexConsumerProvider, i, j);
    }

    @Override
    protected float getOffsetUp() {
        return 1.0f;
    }

    @Override
    protected float getOffsetDown() {
        return 0.0f;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

}
