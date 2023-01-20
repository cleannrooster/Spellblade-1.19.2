package com.cleannrooster.spellblademod.effects;

import com.cleannrooster.spellblademod.StatusEffectsModded;
import com.cleannrooster.spellblademod.items.FriendshipBracelet;
import com.cleannrooster.spellblademod.items.ParticlePacket2;
import com.cleannrooster.spellblademod.manasystem.manatick;
import com.cleannrooster.spellblademod.setup.Messages;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Map;

public class WardAbsorption extends MobEffect {
    public WardAbsorption(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }
    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {

        super.applyEffectTick(p_19467_, p_19468_);

    }
    public void removeAttributeModifiers(LivingEntity p_19417_, AttributeMap p_19418_, int p_19419_) {
        p_19417_.setAbsorptionAmount(p_19417_.getAbsorptionAmount() - (float)(1 * (p_19419_ + 1)));
        super.removeAttributeModifiers(p_19417_, p_19418_, p_19419_);
    }

    public void addAttributeModifiers(LivingEntity p_19421_, AttributeMap p_19422_, int p_19423_) {
        p_19421_.setAbsorptionAmount(p_19421_.getAbsorptionAmount() + (float)(1 * (p_19423_ + 1)));
        super.addAttributeModifiers(p_19421_, p_19422_, p_19423_);
    }

}
