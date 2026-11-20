package net.voidz.dimension;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class VoidChunkGenerator extends ChunkGenerator {

    public static final MapCodec<VoidChunkGenerator> CODEC = RecordCodecBuilder
            .mapCodec(instance -> instance.group(RegistryOps.retrieveElement(Biomes.PLAINS)).apply(instance, instance.stable(VoidChunkGenerator::new)));

    private static final WeightedList<MobSpawnSettings.SpawnerData> NO_SPAWNS = WeightedList.of();

    public VoidChunkGenerator(Holder.Reference<Biome> biomeEntry) {
        super(new FixedBiomeSource(biomeEntry));
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel world, ChunkAccess chunk, StructureManager structureAccessor) {
    }

    @Override
    public void createStructures(RegistryAccess registryManager, ChunkGeneratorStructureState placementCalculator, StructureManager structureAccessor, ChunkAccess chunk,
            StructureTemplateManager structureTemplateManager, ResourceKey<Level> levelKey) {
    }

    @Override
    public void createReferences(WorldGenLevel world, StructureManager accessor, ChunkAccess chunk) {
    }

    @Override
    public WeightedList<MobSpawnSettings.SpawnerData> getMobsAt(Level level, StructureManager accessor, MobCategory group, BlockPos pos) {
        return NO_SPAWNS;
    }

    @Override
    public int getGenDepth() {
        return 256;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public CompletableFuture<ChunkAccess> buildTerrain(ChunkAccess chunk, Blender blender, RandomState noiseConfig, StructureManager structureAccessor, BiomeManager biomeAccess, WorldGenRegion chunkRegion,
            Set<Holder<Biome>> biomes) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getBaseHeight(int x, int z, Types type, LevelHeightAccessor heightLimitView, RandomState noiseConfig) {
        return 0;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor heightLimitView, RandomState noiseConfig) {
        return new NoiseColumn(heightLimitView.getMinY(), new BlockState[0]);
    }

    @Override
    public void addDebugScreenInfo(List<String> list, RandomState noiseConfig, BlockPos blockPos, SamplerContext samplerContext) {
    }

}