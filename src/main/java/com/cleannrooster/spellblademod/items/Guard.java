package com.cleannrooster.spellblademod.items;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Guard extends Item {
    public Guard(Properties p_41383_) {
        super(p_41383_);
    }

    public void guard(Player player, Level level, LivingEntity entity){

    }
    public void guardtick(Player player, Level level,int slot, int count){

    }
    public void guardstart(Player player, Level level, int slot){

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
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        Component text = Component.translatable("Enabled").withStyle(ChatFormatting.GREEN);
        Component text2 = Component.translatable("Right Click to Enable").withStyle(ChatFormatting.DARK_GREEN);
        Component text3 = Component.translatable("While Enabled, this Guard Spell is cast while using a Spellblade").withStyle(ChatFormatting.GRAY);
        p_41423_.add(text3);
        if (p_41421_.hasTag()) {
            if (p_41421_.getTag().get("Triggerable") != null) {
                p_41423_.add(text);
            }
            else{
                p_41423_.add(text2);
            }
        }
        else{
            p_41423_.add(text2);

        }
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
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
