package net.voidz.block.render;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.voidz.block.entity.PortalBlockEntity;

@Environment(EnvType.CLIENT)
public class PortalBlockEntityRenderer<T extends PortalBlockEntity> extends BlockEntityRenderer<T> {
    private static final Random RANDOM = new Random(31100L);
    private static final List<RenderLayer> render_field = (List<RenderLayer>) IntStream.range(0, 16).mapToObj((i) -> {
        return RenderLayer.getEndPortal(i + 1);
    }).collect(ImmutableList.toImmutableList());

    public PortalBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
        super(blockEntityRenderDispatcher);
    }

    public void render(T endPortalBlockEntity, float f, MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        RANDOM.setSeed(31100L);
        double d = endPortalBlockEntity.getPos().getSquaredDistance(this.dispatcher.camera.getPos(), true);
        int k = this.getInteger(d);
        Matrix4f matrix4f = matrixStack.peek().getModel();
        this.method_23084(endPortalBlockEntity, 0.15F, matrix4f,
                vertexConsumerProvider.getBuffer((RenderLayer) render_field.get(0)));

        for (int l = 1; l < k; ++l) {
            this.method_23084(endPortalBlockEntity, 2.0F / (float) (18 - l), matrix4f,
                    vertexConsumerProvider.getBuffer((RenderLayer) render_field.get(l)));
        }

    }

    private void method_23084(T endPortalBlockEntity, float g, Matrix4f matrix4f, VertexConsumer vertexConsumer) {
        float h = (RANDOM.nextFloat() * 0.5F + 0.1F) * g;
        float i = (RANDOM.nextFloat() * 0.5F + 0.4F) * g;
        float j = (RANDOM.nextFloat() * 0.5F + 0.5F) * g;
        this.method_23085(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F,
                1.0F, h, i, j, Direction.SOUTH);
        this.method_23085(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F,
                0.0F, h, i, j, Direction.NORTH);
        this.method_23085(endPortalBlockEntity, matrix4f, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F,
                0.0F, h, i, j, Direction.EAST);
        this.method_23085(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F,
                0.0F, h, i, j, Direction.WEST);
        this.method_23085(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F,
                1.0F, h, i, j, Direction.DOWN);
        this.method_23085(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F,
                0.0F, h, i, j, Direction.UP);
    }

    private void method_23085(T endPortalBlockEntity, Matrix4f matrix4f, VertexConsumer vertexConsumer, float f,
            float g, float h, float i, float j, float k, float l, float m, float n, float o, float p,
            Direction direction) {
        vertexConsumer.vertex(matrix4f, f, h, j).color(n, o, p, 1.0F).next();
        vertexConsumer.vertex(matrix4f, g, h, k).color(n, o, p, 1.0F).next();
        vertexConsumer.vertex(matrix4f, g, i, l).color(n, o, p, 1.0F).next();
        vertexConsumer.vertex(matrix4f, f, i, m).color(n, o, p, 1.0F).next();
    }

    protected int getInteger(double d) {
        if (d > 36864.0D) {
            return 1;
        } else if (d > 25600.0D) {
            return 3;
        } else if (d > 16384.0D) {
            return 5;
        } else if (d > 9216.0D) {
            return 7;
        } else if (d > 4096.0D) {
            return 9;
        } else if (d > 1024.0D) {
            return 11;
        } else if (d > 576.0D) {
            return 13;
        } else {
            return d > 256.0D ? 14 : 15;
        }
    }
}
