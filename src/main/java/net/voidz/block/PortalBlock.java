package net.voidz.block;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.voidz.block.entity.PortalBlockEntity;
import net.voidz.dimension.VoidPlacementHandler;
import net.voidz.init.DimensionInit;

public class PortalBlock extends Block implements BlockEntityProvider {
	public PortalBlock() {
		super(Settings.of(Material.AIR));
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new PortalBlockEntity();
	}

	@Override
	public ActionResult onUse(BlockState stateBlock, World world, BlockPos blockPos, PlayerEntity playerEntity,
			Hand hand, BlockHitResult blockHitResult) {
		if (!world.isClient) {
			ServerWorld serverWorld = (ServerWorld) playerEntity.getEntityWorld();
			if (serverWorld.getRegistryKey() == DimensionInit.VOID_WORLD) {
				ServerWorld overworld = serverWorld.getServer().getWorld(World.OVERWORLD);
				FabricDimensions.teleport(playerEntity, overworld, VoidPlacementHandler.leave(overworld, blockPos));
			} else {
				ServerWorld voidWorld = serverWorld.getServer().getWorld(DimensionInit.VOID_WORLD);
				if (voidWorld == null) {
					playerEntity.sendMessage(new LiteralText("Failed to find void world, was it registered?"), false);
					return ActionResult.FAIL;
				}
				FabricDimensions.teleport(playerEntity, voidWorld, VoidPlacementHandler.enter(voidWorld, blockPos));
			}
		}
		return ActionResult.SUCCESS;
	}

}
