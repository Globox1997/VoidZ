package net.voidz.init;

import org.jetbrains.annotations.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.voidz.dimension.VoidChunkGenerator;

public class DimensionInit {

    public static final ResourceKey<Level> VOID_WORLD = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("voidz", "void"));
    public static final ResourceKey<DimensionType> VOID_DIMENSION_TYPE_KEY = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath("voidz", "void"));

    public static void init() {
        Registry.register(BuiltInRegistries.CHUNK_GENERATOR, ResourceLocation.fromNamespaceAndPath("voidz", "void"), VoidChunkGenerator.CODEC);
    }

    public static boolean isRestricted(@Nullable Player player, Level world) {
        return world.dimension() == VOID_WORLD && (player == null || !player.isCreative());
    }

}
