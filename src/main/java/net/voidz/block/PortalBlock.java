package net.voidz.block;

import net.adventurez.entity.DragonEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.voidz.block.entity.PortalBlockEntity;
import net.voidz.dimension.VoidPlacementHandler;
import net.voidz.init.BlockInit;
import net.voidz.init.DimensionInit;
import org.jetbrains.annotations.Nullable;

public class PortalBlock extends Block implements BlockEntityProvider {

    public PortalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PortalBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, BlockInit.PORTAL_BLOCK_ENTITY, world.isClient() ? PortalBlockEntity::clientTick : PortalBlockEntity::serverTick);
    }

    @Override
    public ActionResult onUse(BlockState stateBlock, World world, BlockPos blockPos, PlayerEntity playerEntity, BlockHitResult blockHitResult) {
        if (world.isClient() || !(world instanceof ServerWorld serverWorld) || !(playerEntity instanceof ServerPlayerEntity serverPlayerEntity)) {
            return ActionResult.SUCCESS;
        }
        if (serverWorld.getRegistryKey() == DimensionInit.VOID_WORLD) {
            PortalBlockEntity portalBlockEntity = world.getBlockEntity(blockPos) instanceof PortalBlockEntity portal ? portal : null;
            boolean bossAlive = portalBlockEntity != null && portalBlockEntity.isBossAlive(serverWorld);
            if (bossAlive && !playerEntity.isCreative() && !playerEntity.isSpectator()) {
                playerEntity.sendMessage(Text.translatable("message.voidz.boss_alive"), false);
                return ActionResult.FAIL;
            }
            if (portalBlockEntity != null && !bossAlive) {
                portalBlockEntity.sendRespawnInfo(serverPlayerEntity);
            }
            TeleportTarget teleportTarget = VoidPlacementHandler.leave(serverPlayerEntity);
            if (playerEntity.hasVehicle() && playerEntity.getVehicle() instanceof DragonEntity dragonEntity && playerEntity.canUsePortals(true)) {
                playerEntity.stopRiding();
                dragonEntity.teleportTo(teleportTarget);
                return ActionResult.SUCCESS;
            }
            playerEntity.teleportTo(teleportTarget);
        } else {
            ServerWorld voidWorld = serverWorld.getServer().getWorld(DimensionInit.VOID_WORLD);
            if (voidWorld == null) {
                playerEntity.sendMessage(Text.translatable("message.voidz.missing_void_world"), false);
                return ActionResult.FAIL;
            }
            playerEntity.teleportTo(VoidPlacementHandler.enter(serverPlayerEntity, voidWorld, blockPos));
        }
        return ActionResult.SUCCESS;
    }

    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType,
            BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

}
