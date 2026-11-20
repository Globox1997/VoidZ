package net.voidz.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
    public static final Block PORTAL = register("void_portal", new PortalBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).noOcclusion().lightLevel(state -> 15).strength(-1.0f, 3600000.0f).noLootTable().pushReaction(PushReaction.BLOCK)));
    public static final Block VOID_STONE = register("void_stone", new VoidStoneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));
    public static final Block INFESTED_VOID = register("infested_void_stone", new InfestedVoidStoneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));

    // Entity
    public static BlockEntityType<PortalBlockEntity> PORTAL_BLOCK_ENTITY;
    // Damage Types
    public static final ResourceKey<DamageType> INFESTED = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("voidz", "infested"));

    private static Block register(String id, Block block) {
        return register(ResourceLocation.fromNamespaceAndPath("voidz", id), block);
    }

    private static Block register(ResourceLocation id, Block block) {
        Item item = Registry.register(BuiltInRegistries.ITEM, id, new BlockItem(block, new Item.Properties()));
        ItemGroupEvents.modifyEntriesEvent(ItemInit.VOIDZ_ITEM_GROUP).register(entries -> entries.accept(item));

        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    public static void init() {
        // Entity
        PORTAL_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "voidz:void_portal_entity", BlockEntityType.Builder.of(PortalBlockEntity::new, PORTAL).build(null));
    }

}
