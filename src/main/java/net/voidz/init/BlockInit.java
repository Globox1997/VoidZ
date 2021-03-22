package net.voidz.init;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.voidz.block.PortalBlock;
import net.voidz.block.VoidBlock;
import net.voidz.block.entity.PortalBlockEntity;

public class BlockInit {

    public static final PortalBlock PORTAL_BLOCK = new PortalBlock();
    public static final VoidBlock VOID_BLOCK = new VoidBlock(FabricBlockSettings.copy(Blocks.END_STONE));

    public static BlockEntityType<PortalBlockEntity> PORTAL_BLOCK_ENTITY;

    public static void init() {
        Registry.register(Registry.ITEM, new Identifier("voidz", "void_portal"),
                new BlockItem(PORTAL_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        Registry.register(Registry.BLOCK, new Identifier("voidz", "void_portal"), PORTAL_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("voidz", "void"),
                new BlockItem(VOID_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        Registry.register(Registry.BLOCK, new Identifier("voidz", "void"), VOID_BLOCK);

        PORTAL_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "voidz:void_portal_entity",
                BlockEntityType.Builder.create(PortalBlockEntity::new, PORTAL_BLOCK).build(null));
    }

}
