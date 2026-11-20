package net.voidz.neoforge;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.voidz.VoidMain;
import net.voidz.init.BlockInit;
import net.voidz.init.DimensionInit;
import net.voidz.init.EventInit;
import net.voidz.init.ItemInit;

@Mod(VoidMain.MOD_ID)
public class VoidNeoForge {

    public VoidNeoForge(IEventBus modBus) {
        VoidMain.init();
        modBus.addListener(RegisterEvent.class, VoidNeoForge::register);

        NeoForge.EVENT_BUS.addListener(PlayerInteractEvent.LeftClickBlock.class, event -> {
            if (!EventInit.canAttackBlock(event.getEntity(), event.getLevel())) {
                event.setCanceled(true);
            }
        });
        NeoForge.EVENT_BUS.addListener(BreakBlockEvent.class, event -> {
            if (event.getLevel() instanceof Level level && !EventInit.canBreakBlock(event.getPlayer(), level)) {
                event.setCanceled(true);
            }
        });
    }

    private static void register(RegisterEvent event) {
        event.register(Registries.CHUNK_GENERATOR, helper -> DimensionInit.registerChunkGenerators(helper::register));
        event.register(Registries.BLOCK, helper -> BlockInit.registerBlocks(helper::register));
        event.register(Registries.ITEM, helper -> BlockInit.registerItems(helper::register));
        event.register(Registries.CREATIVE_MODE_TAB, helper -> ItemInit.registerCreativeModeTabs(helper::register));
        event.register(Registries.BLOCK_ENTITY_TYPE, helper -> BlockInit.registerBlockEntityTypes(helper::register));
    }
}
