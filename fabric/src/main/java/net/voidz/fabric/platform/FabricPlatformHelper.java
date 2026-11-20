package net.voidz.fabric.platform;

import java.util.List;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.voidz.platform.services.IPlatformHelper;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public CreativeModeTab createCreativeModeTab(Component title, Supplier<ItemStack> icon, Supplier<List<Item>> displayItems) {
        return FabricCreativeModeTab.builder().title(title).icon(icon).displayItems((parameters, output) -> displayItems.get().forEach(output::accept)).build();
    }
}
