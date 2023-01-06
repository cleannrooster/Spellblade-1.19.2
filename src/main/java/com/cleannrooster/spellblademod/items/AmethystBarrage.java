package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.entity.AmethystEntity;
import com.cleannrooster.spellblademod.entity.ModEntities;
import com.cleannrooster.spellblademod.manasystem.manatick;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AmethystBarrage extends Spell{
    public AmethystBarrage(Properties p_41383_) {
        super(p_41383_);
    }
    public int getColor() {
        return 11361791;
    }
    public int getColor2() {
        return 12699039;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        for(int i = 0; i < 16; i++){
            AmethystEntity amethyst = new AmethystEntity(ModEntities.AMETHYST.get(),p_41432_,p_41433_);
            amethyst.setPos(p_41433_.getEyePosition().add(p_41433_.getViewVector(1).normalize()));
            amethyst.setDeltaMovement(p_41433_.getViewVector(1).add(-0.5+1*p_41432_.getRandom().nextDouble(),-0.5+1*p_41432_.getRandom().nextDouble(),-0.5+1*p_41432_.getRandom().nextDouble()).normalize().multiply(2,2,2));
            amethyst.setOwner(p_41433_);
            amethyst.damage = p_41433_.getAttributeValue(Attributes.ATTACK_DAMAGE);
            p_41432_.addFreshEntity(amethyst);
        }
        ((Player)p_41433_).getAttribute(manatick.WARD).setBaseValue(((Player) p_41433_).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)p_41433_).getAttributes().getBaseValue(manatick.WARD) < -21) {
            p_41433_.hurt(DamageSource.MAGIC,2);
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }
}
