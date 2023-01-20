package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.entity.*;
import com.cleannrooster.spellblademod.manasystem.manatick;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class LightningStrike extends Spell{
    public LightningStrike(Properties p_41383_) {
        super(p_41383_);
    }
    public Item getIngredient1() {return Items.COPPER_ORE;};
    public Item getIngredient2() {return ModItems.ESSENCEBOLT.get();};
    public ChatFormatting color(){
        return ChatFormatting.YELLOW;
    }
    @Override
    public boolean isTargeted() {
        return true;
    }

    @Override
    public boolean triggeron(Level worldIn, Player playerIn, LivingEntity target, float modifier) {
        if(!worldIn.canSeeSky(target.blockPosition())){
            return false;
        }
        if(!playerIn.getCooldowns().isOnCooldown(this)) {
            LesserLightning lightning = new LesserLightning(ModEntities.LIGHTNING.get(),worldIn);
            lightning.setPos(target.position());
            worldIn.addFreshEntity(lightning);
            AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(),"knockbackresist",1, AttributeModifier.Operation.ADDITION);
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.KNOCKBACK_RESISTANCE, modifier2);

            target.invulnerableTime = 0;
            target.hurt(DamageSource.playerAttack(playerIn).bypassArmor(), (float) (playerIn.getAttributeValue(Attributes.ATTACK_DAMAGE) / 2));
            target.getAttributes().removeAttributeModifiers(builder.build());
            ((Player) playerIn).getAttribute(manatick.WARD).setBaseValue(((Player) playerIn).getAttributeBaseValue(manatick.WARD) - 20);

            if(((Player)playerIn).getAttributes().getBaseValue(manatick.WARD)< -21) {

                playerIn.hurt(DamageSource.MAGIC, 2);
            }
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand p_41434_) {
        BlockHitResult hitResult = Impale.getPlayerPOVHitResult(worldIn,playerIn, ClipContext.Fluid.NONE,playerIn.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue());
        if(hitResult.getType() == HitResult.Type.BLOCK) {
            if(!worldIn.canSeeSky(hitResult.getBlockPos().above())){
                return super.use(worldIn, playerIn, p_41434_);
            }
            LesserLightning lightning = new LesserLightning(ModEntities.LIGHTNING.get(),worldIn);
            lightning.setPos(hitResult.getBlockPos().getX(),hitResult.getBlockPos().getY(),hitResult.getBlockPos().getZ());
            worldIn.addFreshEntity(lightning);
            List<LivingEntity> targets = worldIn.getEntitiesOfClass(LivingEntity.class,new AABB(hitResult.getBlockPos().getX()-2,hitResult.getBlockPos().getY()-2,hitResult.getBlockPos().getZ()-2,hitResult.getBlockPos().getX()+2,hitResult.getBlockPos().getY()+2,hitResult.getBlockPos().getZ()+2), living -> FriendshipBracelet.PlayerFriendshipPredicate(playerIn,living));
            targets.removeIf(target -> target == playerIn);
            for(LivingEntity target : targets) {
                AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(),"knockbackresist",1, AttributeModifier.Operation.ADDITION);
                ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                builder.put(Attributes.KNOCKBACK_RESISTANCE, modifier2);

                target.invulnerableTime = 0;
                target.hurt(DamageSource.playerAttack(playerIn).bypassArmor(), (float) (playerIn.getAttributeValue(Attributes.ATTACK_DAMAGE) / 2));
                target.getAttributes().removeAttributeModifiers(builder.build());

            }
            ((Player)playerIn).getAttribute(manatick.WARD).setBaseValue(((Player) playerIn).getAttributeBaseValue(manatick.WARD)-20);

            if (((Player)playerIn).getAttributes().getBaseValue(manatick.WARD) < -21) {
                playerIn.hurt(DamageSource.MAGIC,2);
            }

        }




        return super.use(worldIn, playerIn, p_41434_);
    }
    @Override
    public boolean isFoil(ItemStack p_41453_) {
        if (p_41453_.hasTag()){
            if(p_41453_.getTag().getInt("Triggerable") == 1){
                return true;
            }
        }
        return false;
    }
    public <T extends Entity> T getNearestEntity(List<? extends T> p_45983_, TargetingConditions p_45984_, @javax.annotation.Nullable T p_45985_, double p_45986_, double p_45987_, double p_45988_) {
        double d0 = -1.0D;
        T t = null;

        for(T t1 : p_45983_) {
            double d1 = t1.distanceToSqr(p_45986_, p_45987_, p_45988_);
            if (d0 == -1.0D || d1 < d0) {
                d0 = d1;
                t = t1;

            }
        }

        return t;
    }
    @Override
    public boolean trigger(Level level, Player player, float modifier, @Nullable ConduitSpearEntity spear) {
        if(!player.getCooldowns().isOnCooldown(this)) {
            Entity entity2;
            if (spear != null) {
                entity2 = spear;
            } else {
                entity2 = player;
            }
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, entity2.getBoundingBox().inflate(6), entity -> FriendshipBracelet.PlayerFriendshipPredicate(player, entity));
            entities.removeIf(entity -> !FriendshipBracelet.PlayerFriendshipPredicate(player, entity));
            //entities.removeIf(entity -> !entity.hasLineOfSight(entity2));
            entities.removeIf(entity -> entity == entity2);
            entities.removeIf(entity -> entity == player);
            entities.removeIf(entity -> !level.canSeeSky(entity.blockPosition()));


            for (int ii = 0; ii < 3; ii++) {
                LivingEntity target = (LivingEntity) getNearestEntity(entities, TargetingConditions.forNonCombat(), entity2, entity2.getX(), entity2.getY(), entity2.getZ());
                if (target != null) {

                    LesserLightning lightning = new LesserLightning(ModEntities.LIGHTNING.get(), level);
                    lightning.setPos(target.position());
                    AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(),"knockbackresist",1, AttributeModifier.Operation.ADDITION);
                    ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                    builder.put(Attributes.KNOCKBACK_RESISTANCE, modifier2);

                    target.getAttributes().addTransientAttributeModifiers(builder.build());
                    target.invulnerableTime = 0;
                    target.hurt(DamageSource.playerAttack(player).bypassArmor(), (float) (player.getAttributeValue(Attributes.ATTACK_DAMAGE) / 2));
                    target.getAttributes().removeAttributeModifiers(builder.build());

                    level.addFreshEntity(lightning);
                    entities.remove(target);
                }

            }

            ((Player) player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD) - 20);

            if (((Player) player).getAttributes().getBaseValue(manatick.WARD) < -21) {

                player.hurt(DamageSource.MAGIC, 2);
            }
        }
        return false;
    }
    public int getColor() {
        return 15065764;
    }
    public int getColor2() {
        return 16751977;
    }

    @Override
    public int triggerCooldown() {
        return 20;
    }
}
