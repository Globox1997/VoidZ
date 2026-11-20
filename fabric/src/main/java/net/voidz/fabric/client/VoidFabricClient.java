package net.voidz.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.voidz.init.RenderInit;

@Environment(EnvType.CLIENT)
public class VoidFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        RenderInit.registerBlockEntityRenderers(new RenderInit.BlockEntityRendererRegistrar() {
            @Override
            public <T extends BlockEntity, S extends BlockEntityRenderState> void register(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T, S> provider) {
                BlockEntityRenderers.register(blockEntityType, provider);
            }
        });
    }

}
