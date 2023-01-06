package com.cleannrooster.spellblademod.entity;

import com.cleannrooster.spellblademod.ModBlocks;
import com.cleannrooster.spellblademod.StatusEffectsModded;
import com.cleannrooster.spellblademod.items.FriendshipBracelet;
import com.cleannrooster.spellblademod.items.Impale;
import com.cleannrooster.spellblademod.items.ParticlePacket2;
import com.cleannrooster.spellblademod.setup.Messages;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.lang.Math.*;

public class SentinelEntity extends PathfinderMob implements NeutralMob {

    private boolean noTotem;
    public Player owner;
    Vec3 location = Vec3.ZERO;
    public float damage = 4;

    public SentinelEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.noPhysics = false;
        this.setNoGravity(true);
        this.moveControl = new VexMoveControl(this);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.MOVEMENT_SPEED,0.20F);
    }
    public void move(MoverType p_33997_, Vec3 p_33998_) {
        super.move(p_33997_, p_33998_);
        this.checkInsideBlocks();
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int p_21673_) {

    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID p_21672_) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }

    class VexMoveControl extends MoveControl {
        public VexMoveControl(SentinelEntity p_34062_) {
            super(p_34062_);
        }

        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                Vec3 vec3 = new Vec3(this.wantedX - SentinelEntity.this.getX(), this.wantedY - SentinelEntity.this.getY(), this.wantedZ - SentinelEntity.this.getZ());
                double d0 = vec3.length();
                if (d0 < SentinelEntity.this.getBoundingBox().getSize()) {
                    this.operation = MoveControl.Operation.WAIT;
                    SentinelEntity.this.setDeltaMovement(SentinelEntity.this.getDeltaMovement().scale(0.5D));
                } else {

                        SentinelEntity.this.setDeltaMovement(SentinelEntity.this.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.05D / d0)));
                    if (SentinelEntity.this.getTarget() == null) {
                        Vec3 vec31 = SentinelEntity.this.getDeltaMovement();
                        SentinelEntity.this.setYRot(-((float)Mth.atan2(vec31.x, vec31.z)) * (180F / (float)Math.PI));
                        SentinelEntity.this.yBodyRot = SentinelEntity.this.getYRot();
                    } else {
                        double d2 = SentinelEntity.this.getTarget().getX() - SentinelEntity.this.getX();
                        double d1 = SentinelEntity.this.getTarget().getZ() - SentinelEntity.this.getZ();
                        SentinelEntity.this.setYRot(-((float)Mth.atan2(d2, d1)) * (180F / (float)Math.PI));
                        SentinelEntity.this.yBodyRot = SentinelEntity.this.getYRot();
                    }
                }

            }
        }
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new SentinelMoveTowardsEnemyGoal());
        this.goalSelector.addGoal(8, new VexRandomMoveGoal());
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, p_21024_ ->{
            if(this.owner != null && p_21024_ != this.owner && !(p_21024_ instanceof SentinelEntity)) {
                return FriendshipBracelet.PlayerFriendshipPredicate(this.owner, p_21024_);
            }
        else return false;}));
    }

    public void tick() {
        super.tick();
        if(this.owner != null && this.owner.isAlive()) {
            if (SentinelEntity.this.tickCount % 20 == 5 && this.getTarget() != null && this.getTarget().isAlive() && this.owner != null && this.owner.isAlive()) {
                if (this.getLevel().clip(new ClipContext(this.getEyePosition(), this.location, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.BLOCK) {
                    if (!this.getLevel().isClientSide()) {
                        for (ServerPlayer serverPlayer : ((ServerLevel) this.getLevel()).players()) {
                            Messages.sendToPlayer(new ParticlePacket2(this.getX(), this.getEyeY(), this.getZ(), this.location.x, this.location.y, this.location.z,1), serverPlayer);
                        }
                    }

                    List<LivingEntity> entities2 = this.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(location.x - 1, location.y - 1, location.z - 1, location.x + 1, location.y + 1, location.z + 1), target -> target != this.owner && FriendshipBracelet.PlayerFriendshipPredicate(this.owner, target));
                    for (LivingEntity entity : entities2) {
                        entity.invulnerableTime = 0;
                        entity.hurt(new EntityDamageSource("spell", SentinelEntity.this), this.damage/2);
                    }
                }

            }
            if(SentinelEntity.this.tickCount % 20 == 15 && this.getTarget() != null && this.getTarget().isAlive() && this.owner != null && this.owner.isAlive()){
                this.location = this.getTarget().getEyePosition().add(this.getTarget().getDeltaMovement().multiply(10,10,10)).add(0,0.5,0);

            }

        }
        else{
            this.kill();
        }
        if(this.tickCount > 400 && !this.getLevel().isClientSide()){
            this.kill();
        }

    }


    class VexRandomMoveGoal extends Goal {
        public VexRandomMoveGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            return !SentinelEntity.this.getMoveControl().hasWanted() && SentinelEntity.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = SentinelEntity.this.blockPosition();
            /*Optional<BlockPos> totem = BlockPos.findClosestMatch(SentinelEntity.this.getOnPos(),64, 64, (p_186148_) -> {
                return SentinelEntity.this.getLevel().getBlockState(p_186148_).is(ModBlocks.SENTINEL_TOTEM_BLOCK.get());
            });*/
            if (SentinelEntity.this.owner != null && SentinelEntity.this.owner.isAlive()) {
                blockpos = SentinelEntity.this.owner.getOnPos();
                SentinelEntity.this.setNoGravity(true);
                SentinelEntity.this.noPhysics = false;
                SentinelEntity.this.setPersistenceRequired();
                for (int i = 0; i < 3; ++i) {
                    BlockPos blockpos1 = blockpos.offset(SentinelEntity.this.random.nextInt(15) - 7, SentinelEntity.this.random.nextInt(11) - 5, SentinelEntity.this.random.nextInt(15) - 7);
                    if (SentinelEntity.this.level.isEmptyBlock(blockpos1)) {
                        SentinelEntity.this.moveControl.setWantedPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 2.05D);
                        if (SentinelEntity.this.getTarget() == null) {
                            SentinelEntity.this.getLookControl().setLookAt((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                        }
                        break;
                    }
                }
            }
            else{
                SentinelEntity.this.moveControl.setWantedPosition(SentinelEntity.this.getX(),SentinelEntity.this.getY(),SentinelEntity.this.getZ(),0.25D);
                SentinelEntity.this.setNoGravity(false);
                SentinelEntity.this.noPhysics = false;
                SentinelEntity.this.noTotem = true;

            }

        }
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_) {
        return false;
    }

    class SentinelMoveTowardsEnemyGoal extends Goal {
        public SentinelMoveTowardsEnemyGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (SentinelEntity.this.getTarget() != null /*&& SentinelEntity.this.random.nextInt(reducedTickDelay(7)) == 0*/) {
                return true;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return SentinelEntity.this.getTarget() != null && SentinelEntity.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = SentinelEntity.this.getTarget();
            if (livingentity != null ) {
                float f7 = livingentity.getYHeadRot();
                float f8 = livingentity.getYRot()+60;
                float f9 = livingentity.getYRot()-60;
                float f = livingentity.getXRot();
                float f1 = -Mth.sin(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
                float f2 = -Mth.sin(f * ((float) Math.PI / 180F));
                float f3 = Mth.cos(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));

                Vec3 vec3 = livingentity.getEyePosition();
                SentinelEntity.this.moveControl.setWantedPosition(vec3.x+f1*5, livingentity.getEyePosition().y() + 2, vec3.z+f3*5, 2.0D);
            }

        }

        public void stop() {
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = SentinelEntity.this.getTarget();
            if (livingentity != null) {
                float f7 = livingentity.getYHeadRot();
                float f8 = livingentity.getYRot()+60;
                float f9 = livingentity.getYRot()-60;
                float f = livingentity.getXRot();
                float f1 = -Mth.sin(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
                float f2 = -Mth.sin(f * ((float) Math.PI / 180F));
                float f3 = Mth.cos(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));

                double d0 = SentinelEntity.this.distanceToSqr(livingentity);
                if (d0 < 64*64) {

                    Vec3 vec3 = livingentity.getEyePosition();
                    SentinelEntity.this.moveControl.setWantedPosition(vec3.x+f1*5, vec3.y+f2*5, vec3.z+f3*5, 2.05);
                }

            }
        }
    }

}
