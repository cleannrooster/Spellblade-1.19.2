package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.entity.EssenceBoltEntity;
import com.cleannrooster.spellblademod.entity.ModEntities;
import com.cleannrooster.spellblademod.manasystem.manatick;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EssenceBolt extends Spell{
    public EssenceBolt(Properties p_41383_) {
        super(p_41383_);
    }
    public ChatFormatting color(){
        return ChatFormatting.YELLOW;
    }
    @Override
    public boolean isTargeted() {
        return false;
    }

    @Override
    public int getColor() {
        return 16766720;
    }
    public int getColor2() {
        return 13762473;
    }

    @Override
    public boolean canSpellweave() {
        return false;
    }

    public Item getIngredient1() {return Items.GLOW_BERRIES;};
    public Item getIngredient2() {return Items.GLASS_BOTTLE;};

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack thisStack, ItemStack onStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        return false;
    }
    @Override
    public boolean isTriggerable() {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        EssenceBoltEntity bolt = new EssenceBoltEntity(ModEntities.ESSENCEBOLT.get(),level,player);
        bolt.shootFromRotation(player,player.getXRot(),player.getYRot(),0,4,1);
        if(!level.isClientSide()) {
            level.addFreshEntity(bolt);
        }
        ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
            player.hurt(DamageSource.MAGIC,2);
        }
        return super.use(level, player, hand);
    }
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {



        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
