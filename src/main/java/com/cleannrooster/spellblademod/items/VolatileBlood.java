package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.StatusEffectsModded;
import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.entity.InvisiVex;
import com.cleannrooster.spellblademod.entity.ModEntities;
import com.cleannrooster.spellblademod.entity.VolatileEntity;
import com.cleannrooster.spellblademod.manasystem.manatick;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VolatileBlood extends Spell{
    public VolatileBlood(Properties p_41383_) {
        super(p_41383_);
    }
    public Item getIngredient1() {return Items.BLAZE_POWDER;};
    public Item getIngredient2() {return ModItems.FLUXITEM.get();};
    public ChatFormatting color(){
        return ChatFormatting.DARK_RED;
    }
    @Override
    public boolean isTargeted() {
        return true;
    }

    @Override
    public boolean triggeron(Level level, Player player, LivingEntity target1, float modifier) {

        List<LivingEntity> friends = level.getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(12D), livingEntity -> {return !FriendshipBracelet.PlayerFriendshipPredicate((Player) player,livingEntity);});
        List<LivingEntity> enemies = level.getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(6D), livingEntity -> {return FriendshipBracelet.PlayerFriendshipPredicate((Player) player,livingEntity);});
        friends.removeIf(friend -> enemies.contains(friend));
        enemies.removeIf(enemy -> enemy == player);
        Object[] entitiesarray = friends.toArray();
        boolean flag1 = false;
        if (player.getInventory().contains(ModItems.FRIENDSHIP.get().getDefaultInstance())){
            flag1 = true;
        }
        friends.forEach(entity -> entity.addEffect(new MobEffectInstance(StatusEffectsModded.PERFECTBLOOD.get(),100,0)));
        player.addEffect(new MobEffectInstance(StatusEffectsModded.PERFECTBLOOD.get(),100,0));
        if(target1.hasEffect(StatusEffectsModded.VOLATILEBLOOD.get())) {
            target1.addEffect(new MobEffectInstance(StatusEffectsModded.VOLATILEBLOOD.get(), 80, target1.getEffect(StatusEffectsModded.VOLATILEBLOOD.get()).getAmplifier()+(int)player.getAttributeValue(Attributes.ATTACK_DAMAGE)-1));
        }
        else{
            target1.addEffect(new MobEffectInstance(StatusEffectsModded.VOLATILEBLOOD.get(), 80, (int) player.getAttributeValue(Attributes.ATTACK_DAMAGE)-1));
        }
        ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
            player.hurt(DamageSource.MAGIC, 2);
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        List<LivingEntity> friends = level.getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(12D), livingEntity -> {return !FriendshipBracelet.PlayerFriendshipPredicate((Player) player,livingEntity);});
        List<LivingEntity> enemies = level.getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(6D), livingEntity -> {return FriendshipBracelet.PlayerFriendshipPredicate((Player) player,livingEntity);});
        friends.removeIf(friend -> enemies.contains(friend));
        enemies.removeIf(enemy -> enemy == player);

        Object[] entitiesarray = friends.toArray();
        boolean flag1 = false;
        if (player.getInventory().contains(ModItems.FRIENDSHIP.get().getDefaultInstance())){
            flag1 = true;
        }
        friends.forEach(entity -> entity.addEffect(new MobEffectInstance(StatusEffectsModded.PERFECTBLOOD.get(),100,0)));
        player.addEffect(new MobEffectInstance(StatusEffectsModded.PERFECTBLOOD.get(),100,0));
        for(LivingEntity target1: enemies) {
            if (target1.hasEffect(StatusEffectsModded.VOLATILEBLOOD.get())) {

                target1.addEffect(new MobEffectInstance(StatusEffectsModded.VOLATILEBLOOD.get(), 80, target1.getEffect(StatusEffectsModded.VOLATILEBLOOD.get()).getAmplifier() + (int) player.getAttributeValue(Attributes.ATTACK_DAMAGE) - 1));
            } else {
                target1.addEffect(new MobEffectInstance(StatusEffectsModded.VOLATILEBLOOD.get(), 80, (int) player.getAttributeValue(Attributes.ATTACK_DAMAGE) - 1));
            }
            ((Player) player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD) - 20);

            if (((Player) player).getAttributes().getBaseValue(manatick.WARD) < -21) {
                player.hurt(DamageSource.MAGIC, 2);
            }
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));

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

    @Override
    public boolean trigger(Level level, Player player, float modifier, @Nullable ConduitSpearEntity spear) {
        Entity entity;
        if (spear != null){
            entity = spear;
        }
        else{
            entity = player;
        }
        List<LivingEntity> friends = level.getEntitiesOfClass(LivingEntity.class,entity.getBoundingBox().inflate(12D), livingEntity -> {return !FriendshipBracelet.PlayerFriendshipPredicate((Player) player,livingEntity);});
        List<LivingEntity> enemies = level.getEntitiesOfClass(LivingEntity.class,entity.getBoundingBox().inflate(6D), livingEntity -> {return FriendshipBracelet.PlayerFriendshipPredicate((Player) player,livingEntity);});
        friends.removeIf(friend -> enemies.contains(friend));
        enemies.removeIf(enemy -> enemy == player);

        Object[] entitiesarray = friends.toArray();
        boolean flag1 = false;
        if (player.getInventory().contains(ModItems.FRIENDSHIP.get().getDefaultInstance())){
            flag1 = true;
        }
        friends.forEach(entity2 -> entity2.addEffect(new MobEffectInstance(StatusEffectsModded.PERFECTBLOOD.get(),100,0)));
        player.addEffect(new MobEffectInstance(StatusEffectsModded.PERFECTBLOOD.get(),100,0));
        for(LivingEntity target1: enemies) {
            if (target1.getEffect(StatusEffectsModded.VOLATILEBLOOD.get()) != null) {
                target1.addEffect(new MobEffectInstance(StatusEffectsModded.VOLATILEBLOOD.get(), 80, target1.getEffect(StatusEffectsModded.VOLATILEBLOOD.get()).getAmplifier() + (int) player.getAttributeValue(Attributes.ATTACK_DAMAGE) - 1));
            }
            else {
                target1.addEffect(new MobEffectInstance(StatusEffectsModded.VOLATILEBLOOD.get(), 80, (int) player.getAttributeValue(Attributes.ATTACK_DAMAGE) - 1));
            }
            ((Player) player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD) - 20);

            if (((Player) player).getAttributes().getBaseValue(manatick.WARD) < -21) {
                player.hurt(DamageSource.MAGIC, 2);
            }
        }
        return false;
    }
    public int getColor() {
        return 4915201;
    }

    @Override
    public int triggerCooldown() {
        return 20;
    }
}
