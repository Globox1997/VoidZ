package net.voidz.init;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.voidz.VoidMain;
import net.voidz.block.*;
import net.voidz.block.entity.*;

public class BlockInit {

    private static final Map<String, Block> BLOCK_ITEMS = new LinkedHashMap<>();

    // Block
    public static Block PORTAL;
    public static Block VOID_STONE;
    public static Block INFESTED_VOID;

    // Entity
    public static BlockEntityType<PortalBlockEntity> PORTAL_BLOCK_ENTITY;
    // Damage Types
    public static final ResourceKey<DamageType> INFESTED = ResourceKey.create(Registries.DAMAGE_TYPE, VoidMain.identifierOf("infested"));

    public static void registerBlocks(BiConsumer<Identifier, Block> registrar) {
        PORTAL = register(registrar, "void_portal", PortalBlock::new,
                BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).noOcclusion().lightLevel(state -> 15).strength(-1.0f, 3600000.0f).noLootTable().pushReaction(PushReaction.IMMOVEABLE));
        VOID_STONE = register(registrar, "void_stone", VoidStoneBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE));
        INFESTED_VOID = register(registrar, "infested_void_stone", InfestedVoidStoneBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE));
    }

    public static void registerItems(BiConsumer<Identifier, Item> registrar) {
        for (Map.Entry<String, Block> entry : BLOCK_ITEMS.entrySet()) {
            ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, VoidMain.identifierOf(entry.getKey()));
            Item item = new BlockItem(entry.getValue(), new Item.Properties().setId(key).useBlockDescriptionPrefix());
            registrar.accept(key.identifier(), item);
            ItemInit.ITEM_GROUP_ITEMS.add(item);
        }
    }

    public static void registerBlockEntityTypes(BiConsumer<Identifier, BlockEntityType<?>> registrar) {
        PORTAL_BLOCK_ENTITY = new BlockEntityType<>(PortalBlockEntity::new, Set.of(PORTAL));
        registrar.accept(VoidMain.identifierOf("void_portal_entity"), PORTAL_BLOCK_ENTITY);
    }

    private static Block register(BiConsumer<Identifier, Block> registrar, String id, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, VoidMain.identifierOf(id));
        Block block = factory.apply(properties.setId(key));
        registrar.accept(key.identifier(), block);
        BLOCK_ITEMS.put(id, block);
        return block;
    }

}
