package net.voidz.init;

import java.util.Set;
import java.util.function.Function;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.voidz.block.*;
import net.voidz.block.entity.*;

public class BlockInit {
    // Block
    public static final Block PORTAL = register("void_portal", PortalBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).noOcclusion().lightLevel(state -> 15).strength(-1.0f, 3600000.0f).noLootTable().pushReaction(PushReaction.IMMOVEABLE));
    public static final Block VOID_STONE = register("void_stone", VoidStoneBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE));
    public static final Block INFESTED_VOID = register("infested_void_stone", InfestedVoidStoneBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE));

    // Entity
    public static BlockEntityType<PortalBlockEntity> PORTAL_BLOCK_ENTITY;
    // Damage Types
    public static final ResourceKey<DamageType> INFESTED = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath("voidz", "infested"));

    private static Block register(String id, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        Identifier identifier = Identifier.fromNamespaceAndPath("voidz", id);
        Block block = factory.apply(properties.setId(ResourceKey.create(Registries.BLOCK, identifier)));
        Item item = Registry.register(BuiltInRegistries.ITEM, identifier, new BlockItem(block, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, identifier)).useBlockDescriptionPrefix()));
        CreativeModeTabEvents.modifyOutputEvent(ItemInit.VOIDZ_ITEM_GROUP).register(output -> output.accept(item));

        return Registry.register(BuiltInRegistries.BLOCK, identifier, block);
    }

    public static void init() {
        // Entity
        PORTAL_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath("voidz", "void_portal_entity"), new BlockEntityType<>(PortalBlockEntity::new, Set.of(PORTAL)));
    }

}
