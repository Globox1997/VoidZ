package net.voidz.block.entity;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.adventurez.entity.VoidShadowEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.voidz.dimension.VoidPlacementHandler;
import net.voidz.init.BlockInit;
import net.voidz.init.ConfigInit;
import net.voidz.init.DimensionInit;

public class PortalBlockEntity extends TheEndPortalBlockEntity {

    private static final String BOSS_TIME_KEY = "VoidShadowKilledTime";
    private static final String BOSS_UUID_KEY = "VoidShadowUuid";
    private static final String RESPAWN_DELAY_KEY = "RespawnDelay";
    private static final int RESPAWN_CHECK_INTERVAL = 20;
    private static final int NO_RESPAWN = -1;

    private float particleTicker;
    private int spawnTicker;

    public int bossTime;

    @Nullable
    private UUID bossUuid;

    private int syncedBossTime;
    private int respawnDelay = NO_RESPAWN;

    public PortalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockInit.PORTAL_BLOCK_ENTITY, pos, state);
    }

    public static void clientTick(Level world, BlockPos pos, BlockState state, PortalBlockEntity blockEntity) {
        blockEntity.tickClient();
    }

    public static void serverTick(Level world, BlockPos pos, BlockState state, PortalBlockEntity blockEntity) {
        blockEntity.tickServer((ServerLevel) world);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.bossTime = nbt.getInt(BOSS_TIME_KEY);
        this.syncedBossTime = this.bossTime;
        this.bossUuid = nbt.hasUUID(BOSS_UUID_KEY) ? nbt.getUUID(BOSS_UUID_KEY) : null;
        if (nbt.contains(RESPAWN_DELAY_KEY)) {
            this.respawnDelay = nbt.getInt(RESPAWN_DELAY_KEY);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.putInt(BOSS_TIME_KEY, this.bossTime);
        if (this.bossUuid != null) {
            nbt.putUUID(BOSS_UUID_KEY, this.bossUuid);
        }
    }

    @Override
    public CompoundTag getUpdateTag(Provider registryLookup) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(BOSS_TIME_KEY, this.bossTime);
        nbt.putInt(RESPAWN_DELAY_KEY, getConfiguredRespawnDelay());
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public boolean shouldRenderFace(Direction direction) {
        return true;
    }

    public boolean isBossAlive(ServerLevel world) {
        if (this.bossUuid != null) {
            Entity boss = world.getEntity(this.bossUuid);
            return boss != null && boss.isAlive();
        }
        if (this.bossTime == 0) {
            List<VoidShadowEntity> list = world.getEntitiesOfClass(VoidShadowEntity.class, new AABB(this.worldPosition).inflate(160D), EntitySelector.NO_SPECTATORS);
            if (!list.isEmpty()) {
                this.bossUuid = list.get(0).getUUID();
                this.setChanged();
                return true;
            }
        }
        return false;
    }

    public void spawnBoss(ServerLevel world) {
        VoidShadowEntity boss = VoidPlacementHandler.spawnVoidBoss(world, this.worldPosition.above());
        if (boss != null) {
            this.bossUuid = boss.getUUID();
            this.bossTime = 0;
            this.setChanged();
        }
    }

    public void sendRespawnInfo(ServerPlayer player) {
        int delay = getConfiguredRespawnDelay();
        if (delay == NO_RESPAWN || this.bossTime == 0) {
            return;
        }
        int remaining = delay - ticksSinceBossDeath();
        if (remaining > 0) {
            player.displayClientMessage(Component.translatable("message.voidz.boss_respawn", formatTicks(remaining)), false);
        } else {
            player.displayClientMessage(Component.translatable("message.voidz.boss_respawn_soon"), false);
        }
    }

    private int ticksSinceBossDeath() {
        return (int) this.level.getGameTime() - this.bossTime;
    }

    private int getConfiguredRespawnDelay() {
        if (this.level == null || this.level.dimension() != DimensionInit.VOID_WORLD || !ConfigInit.CONFIG.allow_boss_respawn) {
            return NO_RESPAWN;
        }
        return Math.max(0, ConfigInit.CONFIG.boss_respawn_time);
    }

    private void tickServer(ServerLevel world) {
        int delay = getConfiguredRespawnDelay();
        if (this.bossTime != this.syncedBossTime || delay != this.respawnDelay) {
            if (this.bossTime != this.syncedBossTime) {
                if (this.bossTime != 0) {
                    this.bossUuid = null;
                }
                this.syncedBossTime = this.bossTime;
                this.setChanged();
            }
            this.respawnDelay = delay;
            world.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }

        if (++this.spawnTicker % RESPAWN_CHECK_INTERVAL != 0) {
            return;
        }
        if (delay != NO_RESPAWN && this.bossTime != 0 && ticksSinceBossDeath() >= delay && !isBossAlive(world)) {
            world.playSound(null, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 2.0F, 1.0F);
            spawnBoss(world);
        }
    }

    private void tickClient() {
        this.particleTicker = (this.particleTicker + (float) Math.PI / 128F) % 1.0F;
        double angle = 2 * Math.PI * this.particleTicker;
        double centerX = this.worldPosition.getX() + 0.5D;
        double centerY = this.worldPosition.getY() + 0.5D;
        double centerZ = this.worldPosition.getZ() + 0.5D;
        this.level.addParticle(ParticleTypes.PORTAL, centerX + 1.5D * Math.sin(angle), centerY + Math.sin(angle) / 2.0D, centerZ + 1.5D * Math.cos(angle), 0.0D, 0.0D, 0.0D);
        this.level.addParticle(ParticleTypes.PORTAL, centerX + 1.5D * Math.sin(angle), centerY + Math.sin(angle + Math.PI) / 2.0D, centerZ + 1.5D * Math.cos(angle), 0.0D, 0.0D, 0.0D);

        if (this.bossTime != 0 && this.respawnDelay != NO_RESPAWN) {
            spawnRespawnChargeParticles();
        }
    }

    private void spawnRespawnChargeParticles() {
        float progress = this.respawnDelay <= 0 ? 1.0F : Mth.clamp((float) ticksSinceBossDeath() / this.respawnDelay, 0.0F, 1.0F);
        RandomSource random = this.level.getRandom();
        if (random.nextFloat() > 0.15F + 0.85F * progress) {
            return;
        }
        double centerX = this.worldPosition.getX() + 0.5D;
        double centerY = this.worldPosition.getY() + 1.0D;
        double centerZ = this.worldPosition.getZ() + 0.5D;
        int count = 1 + (int) (progress * 3.0F);
        for (int i = 0; i < count; i++) {
            double distance = 2.0D + random.nextDouble() * (1.0D + 3.0D * progress);
            double yaw = random.nextDouble() * Math.PI * 2.0D;
            double pitch = (random.nextDouble() - 0.5D) * Math.PI;
            this.level.addParticle(ParticleTypes.PORTAL, centerX, centerY, centerZ, Math.cos(yaw) * Math.cos(pitch) * distance, Math.sin(pitch) * distance,
                    Math.sin(yaw) * Math.cos(pitch) * distance);
        }
        if (progress >= 1.0F || this.respawnDelay - ticksSinceBossDeath() <= 100) {
            this.level.addParticle(ParticleTypes.REVERSE_PORTAL, centerX + (random.nextDouble() - 0.5D), centerY, centerZ + (random.nextDouble() - 0.5D), 0.0D, 0.08D + random.nextDouble() * 0.1D, 0.0D);
        }
    }

    private static String formatTicks(int ticks) {
        int seconds = Mth.positiveCeilDiv(ticks, 20);
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds %= 60;
        return hours > 0 ? String.format("%d:%02d:%02d", hours, minutes, seconds) : String.format("%d:%02d", minutes, seconds);
    }

}
