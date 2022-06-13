package net.voidz.block;

import java.util.List;

import net.adventurez.entity.VoidShadowEntity;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.voidz.block.entity.PortalBlockEntity;
import net.voidz.dimension.VoidPlacementHandler;
import net.voidz.init.BlockInit;
import net.voidz.init.ConfigInit;
import net.voidz.init.DimensionInit;
import org.jetbrains.annotations.Nullable;

public class PortalBlock extends Block implements BlockEntityProvider {
    public PortalBlock() {
        super(Settings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing());
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PortalBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, BlockInit.PORTAL_BLOCK_ENTITY, world.isClient ? PortalBlockEntity::clientTick : PortalBlockEntity::serverTick);
    }

    @Override
    public ActionResult onUse(BlockState stateBlock, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
        if (!world.isClient) {
            ServerWorld serverWorld = (ServerWorld) playerEntity.getEntityWorld();
            if (serverWorld.getRegistryKey() == DimensionInit.VOID_WORLD) {
                Box box = new Box(blockPos);
                List<VoidShadowEntity> list = world.getEntitiesByClass(VoidShadowEntity.class, box.expand(160D), EntityPredicates.EXCEPT_SPECTATOR);
                if (!playerEntity.isCreative() && !playerEntity.isSpectator() && !list.isEmpty()) {
                    playerEntity.sendMessage(Text.literal("Void Shadow: You can't escape the depths of this world as long as I am alive!"), false);
                    return ActionResult.FAIL;
                }
                if (ConfigInit.CONFIG.allow_boss_respawn && list.isEmpty()) {
                    PortalBlockEntity portalBlockEntity = (PortalBlockEntity) world.getBlockEntity(blockPos);
                    if (portalBlockEntity != null && portalBlockEntity.bossTime != 0) {
                        int timer = ConfigInit.CONFIG.boss_respawn_time - ((int) world.getLevelProperties().getTime() - portalBlockEntity.bossTime);
                        playerEntity.sendMessage(Text.literal("The Void Shadow will respawn in " + timer + " ticks"), false);
                    }
                }
                ServerWorld oldWorld = serverWorld.getServer().getOverworld();
                if (oldWorld != null) {
                    FabricDimensions.teleport(playerEntity, oldWorld, VoidPlacementHandler.leave((ServerPlayerEntity) playerEntity, oldWorld, blockPos));
                    return ActionResult.FAIL;
                }
            } else {
                ServerWorld voidWorld = serverWorld.getServer().getWorld(DimensionInit.VOID_WORLD);
                if (voidWorld == null) {
                    playerEntity.sendMessage(Text.literal("Failed to find void world, was it registered?"), false);
                    return ActionResult.FAIL;
                }
                FabricDimensions.teleport(playerEntity, voidWorld, VoidPlacementHandler.enter((ServerPlayerEntity) playerEntity, voidWorld, blockPos));
            }
        }
        return ActionResult.SUCCESS;
    }

    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType,
            BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

}
