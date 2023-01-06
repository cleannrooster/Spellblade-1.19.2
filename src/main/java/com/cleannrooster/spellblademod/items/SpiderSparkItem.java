package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.SpellbladeMod;
import com.cleannrooster.spellblademod.entity.*;
import com.cleannrooster.spellblademod.manasystem.manatick;
import com.cleannrooster.spellblademod.patreon.Patreon;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Position;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.CatVariantTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class SpiderSparkItem extends Spell{
    public SpiderSparkItem(Properties p_41383_) {
        super(p_41383_);
    }
    public Item getIngredient1() {return Items.FERMENTED_SPIDER_EYE;};
    public Item getIngredient2() {return ModItems.ESSENCEBOLT.get();};
    public ChatFormatting color(){
        return ChatFormatting.DARK_GRAY;
    }
    public int getColor2() {
        return 10715804;
    }
    @Override
    public boolean isTargeted() {
        return true;
    }

    protected float rotateTowards(float p_24957_, float p_24958_, float p_24959_) {
        float f = Mth.degreesDifference(p_24957_, p_24958_);
        float f1 = Mth.clamp(f, -p_24959_, p_24959_);
        return p_24957_ + f1;
    }
    protected Optional<Float> getXRotD(double wantedX, double wantedY, double wantedZ, LivingEntity mob) {
        double d0 = wantedX - mob.getX();
        double d1 = wantedY - mob.getEyeY();
        double d2 = wantedZ - mob.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        return !(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d3) > (double)1.0E-5F) ? Optional.empty() : Optional.of((float)(-(Mth.atan2(d1, d3) * (double)(180F / (float)Math.PI))));
    }

    protected Optional<Float> getYRotD(double wantedX, double wantedY, double wantedZ, LivingEntity mob) {
        double d0 = wantedX - mob.getX();
        double d1 = wantedZ - mob.getZ();
        return !(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d0) > (double)1.0E-5F) ? Optional.empty() : Optional.of((float)(Mth.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F);
    }
    @Override
    public boolean triggeron(Level level, Player player, LivingEntity target, float modifier) {

        for(SpiderSpark spider : level.getEntitiesOfClass(SpiderSpark.class,player.getBoundingBox().inflate(16),spiderSpark -> spiderSpark.owner == player)){

            spider.setTarget(target);
            spider.getNavigation().stop();
            spider.getNavigation().createPath(target,16);
            spider.addEffect(new MobEffectInstance(MobEffects.JUMP,80,2));
            spider.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,80, (int) (player.getAttributeValue(Attributes.ATTACK_DAMAGE)/4 - 1)));

            getYRotD(target.getX(),target.getY(),target.getZ(), spider).ifPresent( asdf -> spider.yHeadRot = this.rotateTowards(spider.yHeadRot, asdf, 9999));
            getYRotD(target.getX(),target.getY(),target.getZ(), spider).ifPresent( asdf -> spider.setYRot( this.rotateTowards(spider.yBodyRot, asdf, 9999)));

            getXRotD(target.getX(),target.getY(),target.getZ(), spider).ifPresent( asdf -> spider.setXRot( this.rotateTowards(spider.yHeadRot, asdf, 9999)));

            if(spider.cooldown <= 0) {
                Vec3 pos = spider.position();
                spider.getJumpControl().jump();
                spider.cooldown = 80;
                spider.tickCount = spider.tickCount - 80;
            }
        }
        for(CatSpark spider : level.getEntitiesOfClass(CatSpark.class,player.getBoundingBox().inflate(16),spiderSpark -> spiderSpark.getOwner() == player)){

            spider.setTarget(target);
            spider.getNavigation().stop();
            spider.getNavigation().createPath(target,16);
            spider.addEffect(new MobEffectInstance(MobEffects.JUMP,80,2));
            spider.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,80, (int) (player.getAttributeValue(Attributes.ATTACK_DAMAGE)/4 - 1)));

            getYRotD(target.getX(),target.getY(),target.getZ(), spider).ifPresent( asdf -> spider.yHeadRot = this.rotateTowards(spider.yHeadRot, asdf, 9999));
            getYRotD(target.getX(),target.getY(),target.getZ(), spider).ifPresent( asdf -> spider.setYRot( this.rotateTowards(spider.yBodyRot, asdf, 9999)));

            getXRotD(target.getX(),target.getY(),target.getZ(), spider).ifPresent( asdf -> spider.setXRot( this.rotateTowards(spider.yHeadRot, asdf, 9999)));

            if(spider.cooldown <= 0) {
                Vec3 pos = spider.position();
                spider.getJumpControl().jump();

                spider.cooldown = 80;
                spider.tickCount = spider.tickCount - 80;

            }
        }
        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
            player.hurt(DamageSource.MAGIC,2);
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        ItemStack itemstack = p_41433_.getItemInHand(p_41434_);
        Player player = p_41433_;

        if (((Player) player).isShiftKeyDown()) {
            CompoundTag nbt;
            if (itemstack.hasTag())
            {
                if(itemstack.getTag().get("Triggerable") != null) {
                    nbt = itemstack.getTag();
                    nbt.remove("Triggerable");
                    player.getInventory().setChanged();
                }
                else{
                    nbt = itemstack.getOrCreateTag();
                    nbt.putInt("Triggerable", 1);
                    player.getInventory().setChanged();
                }

            }
            else
            {
                nbt = itemstack.getOrCreateTag();
                nbt.putInt("Triggerable", 1);
                player.getInventory().setChanged();
            }
            return InteractionResultHolder.success(itemstack);

        }
        if(Patreon.allowed(player, SpellbladeMod.CATUUIDS) && Patreon.cat.contains(player)) {
            CatSpark spider = new CatSpark(ModEntities.CATSPARK.get(),p_41432_);
            spider.setPos(p_41433_.position());
            TagKey<CatVariant> tagkey =CatVariantTags.DEFAULT_SPAWNS;

            Registry.CAT_VARIANT.getTag(tagkey).flatMap((p_218136_) -> {
                return p_218136_.getRandomElement(p_41432_.random);
            }).ifPresent((p_218138_) -> {
                spider.setCatVariant(p_218138_.value());});
            spider.setOwnerUUID(player.getUUID());
            p_41432_.addFreshEntity(spider);
            ((Player)p_41433_).getAttribute(manatick.WARD).setBaseValue(((Player)p_41433_).getAttributes().getBaseValue(manatick.WARD)-10);
            if (((Player)p_41433_).getAttributes().getBaseValue(manatick.WARD) < -21) {
                p_41433_.hurt(DamageSource.MAGIC,2);
            }
            return InteractionResultHolder.success(p_41433_.getItemInHand(p_41434_));
        }
        SpiderSpark spider = new SpiderSpark(ModEntities.SPARK.get(),p_41432_, p_41433_);
        spider.setPos(p_41433_.position());
        p_41432_.addFreshEntity(spider);
        ((Player)p_41433_).getAttribute(manatick.WARD).setBaseValue(((Player)p_41433_).getAttributes().getBaseValue(manatick.WARD)-10);
        if (((Player)p_41433_).getAttributes().getBaseValue(manatick.WARD) < -21) {
            p_41433_.hurt(DamageSource.MAGIC,2);
        }
        return InteractionResultHolder.success(p_41433_.getItemInHand(p_41434_));
    }
    public int getColor() {
        return 4277599;
    }

    @Override
    public int triggerCooldown() {
        return 10;
    }

    @Override
    public boolean trigger(Level level, Player player, float modifier, @Nullable ConduitSpearEntity spear) {
        Entity entity;
        if (spear != null){
            entity = spear;
        }
        else{
            entity = player;
        }
        if (Patreon.allowed(player, SpellbladeMod.CATUUIDS) && Patreon.cat.contains(player)) {

            CatSpark spider = new CatSpark(ModEntities.CATSPARK.get(), level);
            spider.setPos(entity.position());
            if (player.getLastHurtMob() != null) {
                if (player.getLastHurtMob().isAlive()) {
                    spider.setTarget(player.getLastHurtMob());
                }
            }


            ((Player) player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD) - 10);

            if (((Player) player).getAttributes().getBaseValue(manatick.WARD) < -21) {
                player.hurt(DamageSource.MAGIC, 2);
            }
            TagKey<CatVariant> tagkey =CatVariantTags.DEFAULT_SPAWNS;

            Registry.CAT_VARIANT.getTag(tagkey).flatMap((p_218136_) -> {
                return p_218136_.getRandomElement(level.random);
            }).ifPresent((p_218138_) -> {
                spider.setCatVariant(p_218138_.value());});
            spider.setOwnerUUID(player.getUUID());

            level.addFreshEntity(spider);
            return false;

        }
        SpiderSpark spider = new SpiderSpark(ModEntities.SPARK.get(),level, player);
        spider.setPos(entity.position());
        if(player.getLastHurtMob() != null){
            if(player.getLastHurtMob().isAlive()){
                spider.setTarget(player.getLastHurtMob());
            }
        }
        ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-10);

        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
            player.hurt(DamageSource.MAGIC,2);
        }
        level.addFreshEntity(spider);
        return false;
    }
}
