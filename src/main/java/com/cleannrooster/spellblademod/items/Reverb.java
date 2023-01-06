package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.effects.ReverbInstance;
import com.cleannrooster.spellblademod.entity.BouncingEntity;
import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.manasystem.manatick;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Reverb extends Spell{
    public Reverb(Properties p_41383_) {
        super(p_41383_);
    }
    public Item getIngredient1() {return Items.ECHO_SHARD;};
    public Item getIngredient2() {return ModItems.FLUXITEM.get();};
    public ChatFormatting color(){
        return ChatFormatting.DARK_GREEN;
    }
    @Override
    public boolean isTargeted() {
        return true;
    }

    @Override
    public boolean triggeron(Level worldIn, Player playerIn, LivingEntity target, float modifier) {
        if(!playerIn.getCooldowns().isOnCooldown(this)) {
            ((Player)playerIn).getAttribute(manatick.WARD).setBaseValue(((Player) playerIn).getAttributeBaseValue(manatick.WARD)-20);

            if (((Player)playerIn).getAttributes().getBaseValue(manatick.WARD)< -21) {

                playerIn.hurt(DamageSource.MAGIC, 2);
            }
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand p_41434_) {

        ((Player)playerIn).getAttribute(manatick.WARD).setBaseValue(((Player) playerIn).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)playerIn).getAttributes().getBaseValue(manatick.WARD) < -21) {
            playerIn.hurt(DamageSource.MAGIC,2);
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
    public boolean trigger(Level level, Player playerIn, float modifier, @Nullable ConduitSpearEntity spear) {
        if(!playerIn.getCooldowns().isOnCooldown(this)) {

            ((Player)playerIn).getAttribute(manatick.WARD).setBaseValue(((Player) playerIn).getAttributeBaseValue(manatick.WARD)-20);

            if (((Player)playerIn).getAttributes().getBaseValue(manatick.WARD)< -21) {

                playerIn.hurt(DamageSource.MAGIC, 2);
            }
        }
        return false;
    }
    public int getColor() {
        return 9298659;
    }
    public int getColor2() {
        return 10739123;
    }
    @Override
    public int triggerCooldown() {
        return 20;
    }
}
