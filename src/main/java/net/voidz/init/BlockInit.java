package net.voidz.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.voidz.block.*;
import net.voidz.block.entity.*;

public class BlockInit {
    // Block
    public static final Block PORTAL = register("void_portal",
            new PortalBlock(FabricBlockSettings.create().mapColor(MapColor.BLACK).nonOpaque().luminance(15).strength(-1.0f, 3600000.0f).dropsNothing().pistonBehavior(PistonBehavior.BLOCK)));
    public static final Block VOID_STONE = register("void_stone", new VoidStoneBlock(FabricBlockSettings.copy(Blocks.END_STONE)));
    public static final Block INFESTED_VOID = register("infested_void_stone", new InfestedVoidStoneBlock(FabricBlockSettings.copy(Blocks.END_STONE)));

    // Entity
    public static BlockEntityType<PortalBlockEntity> PORTAL_BLOCK_ENTITY;
    public static BlockEntityType<VoidBlockEntity> VOID_BLOCK_ENTITY;
    // Damage Types
    public static final RegistryKey<DamageType> INFESTED = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("voidz", "infested"));

    private static Block register(String id, Block block) {
        return register(new Identifier("voidz", id), block);
    }

    private static Block register(Identifier id, Block block) {
        Item item = Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        ItemGroupEvents.modifyEntriesEvent(ItemInit.VOIDZ_ITEM_GROUP).register(entries -> entries.add(item));

        return Registry.register(Registries.BLOCK, id, block);
    }

    public static void init() {
        // Entity
        PORTAL_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "voidz:void_portal_entity", FabricBlockEntityTypeBuilder.create(PortalBlockEntity::new, PORTAL).build(null));
        VOID_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "voidz:void_block_entity", FabricBlockEntityTypeBuilder.create(VoidBlockEntity::new, VOID_STONE).build(null));
    }

}