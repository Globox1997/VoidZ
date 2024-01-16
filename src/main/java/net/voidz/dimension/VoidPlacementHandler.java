package net.voidz.dimension;

import net.adventurez.entity.VoidShadowEntity;
import net.adventurez.init.EntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.voidz.access.ServerPlayerAccess;
import net.voidz.init.BlockInit;

public class VoidPlacementHandler {

    public static final BlockPos VOID_SPAWN_POS = new BlockPos(0, 100, 0);

    public static TeleportTarget enter(ServerPlayerEntity serverPlayerEntity, ServerWorld serverWorld, final BlockPos portalPos) {
        ((ServerPlayerAccess) serverPlayerEntity).setVoidPortingBlockPos(serverPlayerEntity.getBlockPos());
        spawnVoidPlatform(serverWorld, VOID_SPAWN_POS.down());
        return new TeleportTarget(Vec3d.of(VOID_SPAWN_POS).add(0.5, 0, 0.5), Vec3d.ZERO, 0, 0);
    }

    public static TeleportTarget leave(ServerPlayerEntity serverPlayerEntity, ServerWorld serverWorld, final BlockPos portalPos) {
        return new TeleportTarget(Vec3d.of(((ServerPlayerAccess) serverPlayerEntity).getVoidPortingBlockPos()).add(0.5, 0, 0.5), Vec3d.ZERO, serverWorld.getRandom().nextFloat() * 360F, 0);
    }

    private static void spawnVoidPlatform(ServerWorld world, BlockPos pos) {
        // Check if already exist
        if (world.getBlockState(pos).getBlock() != BlockInit.PORTAL) {
            BlockState platformBlock = BlockInit.VOID_STONE.getDefaultState();
            for (float u = 0.0F; u < Math.PI * 2; u += (float) Math.PI / 256F) {
                for (int i = 0; i < 40; i++) {
                    BlockPos blockPos = pos.add((int) (Math.sin(u) * i), 0, (int) (Math.cos(u) * i));
                    if (world.getBlockState(blockPos).isAir()) {
                        world.setBlockState(blockPos, platformBlock);
                    }
                }
            }
            // Pretty good centered
            world.setBlockState(pos, BlockInit.PORTAL.getDefaultState());
            spawnVoidBoss(world, pos.up());
        }
    }

    public static void spawnVoidBoss(ServerWorld world, BlockPos spawnPos) {
        VoidShadowEntity voidShadowEntity = EntityInit.VOID_SHADOW.create(world);
        voidShadowEntity.setVoidMiddle(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        // Cast is required for compat
        ((Entity) voidShadowEntity).refreshPositionAndAngles(spawnPos.up().north(40), 0.0F, 0.0F);
        world.spawnEntity(voidShadowEntity);
    }
}
