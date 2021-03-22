package net.voidz.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Tickable;
import net.voidz.init.BlockInit;

public class PortalBlockEntity extends BlockEntity implements Tickable {

    private float ticker;

    public PortalBlockEntity() {
        super(BlockInit.PORTAL_BLOCK_ENTITY);
    }

    @Override
    public void tick() {

        if (this.world.isClient) {
            ticker += (float) Math.PI / 128F;
            double angle = 2 * Math.PI * ticker;
            this.world.addParticle(ParticleTypes.PORTAL, (double) this.getPos().getX() + 0.5F + 1.5F * Math.sin(angle),
                    (double) this.getPos().getY() + 0.5F + Math.sin(angle) / 2.0F,
                    (double) this.getPos().getZ() + 0.5F + 1.5F * Math.cos(angle), 0.0D, 0.0D, 0.0D);
            this.world.addParticle(ParticleTypes.PORTAL, (double) this.getPos().getX() + 0.5F + 1.5F * Math.sin(angle),
                    (double) this.getPos().getY() + 0.5F + Math.sin(angle + Math.PI) / 2.0F,
                    (double) this.getPos().getZ() + 0.5F + 1.5F * Math.cos(angle), 0.0D, 0.0D, 0.0D);
        }

    }

}
