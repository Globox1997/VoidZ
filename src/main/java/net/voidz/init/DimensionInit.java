package net.voidz.init;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.voidz.dimension.VoidChunkGenerator;

public class DimensionInit {

    public static final RegistryKey<World> VOID_WORLD = RegistryKey.of(Registry.DIMENSION,
            new Identifier("voidz", "void"));

    public static void init() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier("voidz", "void"), VoidChunkGenerator.CODEC);
    }

}
