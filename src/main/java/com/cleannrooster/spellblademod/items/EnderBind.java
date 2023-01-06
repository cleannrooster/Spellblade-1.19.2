package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.entity.BouncingEntity;
import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.entity.EnderBindEntity;
import com.cleannrooster.spellblademod.entity.ModEntities;
import com.cleannrooster.spellblademod.manasystem.manatick;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EnderBind extends Spell{
    public EnderBind(Properties p_41383_) {
        super(p_41383_);
    }
    public Item getIngredient1() {return Items.ENDER_EYE;};
    public Item getIngredient2() {return ModItems.ENDERSEYE.get();};
    public ChatFormatting color(){
        return ChatFormatting.DARK_GREEN;
    }
    @Override
    public boolean isTriggerable() {
        return true;
    }

    @Override
    public boolean canSpellweave() {
        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack thisStack, ItemStack onStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        return false;
    }

    @Override
    public boolean isTargeted() {
        return true;
    }

    @Override
    public boolean triggeron(Level worldIn, Player playerIn, LivingEntity target, float modifier) {
        if(!playerIn.getCooldowns().isOnCooldown(this)) {
            EnderBindEntity thrown = new EnderBindEntity(ModEntities.ENDERBIND.get(),worldIn,target);
            thrown.setPos(target.getEyePosition());
            //thrown.setDeltaMovement(0 + playerIn.getDeltaMovement().x, -0.5 + playerIn.getDeltaMovement().y, 0 + playerIn.getDeltaMovement().z);
            if (!worldIn.isClientSide()) {
                playerIn.getLevel().addFreshEntity(thrown);
            }
            ((Player)playerIn).getAttribute(manatick.WARD).setBaseValue(((Player) playerIn).getAttributeBaseValue(manatick.WARD)-20);

            if (((Player)playerIn).getAttributes().getBaseValue(manatick.WARD)< -21) {

                playerIn.hurt(DamageSource.MAGIC, 2);
            }
        }
        return false;    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand p_41434_) {
        List<LivingEntity> list = worldIn.getEntitiesOfClass(LivingEntity.class, playerIn.getBoundingBox().inflate(6));
        List<LivingEntity> list2 = new ArrayList<>();
        for(LivingEntity entity : list){
            if(entity.getBoundingBox().inflate(0.5).clip(playerIn.getEyePosition(),playerIn.getEyePosition().add(playerIn.getViewVector(0).multiply(new Vec3(playerIn.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue(),playerIn.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue(),playerIn.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue())))).isPresent()){
                list2.add(entity);
            };
        }
        if(!list2.isEmpty()){
            LivingEntity target = worldIn.getNearestEntity(list2, TargetingConditions.forNonCombat(),playerIn,playerIn.getX(),playerIn.getY(),playerIn.getZ());
            EnderBindEntity thrown = new EnderBindEntity(ModEntities.ENDERBIND.get(),worldIn,target);
            thrown.setPos(target.getEyePosition());
            if (!worldIn.isClientSide()) {
                playerIn.getLevel().addFreshEntity(thrown);
            }
            ((Player)playerIn).getAttribute(manatick.WARD).setBaseValue(((Player) playerIn).getAttributeBaseValue(manatick.WARD)-20);

            if (((Player)playerIn).getAttributes().getBaseValue(manatick.WARD)< -21) {

                playerIn.hurt(DamageSource.MAGIC, 2);
            }
        }
        else{
            EnderBindEntity thrown = new EnderBindEntity(ModEntities.ENDERBIND.get(),worldIn,playerIn);
            thrown.setPos(playerIn.getEyePosition());
            if (!worldIn.isClientSide()) {
                playerIn.getLevel().addFreshEntity(thrown);
            }
            ((Player)playerIn).getAttribute(manatick.WARD).setBaseValue(((Player) playerIn).getAttributeBaseValue(manatick.WARD)-20);

            if (((Player)playerIn).getAttributes().getBaseValue(manatick.WARD)< -21) {

                playerIn.hurt(DamageSource.MAGIC, 2);
            }
        }
        System.out.println(list);
        System.out.println(list2);
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
    public boolean trigger(Level worldIn, Player playerIn, float modifier, @Nullable ConduitSpearEntity spear) {

        return false;
    }
    public int getColor() {
        return 3104628;
    }
    public int getColor2() {
        return 10026938;
    }

    @Override
    public int triggerCooldown() {
        return 20;
    }
}
