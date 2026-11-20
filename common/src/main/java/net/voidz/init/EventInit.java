package net.voidz.init;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EventInit {

    public static boolean canGlide(LivingEntity entity) {
        return !(entity instanceof Player player && DimensionInit.isRestricted(player, player.level()));
    }

    public static boolean canAttackBlock(Player player, Level world) {
        return player.isSpectator() || !DimensionInit.isRestricted(player, world);
    }

    public static boolean canBreakBlock(Player player, Level world) {
        return !DimensionInit.isRestricted(player, world);
    }

}
