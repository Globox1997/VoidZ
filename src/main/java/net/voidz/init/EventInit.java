package net.voidz.init;

import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

public class EventInit {

    public static void init() {
        EntityElytraEvents.ALLOW.register((entity) -> !(entity instanceof Player player && DimensionInit.isRestricted(player, player.level())));

        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (!player.isSpectator() && DimensionInit.isRestricted(player, world)) {
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> !DimensionInit.isRestricted(player, world));
    }

}
