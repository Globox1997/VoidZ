package net.voidz.dimension;

import net.adventurez.entity.VoidShadowEntity;
import net.adventurez.init.EntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.voidz.init.BlockInit;

public class VoidPlacementHandler {
	public static TeleportTarget enter(ServerWorld serverWorld, final BlockPos portalPos) {
		BlockPos pos = enterVoid(serverWorld, portalPos);
		return new TeleportTarget(Vec3d.of(pos).add(0.5, 0, 0.5), Vec3d.ZERO, 0, 0);

	}

	public static TeleportTarget leave(ServerWorld serverWorld, final BlockPos portalPos) {
		BlockPos pos = leaveVoid(serverWorld, portalPos);
		return new TeleportTarget(Vec3d.of(pos).add(0.5, 0, 0.5), Vec3d.ZERO, 0, 0);
	}

	private static BlockPos enterVoid(ServerWorld serverWorld, BlockPos portalPos) {
		BlockPos spawnPos = new BlockPos(portalPos.getX(), 100, portalPos.getZ());
		spawnVoidPlatform(serverWorld, spawnPos.down());
		VoidShadowEntity voidShadowEntity = (VoidShadowEntity) EntityInit.VOID_SHADOW_ENTITY.create((World) serverWorld);
		voidShadowEntity.setVoidMiddle(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
		((Entity) voidShadowEntity).refreshPositionAndAngles(spawnPos.up().north(40), 0.0F, 0.0F);
		((ServerWorld) serverWorld).spawnEntity(voidShadowEntity);
		System.out.println("TEST");
		return spawnPos;
	}

	private static BlockPos leaveVoid(ServerWorld serverWorld, BlockPos portalPos) {
		return serverWorld.getTopPosition(Heightmap.Type.MOTION_BLOCKING, portalPos).up();
	}

	private static void spawnVoidPlatform(World world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock() != BlockInit.PORTAL_BLOCK) {
			BlockState platformBlock = BlockInit.VOID_BLOCK.getDefaultState();
			for (float u = 0.0F; u < Math.PI * 2; u += (float) Math.PI / 256F) {
				for (int i = 0; i < 40; i++) {
					world.setBlockState(pos.add(Math.sin(u) * i, 0, Math.cos(u) * i), platformBlock);
				}
			}
			// Pretty good centered
			world.setBlockState(pos, BlockInit.PORTAL_BLOCK.getDefaultState());

		}
	}
}
