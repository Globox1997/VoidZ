package net.voidz.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemInit {
    // Item Group
    public static final RegistryKey<ItemGroup> VOIDZ_ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier("voidz", "item_group"));

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, VOIDZ_ITEM_GROUP,
                FabricItemGroup.builder().icon(() -> new ItemStack(BlockInit.INFESTED_VOID_BLOCK)).displayName(Text.translatable("block.voidz.item_group")).build());
    }
}
