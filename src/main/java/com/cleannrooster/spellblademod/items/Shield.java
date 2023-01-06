package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.entity.ModEntities;
import com.cleannrooster.spellblademod.entity.ShieldEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Shield extends Guard{

    public Shield(Properties p_41383_) {
        super(p_41383_);
    }

    public void guard(Player player, Level level, LivingEntity entity){

    }
    public void guardtick(Player player, Level level,int slot, int count){
        if(player.getUseItem().getItem() instanceof Spellblade blade){
            CompoundTag nbt = player.getUseItem().getOrCreateTag();
            nbt.putBoolean("shield", true);
            nbt.putInt("CustomModelData", 5);
        }
    }
    public void guardstart(Player player, Level level, int slot){
        ShieldEntity shield = new ShieldEntity(ModEntities.SHIELD.get(),level);
        shield.setPos(player.getEyePosition().add(player.getViewVector(0).multiply(2,2,2)));
        shield.setOwner(player);
        shield.setXRot(player.getXRot());
        shield.setYRot(player.getYHeadRot());
        if(!level.isClientSide()){
            level.addFreshEntity(shield);
        }
        if(player.getUseItem().getItem() instanceof Spellblade blade){
            CompoundTag nbt = player.getUseItem().getOrCreateTag();
            nbt.putBoolean("shield", true);
            nbt.putInt("CustomModelData", 5);

        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack thisStack, ItemStack onStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if(clickAction == ClickAction.SECONDARY){
            if (thisStack.hasTag()) {
                if (thisStack.getTag().get("Triggerable") != null) {
                    CompoundTag nbt = thisStack.getTag();
                    nbt.remove("Triggerable");
                    return true;
                } else {
                    CompoundTag nbt = thisStack.getOrCreateTag();
                    nbt.putInt("Triggerable", 1);
                    return true;

                }

            } else {
                CompoundTag nbt = thisStack.getOrCreateTag();
                nbt.putInt("Triggerable", 1);
                return true;

            }
        }
        return false;
    }
}
