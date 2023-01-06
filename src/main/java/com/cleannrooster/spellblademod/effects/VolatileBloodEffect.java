package com.cleannrooster.spellblademod.effects;

import com.cleannrooster.spellblademod.StatusEffectsModded;
import com.cleannrooster.spellblademod.items.FriendshipBracelet;
import com.mojang.math.Vector3f;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class VolatileBloodEffect extends MobEffect {
    public VolatileBloodEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        if(p_19455_ == 1){
            return true;
        }
        return super.isDurationEffectTick(p_19455_, p_19456_);
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        List<LivingEntity> entities = p_19467_.getLevel().getEntitiesOfClass(LivingEntity.class,p_19467_.getBoundingBox().inflate(4D), livingEntity -> {
            return !livingEntity.hasEffect(StatusEffectsModded.PERFECTBLOOD.get());});
        entities.forEach(entity -> entity.hurt(new DamageSource("volatile"),p_19468_));
        entities.forEach(entity -> {
                    entity.playSound(SoundEvents.GENERIC_EXPLODE);
                    if (p_19467_.getLevel() instanceof ServerLevel level2) {
                        List<ServerPlayer> players = level2.getPlayers(player -> true);

                        for (ServerPlayer player : players) {
                            level2.sendParticles(player, ParticleTypes.EXPLOSION, true, entity.getX(), entity.getY(), entity.getZ(), 1, 0, 0, 0, 0);
                        }

                    }
                }
        );
        super.applyEffectTick(p_19467_, p_19468_);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity p_19469_, AttributeMap p_19470_, int p_19471_) {

        super.removeAttributeModifiers(p_19469_, p_19470_, p_19471_);
    }
}
