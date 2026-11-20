package net.voidz.platform.services;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IPlatformHelper {

    CreativeModeTab createCreativeModeTab(Component title, Supplier<ItemStack> icon, Supplier<List<Item>> displayItems);
}
