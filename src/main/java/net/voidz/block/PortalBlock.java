package net.voidz.block;

import net.adventurez.entity.DragonEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.BlockHitResult;
import net.voidz.block.entity.PortalBlockEntity;
import net.voidz.dimension.VoidPlacementHandler;
import net.voidz.init.BlockInit;
import net.voidz.init.DimensionInit;
import org.jetbrains.annotations.Nullable;

public class PortalBlock extends Block implements EntityBlock {

    public PortalBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PortalBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, BlockInit.PORTAL_BLOCK_ENTITY, world.isClientSide() ? PortalBlockEntity::clientTick : PortalBlockEntity::serverTick);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState stateBlock, Level world, BlockPos blockPos, Player playerEntity, BlockHitResult blockHitResult) {
        if (world.isClientSide() || !(world instanceof ServerLevel serverWorld) || !(playerEntity instanceof ServerPlayer serverPlayerEntity)) {
            return InteractionResult.SUCCESS;
        }
        if (serverWorld.dimension() == DimensionInit.VOID_WORLD) {
            PortalBlockEntity portalBlockEntity = world.getBlockEntity(blockPos) instanceof PortalBlockEntity portal ? portal : null;
            boolean bossAlive = portalBlockEntity != null && portalBlockEntity.isBossAlive(serverWorld);
            if (bossAlive && !playerEntity.isCreative() && !playerEntity.isSpectator()) {
                playerEntity.displayClientMessage(Component.translatable("message.voidz.boss_alive"), false);
                return InteractionResult.FAIL;
            }
            if (portalBlockEntity != null && !bossAlive) {
                portalBlockEntity.sendRespawnInfo(serverPlayerEntity);
            }
            DimensionTransition teleportTarget = VoidPlacementHandler.leave(serverPlayerEntity);
            if (playerEntity.isPassenger() && playerEntity.getVehicle() instanceof DragonEntity dragonEntity && playerEntity.canUsePortal(true)) {
                playerEntity.stopRiding();
                dragonEntity.changeDimension(teleportTarget);
                return InteractionResult.SUCCESS;
            }
            playerEntity.changeDimension(teleportTarget);
        } else {
            ServerLevel voidWorld = serverWorld.getServer().getLevel(DimensionInit.VOID_WORLD);
            if (voidWorld == null) {
                playerEntity.displayClientMessage(Component.translatable("message.voidz.missing_void_world"), false);
                return InteractionResult.FAIL;
            }
            playerEntity.changeDimension(VoidPlacementHandler.enter(serverPlayerEntity, voidWorld, blockPos));
        }
        return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType,
            BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

}
