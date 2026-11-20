package net.voidz.init;

import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public class EventInit {

    public static void init() {
        EntityElytraEvents.ALLOW.register((entity) -> !(entity instanceof PlayerEntity player && DimensionInit.isRestricted(player, player.getWorld())));

        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (!player.isSpectator() && DimensionInit.isRestricted(player, world)) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> !DimensionInit.isRestricted(player, world));
    }

}
