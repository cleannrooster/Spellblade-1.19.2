package com.cleannrooster.spellblademod.entity;

import com.cleannrooster.spellblademod.items.FriendshipBracelet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class AmethystEntity extends AbstractArrow implements ItemSupplier {
    public LivingEntity target;
    public double damage = 4;

    public AmethystEntity(EntityType<? extends AmethystEntity> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
        this.setNoGravity(true);

    }
    public AmethystEntity(EntityType<? extends AmethystEntity> p_36721_, Level p_36722_,Player player) {
        super(p_36721_, p_36722_);
        this.setOwner(player);
        Vec3 vec3 = player.getViewVector(0);
        this.setNoGravity(true);

        double d0 = vec3.horizontalDistance();
        this.setYRot(((float) (Mth.atan2(player.getViewVector(0).x, player.getViewVector(0).z) * (double) (180F / (float) Math.PI))));
        this.setXRot((float) (Mth.atan2(player.getViewVector(0).y, d0) * (double) (180F / (float) Math.PI)));
        this.yRotO = ((float) (Mth.atan2(player.getViewVector(0).x, player.getViewVector(0).z) * (double) (180F / (float) Math.PI)));
        this.xRotO = (float) (Mth.atan2(player.getViewVector(0).y, d0) * (double) (180F / (float) Math.PI));

    }

    @Override
    public void tick() {
        if(this.tickCount > 200 && !this.getLevel().isClientSide()){
            this.discard();
        }
        if(this.firstTick){
            this.playSound(SoundEvents.AMETHYST_BLOCK_CHIME);
        }

        if(this.tickCount < 20){
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.8,0.8,0.8));
        }
        if(this.getOwner() instanceof Player player) {
            if (this.tickCount == 20) {
                if (!this.getLevel().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(16), asdf -> FriendshipBracelet.PlayerFriendshipPredicate(player, asdf)).isEmpty()) {
                    LivingEntity entity = this.getLevel().getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(), player, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(16));
                    if (entity != null) {
                        this.target = entity;
                        this.setDeltaMovement(entity.position().add(new Vec3(0, entity.getBoundingBox().getYsize() / 2, 0)).subtract(this.getEyePosition()).normalize().multiply(3, 3, 3));
                    }
                }
                this.setDeltaMovement(new Vec3(-0.5+this.random.nextDouble(),-0.5+this.random.nextDouble(),-0.5+this.random.nextDouble()).normalize().multiply(3,3,3));

            }
        }
        if(this.target != null&& !this.target.isDeadOrDying()  && this.tickCount > 20){
            this.setDeltaMovement(this.getDeltaMovement().add((this.target.position().add(new Vec3(0,this.target.getBoundingBox().getYsize()/2,0).subtract(this.position()))).normalize().multiply(4,4,4)).normalize());
        }
        if(this.target != null && this.target.isDeadOrDying() && this.tickCount > 20) {
            this.setNoGravity(false);
        }
            if(this.tickCount > 20) {
            super.tick();
        }
        else{
            super.baseTick();
            if(!this.inGround) {
                this.setPos(this.position().add(this.getDeltaMovement()));
                this.setXRot(this.getXRot() + 72);
            }
            Vec3 vec3 = this.getDeltaMovement();
            this.inGroundTime = 0;
            Vec3 vec32 = this.position();
            Vec3 vec33 = vec32.add(vec3);
            HitResult hitresult = this.level.clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (hitresult.getType() != HitResult.Type.MISS) {
                vec33 = hitresult.getLocation();
            }
            EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
            if (entityhitresult != null) {
                hitresult = entityhitresult;
            }

            if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult)hitresult).getEntity();
                Entity entity1 = this.getOwner();
                if (entity instanceof Player && entity1 instanceof Player && !((Player)entity1).canHarmPlayer((Player)entity)) {
                    hitresult = null;
                    entityhitresult = null;
                }
            }

            if (hitresult != null && hitresult.getType() != HitResult.Type.MISS  && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
                this.hasImpulse = true;
            }

        }
    }

    @Override
    protected boolean tryPickup(Player p_150121_) {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {
        if(p_36757_.getEntity() instanceof LivingEntity living){
            living.invulnerableTime = 0;
        }
        p_36757_.getEntity().hurt(IndirectEntityDamageSource.arrow(this,this.getOwner()), (float) this.damage);
        this.playSound(SoundEvents.AMETHYST_CLUSTER_BREAK);
        for(int ii = 0; ii < 10; ii++){
            this.getLevel().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AMETHYST_BLOCK.defaultBlockState()), this.getX() + -1 + 2 * this.random.nextDouble(), this.getY() - 1 + 2 * this.random.nextDouble(), this.getZ() - 1 + 2 * this.random.nextDouble(), 0, 0, 0);
        }
        this.discard();

    }



    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.AMETHYST_BLOCK_HIT;
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return Items.AMETHYST_SHARD.getDefaultInstance();
    }
}
