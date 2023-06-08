package net.voidz.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.voidz.dimension.VoidChunkGenerator;

public class DimensionInit {

    public static final RegistryKey<World> VOID_WORLD = RegistryKey.of(RegistryKeys.WORLD, new Identifier("voidz", "void"));
    public static final RegistryKey<DimensionType> VOID_DIMENSION_TYPE_KEY = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, new Identifier("voidz", "void"));

    public static void init() {
        Registry.register(Registries.CHUNK_GENERATOR, new Identifier("voidz", "void"), VoidChunkGenerator.CODEC);
    }

}