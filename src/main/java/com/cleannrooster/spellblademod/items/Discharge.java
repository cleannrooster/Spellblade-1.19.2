package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.entity.BouncingEntity;
import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.manasystem.manatick;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Discharge extends Spell{
    public Discharge(Properties p_41383_) {
        super(p_41383_);
    }
    public Item getIngredient1() {return Items.TNT;};
    public Item getIngredient2() {return ModItems.ESSENCEBOLT.get();};
    public ChatFormatting color(){
        return ChatFormatting.RED;
    }
    @Override
    public boolean isTargeted() {
        return false;
    }

    @Override
    public boolean triggeron(Level worldIn, Player playerIn, LivingEntity target, float modifier) {

        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand p_41434_) {
        ItemStack itemstack = playerIn.getItemInHand(p_41434_);
        Player player = playerIn;
        if((float)((Player)playerIn).getAttribute(manatick.WARD).getBaseValue()/40 > 0) {
            worldIn.explode(playerIn, playerIn.getX(), playerIn.getEyeY(), playerIn.getZ(), (float) ((Player) playerIn).getAttribute(manatick.WARD).getBaseValue() / 40, Explosion.BlockInteraction.NONE);
        }
        ((Player)playerIn).getAttribute(manatick.WARD).setBaseValue(0);
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
    public boolean isTriggerable() {
        return false;
    }

    @Override
    public boolean trigger(Level worldIn, Player playerIn, float modifier, @Nullable ConduitSpearEntity spear) {

        return false;
    }
    public int getColor() {
        return 16724639;
    }

    @Override
    public int getColor2() {
        return 16777215;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack thisStack, ItemStack onStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        return false;
    }
    @Override
    public int triggerCooldown() {
        return 20;
    }
}
