package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.entity.BouncingEntity;
import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.manasystem.manatick;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BouncingItem extends Spell{
    public BouncingItem(Properties p_41383_) {
        super(p_41383_);
    }
    public Item getIngredient1() {return Items.MAGMA_CREAM;};
    public Item getIngredient2() {return ModItems.ESSENCEBOLT.get();};

    @Override
    public boolean isTargeted() {
return true;
    }
    public ChatFormatting color(){
        return ChatFormatting.RED;
    }
    @Override
    public boolean triggeron(Level worldIn, Player playerIn, LivingEntity target, float modifier) {
        if(!playerIn.getCooldowns().isOnCooldown(this)) {
            BouncingEntity thrown = new BouncingEntity(playerIn.getLevel(), playerIn);
            thrown.setPos(target.getEyePosition());
            thrown.setDeltaMovement(0 + playerIn.getDeltaMovement().x, -0.5 + playerIn.getDeltaMovement().y, 0 + playerIn.getDeltaMovement().z);
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
        ItemStack itemstack = playerIn.getItemInHand(p_41434_);
        Player player = playerIn;

        if (((Player) playerIn).isShiftKeyDown()) {
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
        ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
            playerIn.hurt(DamageSource.MAGIC,2);
        }

        BouncingEntity thrown = new BouncingEntity(playerIn.getLevel(), playerIn);
        thrown.setPos(player.getEyePosition());
        thrown.setDeltaMovement(playerIn.getDeltaMovement().add(playerIn.getViewVector(0).multiply(0.5,0.5,0.5)));
        if(!worldIn.isClientSide()) {
            playerIn.getLevel().addFreshEntity(thrown);
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
    public boolean trigger(Level worldIn, Player playerIn, float modifier, @Nullable ConduitSpearEntity spear) {
        if(!playerIn.getCooldowns().isOnCooldown(this)) {
            Entity entity;
            if (spear != null){
                entity = spear;
            }
            else{
                entity = playerIn;
            }
            BouncingEntity thrown = new BouncingEntity(entity.getLevel(), playerIn);
            thrown.setPos(entity.getEyePosition());

            thrown.setDeltaMovement(0, -0.5, 0);
            if (!worldIn.isClientSide()) {
                entity.getLevel().addFreshEntity(thrown);
            }
            ((Player)playerIn).getAttribute(manatick.WARD).setBaseValue(((Player) playerIn).getAttributeBaseValue(manatick.WARD)-20);

            if (((Player)playerIn).getAttributes().getBaseValue(manatick.WARD)< -21) {

                playerIn.hurt(DamageSource.MAGIC, 2);
            }
        }
        return false;
    }
    public int getColor() {
        return 16735232;
    }
    public int getColor2() {
        return 12682508;
    }

    @Override
    public int triggerCooldown() {
        return 20;
    }
}
