package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.StatusEffectsModded;
import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.entity.EnderBindEntity;
import com.cleannrooster.spellblademod.entity.ModEntities;
import com.cleannrooster.spellblademod.manasystem.manatick;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RedstoneBurst extends Spell{

    public RedstoneBurst(Properties p_41383_) {
        super(p_41383_);
    }
    public Item getIngredient1() {return Items.REDSTONE_BLOCK;};
    public Item getIngredient2() {return ModItems.ESSENCEBOLT.get();};
    public ChatFormatting color(){
        return ChatFormatting.DARK_RED;
    }
    @Override
    public boolean isTriggerable() {
        return true;
    }

    @Override
    public boolean canSpellweave() {
        return true;
    }



    @Override
    public boolean isTargeted() {
        return true;
    }

    @Override
    public boolean triggeron(Level worldIn, Player playerIn, LivingEntity target, float modifier3) {
        int modifier = 0;
        int modifier2 = 0;
        if(playerIn.getEffect(StatusEffectsModded.WARDING.get()) != null){
            modifier = playerIn.getEffect(StatusEffectsModded.WARDING.get()).getAmplifier()+1;
        }
        if(playerIn.getEffect(StatusEffectsModded.WARDREAVE.get()) != null){
            modifier2 = playerIn.getEffect(StatusEffectsModded.WARDREAVE.get()).getAmplifier()+1;
        }
        if(playerIn.getEffect(StatusEffectsModded.REDSTONEBURST.get()) != null){
            playerIn.addEffect(new MobEffectInstance(StatusEffectsModded.REDSTONEBURST.get(),playerIn.getEffect(StatusEffectsModded.REDSTONEBURST.get()).getDuration()+40, playerIn.getEffect(StatusEffectsModded.REDSTONEBURST.get()).getAmplifier()+1+modifier2+modifier));
        }
        else{
            playerIn.addEffect(new MobEffectInstance(StatusEffectsModded.REDSTONEBURST.get(),40, modifier2+modifier));
        }
        ((Player)playerIn).getAttribute(manatick.WARD).setBaseValue(((Player) playerIn).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)playerIn).getAttributes().getBaseValue(manatick.WARD)< -21) {

            playerIn.hurt(DamageSource.MAGIC, 2);
        }
        return false;    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand p_41434_) {
        int modifier = 0;
        int modifier2 = 0;
        if(playerIn.getEffect(StatusEffectsModded.WARDING.get()) != null){
            modifier = playerIn.getEffect(StatusEffectsModded.WARDING.get()).getAmplifier()+1;
        }
        if(playerIn.getEffect(StatusEffectsModded.WARDREAVE.get()) != null){
            modifier2 = playerIn.getEffect(StatusEffectsModded.WARDREAVE.get()).getAmplifier()+1;
        }
        if(playerIn.getEffect(StatusEffectsModded.REDSTONEBURST.get()) != null){
            playerIn.addEffect(new MobEffectInstance(StatusEffectsModded.REDSTONEBURST.get(),playerIn.getEffect(StatusEffectsModded.REDSTONEBURST.get()).getDuration()+40, playerIn.getEffect(StatusEffectsModded.REDSTONEBURST.get()).getAmplifier()+1+modifier2+modifier));
        }
        else{
            playerIn.addEffect(new MobEffectInstance(StatusEffectsModded.REDSTONEBURST.get(),40, modifier2+modifier));
        }
        ((Player)playerIn).getAttribute(manatick.WARD).setBaseValue(((Player) playerIn).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)playerIn).getAttributes().getBaseValue(manatick.WARD)< -21) {

            playerIn.hurt(DamageSource.MAGIC, 2);
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

    @Override
    public boolean trigger(Level worldIn, Player playerIn, float modifier3, @Nullable ConduitSpearEntity spear) {
        int modifier = 0;
        int modifier2 = 0;
        if(playerIn.getEffect(StatusEffectsModded.WARDING.get()) != null){
            modifier = playerIn.getEffect(StatusEffectsModded.WARDING.get()).getAmplifier()+1;
        }
        if(playerIn.getEffect(StatusEffectsModded.WARDREAVE.get()) != null){
            modifier2 = playerIn.getEffect(StatusEffectsModded.WARDREAVE.get()).getAmplifier()+1;
        }
        if(playerIn.getEffect(StatusEffectsModded.REDSTONEBURST.get()) != null){
            playerIn.addEffect(new MobEffectInstance(StatusEffectsModded.REDSTONEBURST.get(),playerIn.getEffect(StatusEffectsModded.REDSTONEBURST.get()).getDuration()+40, playerIn.getEffect(StatusEffectsModded.REDSTONEBURST.get()).getAmplifier()+1+modifier2+modifier));
        }
        else{
            playerIn.addEffect(new MobEffectInstance(StatusEffectsModded.REDSTONEBURST.get(),40, modifier2+modifier));
        }
        ((Player)playerIn).getAttribute(manatick.WARD).setBaseValue(((Player) playerIn).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)playerIn).getAttributes().getBaseValue(manatick.WARD)< -21) {

            playerIn.hurt(DamageSource.MAGIC, 2);
        }
        return false;
    }
    public int getColor() {
        return 11272212;
    }
    public int getColor2() {
        return 16777215;
    }

    @Override
    public int triggerCooldown() {
        return 20;
    }
}
