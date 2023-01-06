package com.cleannrooster.spellblademod.effects;

import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.items.ConduitSpear;
import com.cleannrooster.spellblademod.items.FriendshipBracelet;
import com.cleannrooster.spellblademod.items.ParticlePacket2;
import com.cleannrooster.spellblademod.setup.Messages;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class RedstoneBurstEffect extends MobEffect {
    public RedstoneBurstEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        if(p_19467_ instanceof Player player) {
            int radius = (int)Math.ceil((2.15443469003*Math.pow(p_19468_+1,1F/3F)));
            if(player.getLevel() instanceof ServerLevel serverLevel) {

                for (ServerPlayer player1 : serverLevel.players()
                ) {
                    Messages.sendToPlayer(new ParticlePacket2(player.getX(), player.getEyeY(), player.getZ(), player.getX(), player.getEyeY(), player.getZ(),radius), player1);
                }
            }
            for (LivingEntity entity : player.getLevel().getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(radius),living -> FriendshipBracelet.PlayerFriendshipPredicate(player,living))
            ) {
                if(entity != player && player.hasLineOfSight(entity)) {
                    entity.invulnerableTime = 0;
                    entity.hurt(new EntityDamageSource("redstoneburst", player), (float) (player.getAttributeValue(Attributes.ATTACK_DAMAGE) / 2));
                }
            }
            player.removeEffect(this);

            if(p_19468_ != 0) {
                player.addEffect(new MobEffectInstance(this, 10, (int) (2* p_19468_ / 3)));
            }
            else{
            }
        }

        super.applyEffectTick(p_19467_, p_19468_);

    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        if(p_19455_ % 10 == 1){
            return true;
        }
        return super.isDurationEffectTick(p_19455_, p_19456_);
    }
}
