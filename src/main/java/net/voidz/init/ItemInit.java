package net.voidz.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemInit {
    // Item Group
    public static final ResourceKey<CreativeModeTab> VOIDZ_ITEM_GROUP = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath("voidz", "item_group"));

    public static void init() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, VOIDZ_ITEM_GROUP,
                FabricItemGroup.builder().icon(() -> new ItemStack(BlockInit.INFESTED_VOID)).title(Component.translatable("block.voidz.item_group")).build());
    }
}
