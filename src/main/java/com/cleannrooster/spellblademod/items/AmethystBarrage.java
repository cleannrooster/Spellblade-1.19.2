package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.entity.AmethystEntity;
import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.entity.ModEntities;
import com.cleannrooster.spellblademod.manasystem.manatick;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

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
    public ChatFormatting color() {
        return ChatFormatting.DARK_PURPLE;
    }

    public Item getIngredient2() {return ModItems.SPINNING.get();};
    public Item getIngredient1() {return ModItems.DAGGER.get();};
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
    public boolean triggeron(Level level, Player player, LivingEntity target, float modifier) {
        for(int i = 0; i < 12; i++){
            AmethystEntity amethyst = new AmethystEntity(ModEntities.AMETHYST.get(),level,player);
            amethyst.setPos(target.position().add(new Vec3(0,target.getBoundingBox().getYsize()+1,0)));
            amethyst.setDeltaMovement(new Vec3(-0.5+1*player.getRandom().nextDouble(),1-0.5+1*player.getRandom().nextDouble(),-0.5+1*player.getRandom().nextDouble()));
            amethyst.setOwner(player);
            amethyst.damage = player.getAttributeValue(Attributes.ATTACK_DAMAGE)/2;
            level.addFreshEntity(amethyst);
        }
        ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
            player.hurt(DamageSource.MAGIC,2);
        }
        return super.triggeron(level, player, target, modifier);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        for(int i = 0; i < 32; i++){
            AmethystEntity amethyst = new AmethystEntity(ModEntities.AMETHYST.get(),p_41432_,p_41433_);
            amethyst.setPos(p_41433_.getEyePosition().add(p_41433_.getViewVector(1).normalize()));
            amethyst.setDeltaMovement(p_41433_.getViewVector(1).add(-0.5+1*p_41432_.getRandom().nextDouble(),-0.5+1*p_41432_.getRandom().nextDouble(),-0.5+1*p_41432_.getRandom().nextDouble()).normalize().multiply(2,2,2));
            amethyst.setOwner(p_41433_);
            amethyst.damage = p_41433_.getAttributeValue(Attributes.ATTACK_DAMAGE)/2;
            p_41432_.addFreshEntity(amethyst);
        }
        ((Player)p_41433_).getAttribute(manatick.WARD).setBaseValue(((Player) p_41433_).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)p_41433_).getAttributes().getBaseValue(manatick.WARD) < -21) {
            p_41433_.hurt(DamageSource.MAGIC,2);
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public boolean trigger(Level level, Player player, float modifier, @Nullable ConduitSpearEntity spear) {
        Entity target = player;
        if(spear != null){
            target = spear;
        }
        for(int i = 0; i < 24; i++){
            AmethystEntity amethyst = new AmethystEntity(ModEntities.AMETHYST.get(),level,player);
            amethyst.setPos(target.position().add(new Vec3(0,target.getBoundingBox().getYsize()+1,0)));
            amethyst.setDeltaMovement(new Vec3(-0.5+1*player.getRandom().nextDouble(),1-0.5+1*player.getRandom().nextDouble(),-0.5+1*player.getRandom().nextDouble()));
            amethyst.setOwner(player);
            amethyst.damage = player.getAttributeValue(Attributes.ATTACK_DAMAGE)/2;
            level.addFreshEntity(amethyst);
        }
        ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
            player.hurt(DamageSource.MAGIC,2);
        }
        return super.trigger(level, player, modifier, spear);
    }
}
