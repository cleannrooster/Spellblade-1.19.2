package com.cleannrooster.spellblademod.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BindProjectile extends AbstractArrow implements ItemSupplier {
    LivingEntity target;
    EnderBindEntity entity;

    protected BindProjectile(EntityType<? extends BindProjectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    protected BindProjectile(EntityType<? extends BindProjectile> p_37248_, Level p_37249_, LivingEntity target,EnderBindEntity owner) {
        super(p_37248_, p_37249_);
        this.target = target;
        this.entity = owner;
    }


    @Override
    public ItemStack getItem() {
        return Items.AIR.getDefaultInstance();
    }

    @Override
    protected void onHit(HitResult p_37260_) {
    }

    @Override
    protected void onHitBlock(BlockHitResult p_36755_) {

    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {

    }

    @Override
    public void tick() {
        this.setNoGravity(true);
        this.setNoPhysics(true);

        if(this.entity != null && !this.entity.isAlive() && !this.level.isClientSide()){
            this.discard();
        }
        super.tick();
        if(this.target != null) {
            Vec3 vec3 = this.target.getBoundingBox().getCenter().subtract(this.position());

            double d0 = 0.05D * (double)2;
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
            this.setDeltaMovement(this.getDeltaMovement().normalize().multiply(10F / 20F, 10F / 20F, 10F / 20F));
            if(this.getBoundingBox().expandTowards(this.getDeltaMovement()).intersects(this.target.getBoundingBox())) {
                this.discard();
            }
        }
        else{
            if(!this.level.isClientSide()) {
                this.discard();
            }
        }
        this.level.addParticle( ParticleTypes.ELECTRIC_SPARK, true, this.getX(), this.getY(), this.getZ(), this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);




    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }
    @Override
    protected boolean tryPickup(Player p_150121_) {
        return false;
    }
}
