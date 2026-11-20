package net.voidz.neoforge.client;

import me.shedaniel.autoconfig.AutoConfigClient;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.voidz.VoidMain;
import net.voidz.config.VoidConfig;
import net.voidz.init.RenderInit;

@Mod(value = VoidMain.MOD_ID, dist = Dist.CLIENT)
public class VoidNeoForgeClient {

    public VoidNeoForgeClient(IEventBus modBus, ModContainer container) {
        modBus.addListener(EntityRenderersEvent.RegisterRenderers.class, event -> RenderInit.registerBlockEntityRenderers(new RenderInit.BlockEntityRendererRegistrar() {
            @Override
            public <T extends BlockEntity, S extends BlockEntityRenderState> void register(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T, S> provider) {
                event.registerBlockEntityRenderer(blockEntityType, provider);
            }
        }));
        container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, parent) -> AutoConfigClient.getConfigScreen(VoidConfig.class, parent).get());
    }
}
