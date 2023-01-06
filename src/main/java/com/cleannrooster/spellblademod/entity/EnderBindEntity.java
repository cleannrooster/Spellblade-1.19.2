package com.cleannrooster.spellblademod.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class EnderBindEntity extends AbstractArrow implements ItemSupplier {
    LivingEntity target;
    protected EnderBindEntity(EntityType<? extends EnderBindEntity> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    public EnderBindEntity(EntityType<? extends EnderBindEntity> p_37248_, Level p_37249_, LivingEntity target) {
        super(p_37248_, p_37249_);
        this.target = target;
    }

    @Override
    public ItemStack getItem() {
        return Items.ENDER_EYE.getDefaultInstance();
    }

    @Override
    protected void onHit(HitResult p_37260_) {
    }


    @Override
    public void tick() {
        this.setNoGravity(true);
        this.setNoPhysics(true);
        if(this.firstTick){
            this.playSound(SoundEvents.ENDERMAN_TELEPORT);
        }
        super.tick();
        if(this.target != null) {
            if (this.tickCount > 160 && this.level.dimension() == this.target.level.dimension()) {
                this.target.playSound(SoundEvents.ENDERMAN_TELEPORT);
                this.target.teleportTo(this.getX(),this.getY(),this.getZ());
                this.playSound(SoundEvents.ENDERMAN_TELEPORT);
                this.target.playSound(SoundEvents.ENDERMAN_TELEPORT);
                this.discard();
            }
            if(this.tickCount % Math.max(1,Math.ceil(5- (int) (5*this.tickCount/160) ))== 0 && !this.level.isClientSide()){
                BindProjectile proj = new BindProjectile(ModEntities.BINDPROJ.get(),this.level,this.target, this);
                proj.setPos(this.position());
                this.level.addFreshEntity(proj);

            }
        }
        else{
            if(!this.level.isClientSide()) {

                this.discard();
            }
        }
        if(this.tickCount > 160 && !this.level.isClientSide()){
            this.discard();
        }

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
