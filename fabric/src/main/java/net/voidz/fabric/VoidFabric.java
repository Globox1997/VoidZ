package net.voidz.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionResult;
import net.voidz.VoidMain;
import net.voidz.init.BlockInit;
import net.voidz.init.DimensionInit;
import net.voidz.init.EventInit;
import net.voidz.init.ItemInit;

public class VoidFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        VoidMain.init();
        DimensionInit.registerChunkGenerators((id, codec) -> Registry.register(BuiltInRegistries.CHUNK_GENERATOR, id, codec));
        BlockInit.registerBlocks((id, block) -> Registry.register(BuiltInRegistries.BLOCK, id, block));
        BlockInit.registerItems((id, item) -> Registry.register(BuiltInRegistries.ITEM, id, item));
        ItemInit.registerCreativeModeTabs((id, tab) -> Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id, tab));
        BlockInit.registerBlockEntityTypes((id, type) -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, type));

        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> EventInit.canAttackBlock(player, world) ? InteractionResult.PASS : InteractionResult.FAIL);
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> EventInit.canBreakBlock(player, world));
    }
}
