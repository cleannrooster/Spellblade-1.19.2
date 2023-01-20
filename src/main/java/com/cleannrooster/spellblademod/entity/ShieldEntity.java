package com.cleannrooster.spellblademod.entity;

import com.cleannrooster.spellblademod.items.Spellblade;
import net.minecraft.core.Position;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ShieldEntity extends AbstractHurtingProjectile {


    public ShieldEntity(EntityType<? extends ShieldEntity> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    @Override
    protected void onHit(HitResult p_37260_) {
        return;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource p_36839_, float p_36840_) {
        return false;
    }

    @Override
    public void tick() {
        if(!this.getLevel().isClientSide() &&(this.getOwner() == null || (this.getOwner() != null && !this.getOwner().isAlive()) || (this.getOwner() != null && !(this.getOwner() instanceof Player player && player.getUseItem().getItem() instanceof Spellblade)))){
            this.discard();
        }
        if(this.getOwner() !=  null && this.getOwner() instanceof Player player) {
            this.xOld = this.getX();
            this.yOld = this.getY();
            this.zOld = this.getZ();
            this.xo = this.getX();
            this.yo = this.getY();
            this.zo = this.getZ();
            this.setPos(player.position().add(player.getLookAngle().multiply(1, 1, 1)).add(0,0.0625*3,0));
        }
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AIR.defaultBlockState());
    }
}
