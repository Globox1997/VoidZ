package net.voidz.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.voidz.VoidMain;
import net.voidz.platform.Services;

public class ItemInit {
    // Item Group
    public static final ResourceKey<CreativeModeTab> VOIDZ_ITEM_GROUP = ResourceKey.create(Registries.CREATIVE_MODE_TAB, VoidMain.identifierOf("item_group"));
    public static final List<Item> ITEM_GROUP_ITEMS = new ArrayList<>();

    public static void registerCreativeModeTabs(BiConsumer<Identifier, CreativeModeTab> registrar) {
        registrar.accept(VOIDZ_ITEM_GROUP.identifier(),
                Services.PLATFORM.createCreativeModeTab(Component.translatable("block.voidz.item_group"), () -> new ItemStack(BlockInit.INFESTED_VOID), () -> ITEM_GROUP_ITEMS));
    }
}
