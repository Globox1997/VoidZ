package net.voidz.init;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.voidz.dimension.VoidChunkGenerator;

public class DimensionInit {

    public static final RegistryKey<World> VOID_WORLD = RegistryKey.of(RegistryKeys.WORLD, Identifier.of("voidz", "void"));
    public static final RegistryKey<DimensionType> VOID_DIMENSION_TYPE_KEY = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, Identifier.of("voidz", "void"));

    public static void init() {
        Registry.register(Registries.CHUNK_GENERATOR, Identifier.of("voidz", "void"), VoidChunkGenerator.CODEC);
    }

    public static boolean isRestricted(@Nullable PlayerEntity player, World world) {
        return world.getRegistryKey() == VOID_WORLD && (player == null || !player.isCreative());
    }

}
