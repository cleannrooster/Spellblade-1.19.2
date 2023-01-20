package com.cleannrooster.spellblademod.entity;

import com.cleannrooster.spellblademod.manasystem.client.ParticleReverb;
import com.cleannrooster.spellblademod.manasystem.manatick;
import com.cleannrooster.spellblademod.setup.Messages;
import com.google.common.collect.ImmutableMultimap;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class ReverberatingRay extends AbstractArrow implements ItemSupplier {

    public  boolean primary = false;
    public LivingEntity target;
    private boolean secondary = false;
    public boolean triggered = false;
    public float damage = 6;
    ConduitSpearEntity spear;
    @Override
    protected void onHit(HitResult p_37218_) {
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {

    }



    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {

    }
    public ReverberatingRay(EntityType<? extends AbstractArrow> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
        this.pickup = Pickup.DISALLOWED;
        this.target = null;
    }
    public ReverberatingRay(EntityType<? extends AbstractArrow> p_36833_, Level p_36834_, @Nullable LivingEntity target2, @Nullable ConduitSpearEntity spear) {
        super(p_36833_, p_36834_);
        this.pickup = Pickup.DISALLOWED;
        this.target = target2;
        this.spear = spear;
    }

    @Override
    protected boolean tryPickup(Player p_150121_) {
        return false;
    }

    protected float rotateTowards(float p_24957_, float p_24958_, float p_24959_) {
        float f = Mth.degreesDifference(p_24957_, p_24958_);
        float f1 = Mth.clamp(f, -p_24959_, p_24959_);
        return p_24957_ + f1;
    }
    public static float getYRotD(Entity this1, Entity target) {
        double d0 = target.getX() - this1.getX();
        double d1 = target.getZ() - this1.getZ();
        return /*!(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d0) > (double)1.0E-5F) ? Optional.empty() : Optional.of(*/(float)(Mth.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
    }
    public static float getXRotD(Entity this1, LivingEntity target) {
        double d0 = target.getX() - this1.getX();
        double d1 = target.getY()+target.getBoundingBox().getYsize()/2 - this1.getEyeY();
        double d2 = target.getZ() - this1.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        return /*!(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d3) > (double)1.0E-5F) ? Optional.empty() : Optional.of(*/(float)(-(Mth.atan2(d1, d3) * (double)(180F / (float)Math.PI)));
    }
    @Override
    public void tick() {
        super.tick();
        Entity entity2;
        if (this.spear != null){
            entity2 = this.spear;
        }
        else{
            entity2 = this.getOwner();
        }
        if (this.triggered && tickCount > 18){
            this.discard();
        }
        if (this.getOwner() != null) {
            Random rand = new Random();

            this.pickup = Pickup.DISALLOWED;


            float f7 = this.getOwner().getYRot();
            float f8 = this.getOwner().getYRot()+60;
            float f9 = this.getOwner().getYRot()-60;
            float f = this.getOwner().getXRot();
            if(this.target != null){
                f7 = getYRotD(this,target);
                f = getXRotD(this,target);
            }
            float f1 = -Mth.sin(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
            float f2 = -Mth.sin(f * ((float) Math.PI / 180F));
            float f3 = Mth.cos(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
            ReverberatingRay orb1 = new ReverberatingRay(ModEntities.REVERBERATING_RAY_ORB.get(), this.level);
            orb1.setOwner(this.getOwner());
            orb1.secondary = true;
            orb1.noPhysics = true;
                this.setPos(entity2.getEyePosition().add(f1, f2, f3));
            Position pos1 = (entity2.getEyePosition().add(f1 * 40, f2 * 40, f3 * 40)).add(rand.nextDouble(-10/Math.sqrt(tickCount),10/Math.sqrt(tickCount)),rand.nextDouble(-10/Math.sqrt(tickCount),10/Math.sqrt(tickCount)),rand.nextDouble(-10/Math.sqrt(tickCount),10/Math.sqrt(tickCount)));
            Position pos2 = (entity2.getEyePosition().add(f1 * 40, f2 * 40, f3 * 40)).add(rand.nextDouble(-10/Math.sqrt(tickCount),10/Math.sqrt(tickCount)),rand.nextDouble(-10/Math.sqrt(tickCount),10/Math.sqrt(tickCount)),rand.nextDouble(-10/Math.sqrt(tickCount),10/Math.sqrt(tickCount)));
            Position pos3 = (entity2.getEyePosition().add(f1 * 40, f2 * 40, f3 * 40)).add(rand.nextDouble(-10/Math.sqrt(tickCount),10/Math.sqrt(tickCount)),rand.nextDouble(-10/Math.sqrt(tickCount),10/Math.sqrt(tickCount)),rand.nextDouble(-10/Math.sqrt(tickCount),10/Math.sqrt(tickCount)));
            if (this.triggered && tickCount > 10){
                 pos1 = (entity2.getEyePosition().add(f1 * 40, f2 * 40, f3 * 40));
                 pos2 = (entity2.getEyePosition().add(f1 * 40, f2 * 40, f3 * 40));
                 pos3 = (entity2.getEyePosition().add(f1 * 40, f2 * 40, f3 * 40));

            }

            if (tickCount % 10 == 1) {

                SoundEvent soundEvent = SoundEvents.WARDEN_SONIC_BOOM;
                level.playSound((Player)null, this.getOnPos(), soundEvent, SoundSource.PLAYERS, 1F, 1F);
                if(this.getLevel() instanceof ServerLevel serverLevel) {
                    for (ServerPlayer player : serverLevel.players()){
                        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                        buf.writeDouble(this.getX());
                        buf.writeDouble(this.getY());
                        buf.writeDouble(this.getZ());
                        buf.writeDouble(pos1.x());
                        buf.writeDouble(pos1.y());
                        buf.writeDouble(pos1.z());
                        buf.writeDouble(pos2.x());
                        buf.writeDouble(pos2.y());
                        buf.writeDouble(pos2.z());
                        buf.writeDouble(pos3.x());
                        buf.writeDouble(pos3.y());
                        buf.writeDouble(pos3.z());
                        Messages.sendToPlayer(new ParticleReverb(buf),player);
                    }
                }
                List<Entity> list = this.level.getEntities(this, new AABB((double)entity2.getEyePosition().x(), (double)entity2.getEyePosition().y(), (double)entity2.getEyePosition().z(), (double)pos1.x(), (double)pos1.y(), (double)pos1.z()));
                List<Entity> list2 = this.level.getEntities(this, new AABB((double)entity2.getEyePosition().x(), (double)entity2.getEyePosition().y(), (double)entity2.getEyePosition().z(), (double)pos2.x(), (double)pos2.y(), (double)pos2.z()));
                List<Entity> list3 = this.level.getEntities(this, new AABB((double)entity2.getEyePosition().x(), (double)entity2.getEyePosition().y(), (double)entity2.getEyePosition().z(), (double)pos3.x(), (double)pos3.y(), (double)pos3.z()));

                for (int ii = 0; ii < list.toArray().length; ii++) {
                    Optional<Vec3> vec1 = list.get(ii).getBoundingBox().inflate(0.5).clip(entity2.getEyePosition(), (Vec3) pos1);
                    if (list.get(ii) instanceof LivingEntity target && vec1.isPresent() &&  list.get(ii) != this.getOwner() && !this.getLevel().isClientSide()) {
                        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(),"knockbackresist",1, AttributeModifier.Operation.ADDITION);

                        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                        builder.put(Attributes.KNOCKBACK_RESISTANCE, modifier);
                        target.getAttributes().addTransientAttributeModifiers(builder.build());
                        target.invulnerableTime = 0;
                        target.hurt(new EntityDamageSource("spell", this.getOwner()), (float)Math.max(2,this.damage/3));

                        target.getAttributes().removeAttributeModifiers(builder.build());
                    }
                }
                for (int ii = 0; ii < list2.toArray().length; ii++) {
                    Optional<Vec3> vec1 = list2.get(ii).getBoundingBox().inflate(0.5).clip(entity2.getEyePosition(), (Vec3) pos2);
                    if (list2.get(ii) instanceof LivingEntity target && vec1.isPresent() &&  list2.get(ii) != this.getOwner()&& !this.getLevel().isClientSide()) {
                        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(),"knockbackresist",1, AttributeModifier.Operation.ADDITION);

                        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                        builder.put(Attributes.KNOCKBACK_RESISTANCE, modifier);
                        target.getAttributes().addTransientAttributeModifiers(builder.build());
                        target.invulnerableTime = 0;
                        target.hurt(new EntityDamageSource("spell", this.getOwner()), (float)Math.max(2,this.damage/3));

                        target.getAttributes().removeAttributeModifiers(builder.build());
                    }
                }
                for (int ii = 0; ii < list3.toArray().length; ii++) {
                    Optional<Vec3> vec1 = list3.get(ii).getBoundingBox().inflate(0.5).clip(entity2.getEyePosition(), (Vec3) pos3);
                    if (list3.get(ii) instanceof LivingEntity target && vec1.isPresent() && list3.get(ii) != this.getOwner()&& !this.getLevel().isClientSide()) {
                        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(),"knockbackresist",1, AttributeModifier.Operation.ADDITION);

                        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                        builder.put(Attributes.KNOCKBACK_RESISTANCE, modifier);
                        target.getAttributes().addTransientAttributeModifiers(builder.build());
                        target.invulnerableTime = 0;
                        target.hurt(new EntityDamageSource("spell", this.getOwner()), (float)Math.max(2,this.damage/3));

                        target.getAttributes().removeAttributeModifiers(builder.build());
                    }
                }
            }
        }

    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    public boolean hurt(DamageSource p_36839_, float p_36840_) {
        return false;
    }
    @Override
    public boolean mayInteract(Level p_150167_, BlockPos p_150168_) {
        return false;
    }
    @Override
    public boolean isAttackable() {
        return false;
    }
    @Override
    public boolean skipAttackInteraction(Entity p_20357_) {
        return true;
    }


    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.AIR);
    }

}
