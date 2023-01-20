package com.cleannrooster.spellblademod.entity;

import com.cleannrooster.spellblademod.items.FriendshipBracelet;
import com.cleannrooster.spellblademod.items.ModItems;
import com.cleannrooster.spellblademod.items.ParticlePacket2;
import com.cleannrooster.spellblademod.manasystem.network.ParticlePacket3;
import com.cleannrooster.spellblademod.setup.Messages;
import io.netty.buffer.Unpooled;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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

import java.util.List;
import java.util.stream.Stream;
public class AmethystEntity extends AbstractArrow implements ItemSupplier {
    public LivingEntity target;
    public boolean inGround = false;
    public double damage = 1;

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

        if(this.tickCount <= 20 && !this.getLevel().isClientSide()){
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.8,0.8,0.8));
        }

        if(this.target != null&& !this.target.isDeadOrDying()  && this.tickCount > 20 && !this.inGround){
            this.setDeltaMovement(this.getDeltaMovement().add((this.target.position().add(new Vec3(0,this.target.getBoundingBox().getYsize()/2,0).subtract(this.position()))).normalize().multiply(1,1,1)));
            if(this.getDeltaMovement().length() > 2){
                this.setDeltaMovement(this.getDeltaMovement().normalize().multiply(2,2,2));
            }
        }
        if(this.target != null && this.target.isDeadOrDying() && this.tickCount > 20) {
            this.setNoGravity(false);
        }
        if(this.tickCount > 20 && this.target != null) {
            super.tick();
            if(!this.getLevel().isClientSide()) {
                Vec3 vec3 = this.getDeltaMovement();
                double d0 = vec3.horizontalDistance();
                this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
                this.setXRot((float) (Mth.atan2(vec3.y, d0) * (double) (180F / (float) Math.PI)));
                this.yRotO = this.getYRot();
                this.xRotO = this.getXRot();
            }
        }
        else{
            super.baseTick();
            this.setPos(this.position().add(this.getDeltaMovement()));
            if(!this.inGround && !this.getLevel().isClientSide()) {
                Vec3 vec3 = this.getDeltaMovement();
                double d0 = vec3.horizontalDistance();
                this.xRotO = this.getXRot();

                this.setXRot(this.xRotO+144);
                double d5 = vec3.x;
                double d6 = vec3.y;
                double d1 = vec3.z;
                double d7 = this.getX() + d5;
                double d2 = this.getY() + d6;
                double d3 = this.getZ() + d1;
                double d4 = vec3.horizontalDistance();

                //this.setYRot((float)(Mth.atan2(d5, d1) * (double)(180F / (float)Math.PI)));

                //this.setXRot((float)(Mth.atan2(d6, d4) * (double)(180F / (float)Math.PI)));
                //this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
                Vec3 vec32 = this.position();
                Vec3 vec33 = vec32.add(vec3);
            }
                this.setXRot(Mth.lerp(0.2F, this.xRotO, this.getXRot()));


            Vec3 vec3 = this.getDeltaMovement();
            this.inGroundTime = 0;
            Vec3 vec32 = this.position();
            Vec3 vec33 = vec32.add(vec3);
            HitResult hitresult = this.level.clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if(this.target != null && this.getBoundingBox().intersects(this.target.getBoundingBox())){
                this.target.hurt(IndirectEntityDamageSource.arrow(this, this.getOwner()), (float) this.damage);
                if (this.getLevel() instanceof ServerLevel serverLevel) {

                    for (ServerPlayer player1 : serverLevel.players()
                    ) {
                        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                        buf.writeDouble(this.getX());
                        buf.writeDouble(this.getY());
                        buf.writeDouble(this.getZ());
                        Messages.sendToPlayer(new ParticlePacket3(buf), player1);
                    }
                }
                this.discard();
            }
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
                if(hitresult.getType() == HitResult.Type.BLOCK) {
                    this.inGround = true;
                    this.setDeltaMovement(Vec3.ZERO);
                    if(this.getLevel() instanceof ServerLevel serverLevel) {

                        for (ServerPlayer player1 : serverLevel.players()
                        ) {
                            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                            buf.writeDouble(this.getX());
                            buf.writeDouble(this.getY());
                            buf.writeDouble(this.getZ());
                            Messages.sendToPlayer(new ParticlePacket3(buf), player1);
                        }
                    }
                    this.discard();
                }
                else {
                    this.onHit(hitresult);
                    this.hasImpulse = true;
                }
            }

        }
        if(this.getOwner() instanceof Player player && !this.getLevel().isClientSide()) {
            if (this.tickCount == 20) {
                List<AmethystEntity> same = this.getLevel().getEntitiesOfClass(AmethystEntity.class,this.getBoundingBox().inflate(16), asdf1 -> asdf1.getOwner() == this.getOwner());
                List<LivingEntity> entities = this.getLevel().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(16), asdf -> {
                    List<AmethystEntity> same2 = List.copyOf(same);
                    Stream<AmethystEntity> same3 = same2.stream().filter(same1 ->
                            same1.target == asdf);
                    return same3.toArray().length < 4 && FriendshipBracelet.PlayerFriendshipPredicate(player, asdf);});
                if (!entities.isEmpty()) {
                    LivingEntity entity = this.getLevel().getNearestEntity(entities, TargetingConditions.forCombat(), player, this.getX(), this.getY(), this.getZ());
                    if (entity != null) {
                        this.target = entity;
                        this.setDeltaMovement(entity.position().add(new Vec3(0, entity.getBoundingBox().getYsize() / 2, 0)).subtract(this.getEyePosition()).normalize().multiply(3, 3, 3));
                    }
                    else {
                        if(player.getLevel() instanceof ServerLevel serverLevel) {

                            for (ServerPlayer player1 : serverLevel.players()
                            ) {
                                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                                buf.writeDouble(this.getX());
                                buf.writeDouble(this.getY());
                                buf.writeDouble(this.getZ());
                                Messages.sendToPlayer(new ParticlePacket3(buf), player1);
                            }
                        }
                        this.discard();
                    }
                }
                else{
                    if(player.getLevel() instanceof ServerLevel serverLevel) {

                        for (ServerPlayer player1 : serverLevel.players()
                        ) {
                            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                            buf.writeDouble(this.getX());
                            buf.writeDouble(this.getY());
                            buf.writeDouble(this.getZ());
                            Messages.sendToPlayer(new ParticlePacket3(buf), player1);                        }
                    }

                    this.discard();
                }

            }
        }
    }

    @Override
    protected boolean tryPickup(Player p_150121_) {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {
        if(p_36757_.getEntity() != this.getOwner()) {
            if (p_36757_.getEntity() instanceof LivingEntity living) {
                living.invulnerableTime = 0;
            }
            p_36757_.getEntity().hurt(IndirectEntityDamageSource.arrow(this, this.getOwner()), (float) this.damage);
            if (this.getLevel() instanceof ServerLevel serverLevel) {

                for (ServerPlayer player1 : serverLevel.players()
                ) {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeDouble(this.getX());
                    buf.writeDouble(this.getY());
                    buf.writeDouble(this.getZ());
                    Messages.sendToPlayer(new ParticlePacket3(buf), player1);
                }
            }
            this.discard();
        }

    }


    @Override
    protected void onHitBlock(BlockHitResult p_36755_) {
        if (this.getLevel() instanceof ServerLevel serverLevel) {

            for (ServerPlayer player1 : serverLevel.players()
            ) {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                buf.writeDouble(this.getX());
                buf.writeDouble(this.getY());
                buf.writeDouble(this.getZ());
                Messages.sendToPlayer(new ParticlePacket3(buf), player1);
            }
        }
        this.discard();
        super.onHitBlock(p_36755_);
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
        return ModItems.DAGGER.get().getDefaultInstance();
    }
}
