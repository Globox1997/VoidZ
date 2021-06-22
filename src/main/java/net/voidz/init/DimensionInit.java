package net.voidz.init;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.voidz.dimension.VoidChunkGenerator;

public class DimensionInit {

    public static final RegistryKey<World> VOID_WORLD = RegistryKey.of(Registry.WORLD_KEY, new Identifier("voidz", "void"));
    public static final RegistryKey<DimensionType> VOID_DIMENSION_TYPE_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, new Identifier("voidz", "void"));

    public static void init() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier("voidz", "void"), VoidChunkGenerator.CODEC);
    }

}