package com.cleannrooster.spellblademod.entity;

import com.cleannrooster.spellblademod.items.FriendshipBracelet;
import com.cleannrooster.spellblademod.items.ModItems;
import com.cleannrooster.spellblademod.items.ParticlePacket2;
import com.cleannrooster.spellblademod.setup.Messages;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EndersEyeEntity extends Projectile implements ItemSupplier {

    public List<Entity> blacklist;
    public LivingEntity target;
    public Vec3 pos1;
    public float damage = 1;

    public EndersEyeEntity(EntityType<? extends EndersEyeEntity> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }
    @Override
    protected void defineSynchedData() {

    }
    double lastx = 0;
    double lasty = 0;
    double lastz = 0;
    @Override
    public void tick() {
        if(firstTick){
            SoundEvent soundEvent = SoundEvents.ENDERMAN_TELEPORT;
            this.playSound(soundEvent, 0.25F, 1F);
        }
        super.tick();
        this.noPhysics = true;
        if (this.tickCount > 160 && !this.getLevel().isClientSide()){
            this.discard();
        }

        if(this.getOwner()!=null){
            if (this.distanceTo(this.getOwner()) > 32 && !this.getLevel().isClientSide()){
                this.discard();
            }
            if(this.getOwner().isAlive() && this.getOwner() instanceof Player player) {

                if (this.lastx == 0 && this.lasty == 0 && this.lastz == 0 && this.pos1 != null) {
                    this.lastx = this.pos1.x;
                    this.lasty = this.pos1.y;
                    this.lastz = this.pos1.z;
                }


                if (target == null || !target.isAlive() || target.isDeadOrDying()) {
                    List<LivingEntity> entities = this.getLevel().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue() * 1.5), livingEntity -> {
                        return FriendshipBracelet.PlayerFriendshipPredicate((Player) this.getOwner(), livingEntity);
                    });
                    entities.removeIf(livingEntity -> !livingEntity.hasLineOfSight(this));
                    entities.removeIf(livingEntity -> livingEntity == this.getOwner() || livingEntity.isDeadOrDying());
                    entities.removeIf(livingEntity -> {
                        if (livingEntity instanceof InvisiVex vex) {
                            return vex.owner2 == this.getOwner();
                        } else {
                            return false;
                        }
                    });

                    if (!entities.isEmpty()) {
                        LivingEntity target1 = entities.get(0);
                        this.target = entities.get(0);
                        for (LivingEntity entity : entities) {
                            if (entity.distanceTo(this) < target1.distanceTo(this)) {
                                target1 = entity;
                                this.target = entity;
                            }
                        }
                    }
                }


                if (this.target == this.getOwner()) {
                    this.target = null;
                }
                if(this.target == null){
                    if(this.pos1 != null && !this.getLevel().isClientSide()) {
                        this.setPos(this.pos1.add(-0.5+level.random.nextDouble()*1, -0.5+level.random.nextDouble()*1, -0.5+level.random.nextDouble()*1));

                    }
                }



                if (this.target != null) {
                    if (this.target.isAlive() && !this.target.isDeadOrDying()) {
                        if (!this.getLevel().isClientSide()) {
                            this.pos1 = this.target.position().add(new Vec3(0, 1.5, 0)).add(new Vec3(0, this.target.getBoundingBox().getYsize() / 2, 0));
                            this.setPos(this.target.position().add(new Vec3(0, 1.5, 0)).add(new Vec3(0, this.target.getBoundingBox().getYsize() / 2, 0)).add(-0.5+level.random.nextDouble()*1, -0.5+level.random.nextDouble()*1, -0.5+level.random.nextDouble()*1));

                        }


                        if (tickCount % 10 == 5) {
                            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, this.target.getBoundingBox().inflate(2D), livingEntity -> {
                                return FriendshipBracelet.PlayerFriendshipPredicate((Player) this.getOwner(), livingEntity);
                            });
                            entities.removeIf(livingEntity -> {
                                if (livingEntity instanceof InvisiVex vex) {
                                    return vex.owner2 == this.getOwner();
                                } else {
                                    return false;
                                }
                            });/*
                            if (((Player) this.getOwner()).getLastHurtMob() != null) {
                                if (((Player) this.getOwner()).getLastHurtMob().isAlive() && this.getOwner().distanceTo(((Player) this.getOwner()).getLastHurtMob()) <= 8) {
                                    if (!entities.contains(((Player) this.getOwner()).getLastHurtMob())) {
                                        entities.add(((Player) this.getOwner()).getLastHurtMob());
                                    }
                                }
                            }*/
                            Object[] entitiesarray = entities.toArray();

                            int entityamount = entitiesarray.length;
                            this.target.invulnerableTime = 0;
                            AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "knockbackresist", 1, AttributeModifier.Operation.ADDITION);
                            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                            builder.put(Attributes.KNOCKBACK_RESISTANCE, modifier);
                            this.target.getAttributes().addTransientAttributeModifiers(builder.build());
                            this.target.hurt(new IndirectEntityDamageSource("spell",this, (Player) this.getOwner()), (float)Math.max(1,this.damage/6));
                            this.target.getAttributes().removeAttributeModifiers(builder.build());
                            if(!this.getLevel().isClientSide()) {
                                for (ServerPlayer serverPlayer : ((ServerLevel) this.getLevel()).players()) {
                                    Messages.sendToPlayer(new ParticlePacket2(this.getX(), this.getY(), this.getZ(), this.target.getX(), this.target.getEyeY(), this.target.getZ(),1), serverPlayer);
                                }
                            }
                        }
                    }
                    else{
                        if(this.pos1 != null&& !this.getLevel().isClientSide()) {
                            this.setPos(this.pos1.add(-0.5+level.random.nextDouble()*1, -0.5+level.random.nextDouble()*1, -0.5+level.random.nextDouble()*1));

                        }
                        if (this.distanceToSqr(new Vec3(this.lastx, this.lasty, this.lastz)) > 1) {
                            for (int iii = 0; iii < 25; iii++) {
                                double X = this.position().x + (this.lastx - this.position().x) * ((double) iii / (25));
                                double Y = this.position().y + (this.lasty - this.position().y) * ((double) iii / (25));
                                double Z = this.position().z + (this.lastz - this.position().z) * ((double) iii / (25));
                                this.level.addParticle(ParticleTypes.DRAGON_BREATH, X, Y, Z, 0, 0, 0);
                            }
                        }
                        this.lastx = this.position().x;
                        this.lasty = this.position().y;
                        this.lastz = this.position().z;
                        return;
                    }


                }
                if (this.distanceToSqr(new Vec3(this.lastx, this.lasty, this.lastz)) > 1) {
                    for (int iii = 0; iii < 25; iii++) {
                        double X = this.position().x + (this.lastx - this.position().x) * ((double) iii / (25));
                        double Y = this.position().y + (this.lasty - this.position().y) * ((double) iii / (25));
                        double Z = this.position().z + (this.lastz - this.position().z) * ((double) iii / (25));
                        this.level.addParticle(ParticleTypes.DRAGON_BREATH, X, Y, Z, 0, 0, 0);
                    }
                }
                this.lastx = this.position().x;
                this.lasty = this.position().y;
                this.lastz = this.position().z;

            }
        }
        else {
            this.discard();
        }
    }



    @Override
    public ItemStack getItem() {
        return Items.ENDER_EYE.getDefaultInstance();
    }
    public boolean isPickable() {
        return true;
    }

    public float getPickRadius() {
        return 0.125F;
    }

}
