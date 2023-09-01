package net.voidz.init;

import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.entity.player.PlayerEntity;

public class EventInit {

    public static void init() {
        EntityElytraEvents.ALLOW.register((entity) -> {
            if (entity instanceof PlayerEntity player && !player.isCreative() && player.getWorld().getRegistryKey() == DimensionInit.VOID_WORLD) {
                return false;
            }
            return true;
        });
    }

}
