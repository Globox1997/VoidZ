package net.voidz.init;

import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.voidz.dimension.VoidChunkGenerator;

public class DimensionInit {

    public static final ResourceKey<Level> VOID_WORLD = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath("voidz", "void"));
    public static final ResourceKey<DimensionType> VOID_DIMENSION_TYPE_KEY = ResourceKey.create(Registries.DIMENSION_TYPE, Identifier.fromNamespaceAndPath("voidz", "void"));

    public static void registerChunkGenerators(BiConsumer<Identifier, MapCodec<? extends ChunkGenerator>> registrar) {
        registrar.accept(Identifier.fromNamespaceAndPath("voidz", "void"), VoidChunkGenerator.CODEC);
    }

    public static boolean isRestricted(@Nullable Player player, Level world) {
        return world.dimension() == VOID_WORLD && (player == null || !player.isCreative());
    }

}
