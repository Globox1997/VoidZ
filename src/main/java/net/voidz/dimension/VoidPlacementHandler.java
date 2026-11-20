package net.voidz.dimension;

import org.jetbrains.annotations.Nullable;

import net.adventurez.entity.VoidShadowEntity;
import net.adventurez.init.EntityInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.TeleportTarget;
import net.voidz.access.ServerPlayerAccess;
import net.voidz.block.entity.PortalBlockEntity;
import net.voidz.init.BlockInit;
import net.voidz.init.DimensionInit;

public class VoidPlacementHandler {

    public static final BlockPos VOID_SPAWN_POS = new BlockPos(0, 100, 0);
    private static final int PLATFORM_RADIUS = 40;
    private static final int SAFE_SEARCH_RANGE = 16;

    public static TeleportTarget enter(ServerPlayerEntity serverPlayerEntity, ServerWorld serverWorld, final BlockPos portalPos) {
        ((ServerPlayerAccess) serverPlayerEntity).setVoidReturnPos(GlobalPos.create(serverPlayerEntity.getWorld().getRegistryKey(), serverPlayerEntity.getBlockPos()));
        spawnVoidPlatform(serverWorld, VOID_SPAWN_POS.down());
        return new TeleportTarget(serverWorld, Vec3d.of(VOID_SPAWN_POS).add(0.5, 0, 0.5), Vec3d.ZERO, 0, 0, TeleportTarget.NO_OP);
    }

    public static TeleportTarget leave(ServerPlayerEntity serverPlayerEntity) {
        MinecraftServer server = serverPlayerEntity.getServer();
        GlobalPos returnPos = ((ServerPlayerAccess) serverPlayerEntity).getVoidReturnPos();

        ServerWorld targetWorld = null;
        BlockPos targetPos = null;
        if (returnPos != null && returnPos.dimension() != DimensionInit.VOID_WORLD) {
            targetWorld = server.getWorld(returnPos.dimension());
            if (targetWorld != null) {
                targetPos = findSafePos(targetWorld, returnPos.pos());
            }
        }
        if (targetPos == null) {
            targetWorld = server.getOverworld();
            BlockPos spawnPos = targetWorld.getSpawnPos();
            targetPos = findSafePos(targetWorld, spawnPos);
            if (targetPos == null) {
                targetPos = targetWorld.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, spawnPos);
            }
        }
        return new TeleportTarget(targetWorld, Vec3d.of(targetPos).add(0.5, 0, 0.5), Vec3d.ZERO, targetWorld.getRandom().nextFloat() * 360F, 0, TeleportTarget.NO_OP);
    }

    @Nullable
    private static BlockPos findSafePos(ServerWorld world, BlockPos pos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int offset = 0; offset <= SAFE_SEARCH_RANGE; offset++) {
            if (isSafe(world, mutable.set(pos.getX(), pos.getY() + offset, pos.getZ()))) {
                return mutable.toImmutable();
            }
            if (offset != 0 && isSafe(world, mutable.set(pos.getX(), pos.getY() - offset, pos.getZ()))) {
                return mutable.toImmutable();
            }
        }
        if (!world.getDimension().hasCeiling()) {
            return world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos);
        }
        return null;
    }

    private static boolean isSafe(ServerWorld world, BlockPos pos) {
        if (pos.getY() <= world.getBottomY() || pos.getY() + 1 >= world.getTopY()) {
            return false;
        }
        BlockPos headPos = pos.up();
        BlockPos groundPos = pos.down();
        BlockState feet = world.getBlockState(pos);
        BlockState head = world.getBlockState(headPos);
        BlockState ground = world.getBlockState(groundPos);
        return feet.getCollisionShape(world, pos).isEmpty() && head.getCollisionShape(world, headPos).isEmpty() && !feet.getFluidState().isIn(FluidTags.LAVA)
                && !head.getFluidState().isIn(FluidTags.LAVA) && !ground.getCollisionShape(world, groundPos).isEmpty();
    }

    private static void spawnVoidPlatform(ServerWorld world, BlockPos pos) {
        // Check if already exist
        if (world.getBlockState(pos).getBlock() != BlockInit.PORTAL) {
            BlockState platformBlock = BlockInit.VOID_STONE.getDefaultState();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            int radiusSquared = PLATFORM_RADIUS * PLATFORM_RADIUS;
            for (int x = -PLATFORM_RADIUS + 1; x < PLATFORM_RADIUS; x++) {
                for (int z = -PLATFORM_RADIUS + 1; z < PLATFORM_RADIUS; z++) {
                    if (x * x + z * z < radiusSquared && world.getBlockState(mutable.set(pos.getX() + x, pos.getY(), pos.getZ() + z)).isAir()) {
                        world.setBlockState(mutable, platformBlock, Block.NOTIFY_LISTENERS);
                    }
                }
            }
            // Pretty good centered
            world.setBlockState(pos, BlockInit.PORTAL.getDefaultState());
            if (world.getBlockEntity(pos) instanceof PortalBlockEntity portalBlockEntity) {
                portalBlockEntity.spawnBoss(world);
            } else {
                spawnVoidBoss(world, pos.up());
            }
        }
    }

    @Nullable
    public static VoidShadowEntity spawnVoidBoss(ServerWorld world, BlockPos spawnPos) {
        VoidShadowEntity voidShadowEntity = EntityInit.VOID_SHADOW.create(world);
        if (voidShadowEntity == null) {
            return null;
        }
        voidShadowEntity.setVoidMiddle(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        // Cast is required for compat
        ((Entity) voidShadowEntity).refreshPositionAndAngles(spawnPos.up().north(40), 0.0F, 0.0F);
        return world.spawnEntity(voidShadowEntity) ? voidShadowEntity : null;
    }
}
