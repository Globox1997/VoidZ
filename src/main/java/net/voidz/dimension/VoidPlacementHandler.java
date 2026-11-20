package net.voidz.dimension;

import org.jetbrains.annotations.Nullable;

import net.adventurez.entity.VoidShadowEntity;
import net.adventurez.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.voidz.access.ServerPlayerAccess;
import net.voidz.block.entity.PortalBlockEntity;
import net.voidz.init.BlockInit;
import net.voidz.init.DimensionInit;

public class VoidPlacementHandler {

    public static final BlockPos VOID_SPAWN_POS = new BlockPos(0, 100, 0);
    private static final int PLATFORM_RADIUS = 40;
    private static final int SAFE_SEARCH_RANGE = 16;

    public static DimensionTransition enter(ServerPlayer serverPlayerEntity, ServerLevel serverWorld, final BlockPos portalPos) {
        ((ServerPlayerAccess) serverPlayerEntity).setVoidReturnPos(GlobalPos.of(serverPlayerEntity.level().dimension(), serverPlayerEntity.blockPosition()));
        spawnVoidPlatform(serverWorld, VOID_SPAWN_POS.below());
        return new DimensionTransition(serverWorld, Vec3.atLowerCornerOf(VOID_SPAWN_POS).add(0.5, 0, 0.5), Vec3.ZERO, 0, 0, DimensionTransition.DO_NOTHING);
    }

    public static DimensionTransition leave(ServerPlayer serverPlayerEntity) {
        MinecraftServer server = serverPlayerEntity.getServer();
        GlobalPos returnPos = ((ServerPlayerAccess) serverPlayerEntity).getVoidReturnPos();

        ServerLevel targetWorld = null;
        BlockPos targetPos = null;
        if (returnPos != null && returnPos.dimension() != DimensionInit.VOID_WORLD) {
            targetWorld = server.getLevel(returnPos.dimension());
            if (targetWorld != null) {
                targetPos = findSafePos(targetWorld, returnPos.pos());
            }
        }
        if (targetPos == null) {
            targetWorld = server.overworld();
            BlockPos spawnPos = targetWorld.getSharedSpawnPos();
            targetPos = findSafePos(targetWorld, spawnPos);
            if (targetPos == null) {
                targetPos = targetWorld.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawnPos);
            }
        }
        return new DimensionTransition(targetWorld, Vec3.atLowerCornerOf(targetPos).add(0.5, 0, 0.5), Vec3.ZERO, targetWorld.getRandom().nextFloat() * 360F, 0, DimensionTransition.DO_NOTHING);
    }

    @Nullable
    private static BlockPos findSafePos(ServerLevel world, BlockPos pos) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int offset = 0; offset <= SAFE_SEARCH_RANGE; offset++) {
            if (isSafe(world, mutable.set(pos.getX(), pos.getY() + offset, pos.getZ()))) {
                return mutable.immutable();
            }
            if (offset != 0 && isSafe(world, mutable.set(pos.getX(), pos.getY() - offset, pos.getZ()))) {
                return mutable.immutable();
            }
        }
        if (!world.dimensionType().hasCeiling()) {
            return world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);
        }
        return null;
    }

    private static boolean isSafe(ServerLevel world, BlockPos pos) {
        if (pos.getY() <= world.getMinBuildHeight() || pos.getY() + 1 >= world.getMaxBuildHeight()) {
            return false;
        }
        BlockPos headPos = pos.above();
        BlockPos groundPos = pos.below();
        BlockState feet = world.getBlockState(pos);
        BlockState head = world.getBlockState(headPos);
        BlockState ground = world.getBlockState(groundPos);
        return feet.getCollisionShape(world, pos).isEmpty() && head.getCollisionShape(world, headPos).isEmpty() && !feet.getFluidState().is(FluidTags.LAVA)
                && !head.getFluidState().is(FluidTags.LAVA) && !ground.getCollisionShape(world, groundPos).isEmpty();
    }

    private static void spawnVoidPlatform(ServerLevel world, BlockPos pos) {
        // Check if already exist
        if (world.getBlockState(pos).getBlock() != BlockInit.PORTAL) {
            BlockState platformBlock = BlockInit.VOID_STONE.defaultBlockState();
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            int radiusSquared = PLATFORM_RADIUS * PLATFORM_RADIUS;
            for (int x = -PLATFORM_RADIUS + 1; x < PLATFORM_RADIUS; x++) {
                for (int z = -PLATFORM_RADIUS + 1; z < PLATFORM_RADIUS; z++) {
                    if (x * x + z * z < radiusSquared && world.getBlockState(mutable.set(pos.getX() + x, pos.getY(), pos.getZ() + z)).isAir()) {
                        world.setBlock(mutable, platformBlock, Block.UPDATE_CLIENTS);
                    }
                }
            }
            // Pretty good centered
            world.setBlockAndUpdate(pos, BlockInit.PORTAL.defaultBlockState());
            if (world.getBlockEntity(pos) instanceof PortalBlockEntity portalBlockEntity) {
                portalBlockEntity.spawnBoss(world);
            } else {
                spawnVoidBoss(world, pos.above());
            }
        }
    }

    @Nullable
    public static VoidShadowEntity spawnVoidBoss(ServerLevel world, BlockPos spawnPos) {
        VoidShadowEntity voidShadowEntity = EntityInit.VOID_SHADOW.create(world);
        if (voidShadowEntity == null) {
            return null;
        }
        voidShadowEntity.setVoidMiddle(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        // Cast is required for compat
        ((Entity) voidShadowEntity).moveTo(spawnPos.above().north(40), 0.0F, 0.0F);
        return world.addFreshEntity(voidShadowEntity) ? voidShadowEntity : null;
    }
}
