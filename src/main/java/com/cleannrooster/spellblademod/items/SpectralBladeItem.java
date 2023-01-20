package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.entity.AmethystEntity;
import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.entity.ModEntities;
import com.cleannrooster.spellblademod.entity.SpectralBlades;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SpectralBladeItem extends Spell{
    public SpectralBladeItem(Properties p_41383_) {
        super(p_41383_);
    }
    public int getColor() {
        return 11361791;
    }
    public int getColor2() {
        return 5081087;
    }

    @Override
    public ChatFormatting color() {
        return ChatFormatting.DARK_PURPLE;
    }

    public Item getIngredient2() {return ModItems.ESSENCEBOLT.get();};
    public Item getIngredient1() {return Items.AMETHYST_SHARD;};
    @Override
    public boolean isTriggerable() {
        return false;
    }

    @Override
    public boolean canSpellweave() {
        return false;
    }

    @Override
    public boolean isTargeted() {
        return false;
    }

    @Override
    public boolean triggeron(Level level, Player player, LivingEntity target, float modifier) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        SpectralBlades amethyst = new SpectralBlades(ModEntities.SPECTRALBLADES.get(),p_41432_,p_41433_);
        amethyst.setPos(p_41433_.getEyePosition().add(p_41433_.getViewVector(1).normalize()));
        amethyst.setDeltaMovement(p_41433_.getViewVector(1).multiply(2,2,2));
        amethyst.setOwner(p_41433_);

        amethyst.damage = (float) (p_41433_.getAttributeValue(Attributes.ATTACK_DAMAGE));
        SpectralBlades amethyst2 = new SpectralBlades(ModEntities.SPECTRALBLADES.get(),p_41432_,p_41433_);

        amethyst2.setPos(p_41433_.getEyePosition().add(p_41433_.getViewVector(1).normalize()));
        amethyst2.setDeltaMovement(p_41433_.getViewVector(1).multiply(2,2,2));
        amethyst2.setOwner(p_41433_);
        amethyst2.damage = (float) (p_41433_.getAttributeValue(Attributes.ATTACK_DAMAGE));
        SpectralBlades amethyst3 = new SpectralBlades(ModEntities.SPECTRALBLADES.get(),p_41432_,p_41433_);

        amethyst3.setPos(p_41433_.getEyePosition().add(p_41433_.getViewVector(1).normalize()));
        amethyst3.setDeltaMovement(p_41433_.getViewVector(1).multiply(2,2,2));
        amethyst3.setOwner(p_41433_);
        amethyst3.damage = (float) (p_41433_.getAttributeValue(Attributes.ATTACK_DAMAGE));
        amethyst.addBuddies(amethyst2);
        amethyst.addBuddies(amethyst3);
        amethyst2.addBuddies(amethyst2);

        amethyst2.addBuddies(amethyst);
        amethyst3.addBuddies(amethyst);
        amethyst3.addBuddies(amethyst2);

        p_41432_.addFreshEntity(amethyst);
        p_41432_.addFreshEntity(amethyst2);
        p_41432_.addFreshEntity(amethyst3);
        ((Player)p_41433_).getAttribute(manatick.WARD).setBaseValue(((Player) p_41433_).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)p_41433_).getAttributes().getBaseValue(manatick.WARD) < -21) {
            p_41433_.hurt(DamageSource.MAGIC,2);
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public boolean trigger(Level level, Player player, float modifier, @Nullable ConduitSpearEntity spear) {
        return false;
    }
}
