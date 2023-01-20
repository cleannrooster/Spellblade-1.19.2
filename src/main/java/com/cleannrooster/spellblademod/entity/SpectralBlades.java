package com.cleannrooster.spellblademod.entity;

import com.cleannrooster.spellblademod.entity.ThrownItems;
import com.cleannrooster.spellblademod.items.FriendshipBracelet;
import com.cleannrooster.spellblademod.items.ModItems;
import com.cleannrooster.spellblademod.manasystem.network.ParticlePacket3;
import com.cleannrooster.spellblademod.setup.Messages;
import com.google.common.io.ByteSource;
import io.netty.buffer.Unpooled;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpectralBlades extends AbstractArrow implements ItemSupplier {
    public LivingEntity target;
    public UUID targetUUID;
    public int chained = 0;
    public float damage = 4;
    public List<LivingEntity> list = new ArrayList<LivingEntity>();
    public List<SpectralBlades> buddies = new ArrayList<SpectralBlades>();
    private Entity cachedTarget;
    private List<LivingEntity> cachedList = new ArrayList<LivingEntity>();
    private List<SpectralBlades> cachedBuddies = new ArrayList<SpectralBlades>();

    protected SpectralBlades(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
        this.setNoGravity(true);
    }
    public SpectralBlades(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_, Player owner) {
        super(p_36721_, p_36722_);
        this.setOwner(owner);
        this.setNoGravity(true);
    }
    @Nullable
    public Entity getTarget() {
        if (this.cachedTarget != null && !this.cachedTarget.isRemoved()) {
            return this.cachedTarget;
        } else if (this.targetUUID != null && this.level instanceof ServerLevel) {
            this.cachedTarget = ((ServerLevel)this.level).getEntity(this.targetUUID);
            return this.cachedTarget;
        } else {
            return null;
        }
    }
    @Nullable
    public List<LivingEntity> getList() {
        if (!this.cachedList.isEmpty()) {
            return this.cachedList;
        } else if (this.list != null) {
            this.cachedList = this.list;
            return this.cachedList;
        } else {
            return null;
        }
    }
    @Nullable
    public List<SpectralBlades> getBuddies() {
        if (!this.cachedBuddies.isEmpty()) {
            return this.cachedBuddies;
        } else if (this.buddies != null) {
            this.cachedBuddies = this.buddies;
            return this.cachedBuddies;
        } else {
            return null;
        }
    }
    @Nullable
    public void addBuddies(SpectralBlades blades) {
        if (blades != null) {
            this.buddies.add(blades);
            this.cachedBuddies.add(blades);
        }
    }
    @Nullable
    public void addList(LivingEntity living) {
        if (living != null) {
            this.list.add(living);
            this.cachedList.add(living);
        }
    }
    public void setTarget(@Nullable Entity p_37263_) {
        if (p_37263_ != null) {
            this.targetUUID = p_37263_.getUUID();
            this.cachedTarget = p_37263_;
        }

    }
    @Override
    public void tick() {
        if(this.firstTick){
            this.playSound(SoundEvents.AMETHYST_BLOCK_CHIME);
        }
        if (!this.getLevel().isClientSide() && this.getTarget() != null && this.getTarget().getBoundingBox().intersects(this.getBoundingBox())) {
            if (this.chained < 4 && this.getTarget() instanceof LivingEntity living && this.getOwner() instanceof Player player && living != player) {
                List<LivingEntity> entities = this.getLevel().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(16), asdf -> asdf != this.getTarget() && !this.getList().contains(asdf) &&asdf.hasLineOfSight(this) && FriendshipBracelet.PlayerFriendshipPredicate(player, asdf) && asdf != player);
                entities.removeIf(asdf -> this.getBuddies().stream().anyMatch(buddy -> {
                    if (buddy.getList().contains(asdf) || buddy.getTarget() == asdf) {
                        this.addList(asdf);
                        return true;
                    }
                    else{
                        return false;
                    }
                }));
                //entities.remove(this.getTarget());
                this.setTarget(this.getLevel().getNearestEntity(entities, TargetingConditions.forNonCombat(), (LivingEntity) this.getTarget(), this.getX(), this.getY(), this.getZ()));
                living.hurt(IndirectEntityDamageSource.arrow(this, this.getOwner()), (float) this.damage);

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
                if(entities.isEmpty()){
                    this.discard();
                }
                this.chained++;
                    this.addList((LivingEntity) this.getTarget());
            } else if (this.getTarget().getBoundingBox().intersects(this.getBoundingBox()) && this.getTarget() instanceof LivingEntity living && this.chained > 3 && this.getOwner() instanceof Player player) {
                living.hurt(IndirectEntityDamageSource.arrow(this, this.getOwner()), (float) this.damage);
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
        if(this.getBuddies().stream().anyMatch(buddy -> buddy.getList().stream().anyMatch(target -> target == this.getTarget()))){
            this.setTarget(null);
        }
        if(this.chained < 4 && this.getTarget() == null) {
            if (this.getOwner() instanceof Player player){
                if(this.tickCount % 5 == 0) {
                    List<LivingEntity> entities = this.getLevel().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(16), asdf -> asdf.hasLineOfSight(this) && FriendshipBracelet.PlayerFriendshipPredicate(player, asdf) && asdf != player && !this.list.contains(asdf) && !asdf.isDeadOrDying());
                    entities.removeIf(asdf -> this.getBuddies().stream().anyMatch(buddy -> {
                        if (buddy.getList().contains(asdf) || buddy.getTarget() == asdf) {
                            this.addList(asdf);
                            return true;
                        }
                        else{
                            return false;
                        }
                    }));
                    this.setTarget( this.getLevel().getNearestEntity(entities, TargetingConditions.forNonCombat(), (LivingEntity) this.getTarget(), this.getX(), this.getY(), this.getZ()));
                    if(this.getTarget() instanceof LivingEntity living) {
                        this.addList(living);
                    }

                }
            }
        }

        if(this.getTarget() != null && this.getTarget() instanceof LivingEntity living && !living.isDeadOrDying() && !this.getLevel().isClientSide() ) {
            this.setDeltaMovement((this.getTarget().position().add(0, this.getTarget().getBbHeight() / 2, 0).subtract(this.position()).multiply(0.05, 0.05, 0.05)));
        }
        if(this.getDeltaMovement().length() < 1 && !this.getLevel().isClientSide()){
            this.setDeltaMovement(this.getDeltaMovement().normalize().multiply(1,1,1));

        }
        if(this.getDeltaMovement().length() > 2 && !this.getLevel().isClientSide()){
            this.setDeltaMovement(this.getDeltaMovement().normalize().multiply(2,2,2));

        }
        if(this.tickCount > 200 && !this.getLevel().isClientSide()){
            this.discard();
        }

        Vec3 vec3 = this.getDeltaMovement();
            double d0 = vec3.horizontalDistance();
            this.setYRot(this.getYRot()+180);
            this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        double d7 = this.getX() + d5;
        double d2 = this.getY() + d6;
        double d3 = this.getZ() + d1;
        double d4 = vec3.horizontalDistance();

            //this.setYRot((float)(Mth.atan2(d5, d1) * (double)(180F / (float)Math.PI)));

        this.setXRot((float)(Mth.atan2(d6, d4) * (double)(160F / (float)Math.PI)));
        this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
        Vec3 vec32 = this.position();
        Vec3 vec33 = vec32.add(vec3);
        HitResult hitresult = this.level.clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitresult.getType() != HitResult.Type.MISS) {
            vec33 = hitresult.getLocation();
        }

        while(!this.isRemoved()) {
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

            if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
                this.hasImpulse = true;
            }

            if (entityhitresult == null || this.getPierceLevel() <= 0) {
                break;
            }

            hitresult = null;
        }
        super.baseTick();
            this.setPos(this.position().add(this.getDeltaMovement()));

    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_36772_) {
        if (this.target != null) {
            p_36772_.putUUID("Target", this.target.getUUID());
        }
        if (!this.list.isEmpty()) {
            CompoundTag tag = new CompoundTag();
            for (LivingEntity chained :
                 this.list) {
                tag.putUUID(chained.getDisplayName().getString(),chained.getUUID());

            }
            p_36772_.put("Chained", tag);

        }
        if (!this.buddies.isEmpty()) {
            CompoundTag tag = new CompoundTag();
            for (SpectralBlades chained :
                    this.buddies) {
                tag.putUUID(chained.getDisplayName().getString(),chained.getUUID());

            }
            p_36772_.put("Buddies", tag);

        }
        super.addAdditionalSaveData(p_36772_);
    }
    public void readAdditionalSaveData(CompoundTag p_37262_) {
        if (p_37262_.hasUUID("Target")) {
            this.targetUUID = p_37262_.getUUID("Target");
        }
        if (p_37262_.hasUUID("Chained") && this.getLevel() instanceof ServerLevel serverLevel) {
            List<LivingEntity> list = new ArrayList<>();
            for (String asdf:
            p_37262_.getCompound("Chained").getAllKeys()) {
                list.add((LivingEntity) serverLevel.getEntity(p_37262_.getCompound("Chained").getUUID(asdf)));
            }
            this.list = list;
        }
        if (p_37262_.hasUUID("Buddies") && this.getLevel() instanceof ServerLevel serverLevel) {
            List<SpectralBlades> list = new ArrayList<>();
            for (String asdf:
                    p_37262_.getCompound("Buddies").getAllKeys()) {
                list.add((SpectralBlades) serverLevel.getEntity(p_37262_.getCompound("Buddies").getUUID(asdf)));
            }
            this.buddies = list;
        }
        super.readAdditionalSaveData(p_37262_);
    }
    @Override
    protected void onHitBlock(BlockHitResult result) {
            if ((result.getDirection() == Direction.NORTH) || result.getDirection() == Direction.SOUTH) {
                this.setDeltaMovement(1 * this.getDeltaMovement().x, 1 * this.getDeltaMovement().y, -1 * this.getDeltaMovement().z);
				/*for (int i = 0; i < NUM_POINTS; ++i)
				{
				    final double angle = Math.toRadians(((double) i / NUM_POINTS) * 360d);

				        double x = Math.cos(angle) * RADIUS;
				        double y = Math.sin(angle) * RADIUS;

					ThrownItems thrown = new ThrownItems(world, this.thrower);
					thrown.setNoGravity(true);
					Vec3d vec3 = new Vec3d(x,0,y);
					thrown.shoot(vec3.x, vec3.y, vec3.z, 1, 0);

				}
				if(count >= 8) {
					this.setDead();
				}
				this.count++;*/
            }
            if ((result.getDirection() == Direction.EAST || result.getDirection() == Direction.WEST)) {
                this.setDeltaMovement(-1 * this.getDeltaMovement().x, 1 * this.getDeltaMovement().y, 1 * this.getDeltaMovement().z);
				/*for (int i = 0; i < NUM_POINTS; ++i)
				{
				    final double angle = Math.toRadians(((double) i / NUM_POINTS) * 360d);

				        double x = Math.cos(angle) * RADIUS;
				        double y = Math.sin(angle) * RADIUS;

					ThrownItems thrown = new ThrownItems(world, this.thrower);
					thrown.setNoGravity(true);
					Vec3d vec3 = new Vec3d(x,0,y);
					thrown.shoot(vec3.x, vec3.y, vec3.z, 1, 0);

				}
				if(count >= 8) {
					this.setDead();
				}
				this.count++;*/

            }
            if (result.getDirection() == Direction.UP || result.getDirection() == Direction.DOWN) {
                this.setDeltaMovement(1 * this.getDeltaMovement().x, -1 * this.getDeltaMovement().y, 1 * this.getDeltaMovement().z);
            }

    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {




    }

    @Override
    public ItemStack getItem() {
        return Items.AMETHYST_SHARD.getDefaultInstance();
    }
}