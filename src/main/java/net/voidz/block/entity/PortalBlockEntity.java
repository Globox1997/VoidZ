package net.voidz.block.entity;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.adventurez.entity.VoidShadowEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.voidz.dimension.VoidPlacementHandler;
import net.voidz.init.BlockInit;
import net.voidz.init.ConfigInit;
import net.voidz.init.DimensionInit;

public class PortalBlockEntity extends EndPortalBlockEntity {

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

    public static void clientTick(World world, BlockPos pos, BlockState state, PortalBlockEntity blockEntity) {
        blockEntity.tickClient();
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, PortalBlockEntity blockEntity) {
        blockEntity.tickServer((ServerWorld) world);
    }

    @Override
    protected void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.bossTime = nbt.getInt(BOSS_TIME_KEY);
        this.syncedBossTime = this.bossTime;
        this.bossUuid = nbt.containsUuid(BOSS_UUID_KEY) ? nbt.getUuid(BOSS_UUID_KEY) : null;
        if (nbt.contains(RESPAWN_DELAY_KEY)) {
            this.respawnDelay = nbt.getInt(RESPAWN_DELAY_KEY);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt(BOSS_TIME_KEY, this.bossTime);
        if (this.bossUuid != null) {
            nbt.putUuid(BOSS_UUID_KEY, this.bossUuid);
        }
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(WrapperLookup registryLookup) {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt(BOSS_TIME_KEY, this.bossTime);
        nbt.putInt(RESPAWN_DELAY_KEY, getConfiguredRespawnDelay());
        return nbt;
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public boolean shouldDrawSide(Direction direction) {
        return true;
    }

    public boolean isBossAlive(ServerWorld world) {
        if (this.bossUuid != null) {
            Entity boss = world.getEntity(this.bossUuid);
            return boss != null && boss.isAlive();
        }
        if (this.bossTime == 0) {
            List<VoidShadowEntity> list = world.getEntitiesByClass(VoidShadowEntity.class, new Box(this.pos).expand(160D), EntityPredicates.EXCEPT_SPECTATOR);
            if (!list.isEmpty()) {
                this.bossUuid = list.get(0).getUuid();
                this.markDirty();
                return true;
            }
        }
        return false;
    }

    public void spawnBoss(ServerWorld world) {
        VoidShadowEntity boss = VoidPlacementHandler.spawnVoidBoss(world, this.pos.up());
        if (boss != null) {
            this.bossUuid = boss.getUuid();
            this.bossTime = 0;
            this.markDirty();
        }
    }

    public void sendRespawnInfo(ServerPlayerEntity player) {
        int delay = getConfiguredRespawnDelay();
        if (delay == NO_RESPAWN || this.bossTime == 0) {
            return;
        }
        int remaining = delay - ticksSinceBossDeath();
        if (remaining > 0) {
            player.sendMessage(Text.translatable("message.voidz.boss_respawn", formatTicks(remaining)), false);
        } else {
            player.sendMessage(Text.translatable("message.voidz.boss_respawn_soon"), false);
        }
    }

    private int ticksSinceBossDeath() {
        return (int) this.world.getTime() - this.bossTime;
    }

    private int getConfiguredRespawnDelay() {
        if (this.world == null || this.world.getRegistryKey() != DimensionInit.VOID_WORLD || !ConfigInit.CONFIG.allow_boss_respawn) {
            return NO_RESPAWN;
        }
        return Math.max(0, ConfigInit.CONFIG.boss_respawn_time);
    }

    private void tickServer(ServerWorld world) {
        int delay = getConfiguredRespawnDelay();
        if (this.bossTime != this.syncedBossTime || delay != this.respawnDelay) {
            if (this.bossTime != this.syncedBossTime) {
                if (this.bossTime != 0) {
                    this.bossUuid = null;
                }
                this.syncedBossTime = this.bossTime;
                this.markDirty();
            }
            this.respawnDelay = delay;
            world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
        }

        if (++this.spawnTicker % RESPAWN_CHECK_INTERVAL != 0) {
            return;
        }
        if (delay != NO_RESPAWN && this.bossTime != 0 && ticksSinceBossDeath() >= delay && !isBossAlive(world)) {
            world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.HOSTILE, 2.0F, 1.0F);
            spawnBoss(world);
        }
    }

    private void tickClient() {
        this.particleTicker = (this.particleTicker + (float) Math.PI / 128F) % 1.0F;
        double angle = 2 * Math.PI * this.particleTicker;
        double centerX = this.pos.getX() + 0.5D;
        double centerY = this.pos.getY() + 0.5D;
        double centerZ = this.pos.getZ() + 0.5D;
        this.world.addParticle(ParticleTypes.PORTAL, centerX + 1.5D * Math.sin(angle), centerY + Math.sin(angle) / 2.0D, centerZ + 1.5D * Math.cos(angle), 0.0D, 0.0D, 0.0D);
        this.world.addParticle(ParticleTypes.PORTAL, centerX + 1.5D * Math.sin(angle), centerY + Math.sin(angle + Math.PI) / 2.0D, centerZ + 1.5D * Math.cos(angle), 0.0D, 0.0D, 0.0D);

        if (this.bossTime != 0 && this.respawnDelay != NO_RESPAWN) {
            spawnRespawnChargeParticles();
        }
    }

    private void spawnRespawnChargeParticles() {
        float progress = this.respawnDelay <= 0 ? 1.0F : MathHelper.clamp((float) ticksSinceBossDeath() / this.respawnDelay, 0.0F, 1.0F);
        Random random = this.world.getRandom();
        if (random.nextFloat() > 0.15F + 0.85F * progress) {
            return;
        }
        double centerX = this.pos.getX() + 0.5D;
        double centerY = this.pos.getY() + 1.0D;
        double centerZ = this.pos.getZ() + 0.5D;
        int count = 1 + (int) (progress * 3.0F);
        for (int i = 0; i < count; i++) {
            double distance = 2.0D + random.nextDouble() * (1.0D + 3.0D * progress);
            double yaw = random.nextDouble() * Math.PI * 2.0D;
            double pitch = (random.nextDouble() - 0.5D) * Math.PI;
            this.world.addParticle(ParticleTypes.PORTAL, centerX, centerY, centerZ, Math.cos(yaw) * Math.cos(pitch) * distance, Math.sin(pitch) * distance,
                    Math.sin(yaw) * Math.cos(pitch) * distance);
        }
        if (progress >= 1.0F || this.respawnDelay - ticksSinceBossDeath() <= 100) {
            this.world.addParticle(ParticleTypes.REVERSE_PORTAL, centerX + (random.nextDouble() - 0.5D), centerY, centerZ + (random.nextDouble() - 0.5D), 0.0D, 0.08D + random.nextDouble() * 0.1D, 0.0D);
        }
    }

    private static String formatTicks(int ticks) {
        int seconds = MathHelper.ceilDiv(ticks, 20);
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds %= 60;
        return hours > 0 ? String.format("%d:%02d:%02d", hours, minutes, seconds) : String.format("%d:%02d", minutes, seconds);
    }

}
