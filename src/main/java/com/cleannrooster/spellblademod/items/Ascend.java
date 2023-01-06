package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.StatusEffectsModded;
import com.cleannrooster.spellblademod.manasystem.manatick;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import static java.lang.Math.sqrt;

public class Ascend extends Guard{
    public Ascend(Properties p_41383_) {
        super(p_41383_);
    }

    public void guard(Player player, Level level, LivingEntity entity){

    }
    public void guardtick(Player player, Level level,int slot, int count){
        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) > -21) {
            BlockHitResult result = level.clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(0, -player.getMaxFallDistance() * 2, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, player));
            double hordy = 0;
            if (result.getType() == HitResult.Type.BLOCK) {
                double distance = sqrt(result.getBlockPos().distToCenterSqr(player.getPosition(1)));
                if (distance < player.getMaxFallDistance() * 2) {
                    hordy = Math.max(0, player.getViewVector(1).y() - player.getDeltaMovement().y);
                    player.setDeltaMovement(player.getDeltaMovement().add(-0, hordy / 2, -0));

                }


            }
            double dx = player.getViewVector(1).x;
            if (dx > 1) dx = 1;
            if (dx < -1) dx = -1;
            double dz = player.getViewVector(1).z;
            if (dz > 1) dz = 1;
            if (dz < -1) dz = -1;
            player.setDeltaMovement(player.getDeltaMovement().add(dx * player.getAttributeValue(Attributes.MOVEMENT_SPEED), -0, dz * player.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-((float)player.getUseItem().getItem().getUseDuration(player.getUseItem())-count)/80);

        }
        else{
            player.getCooldowns().addCooldown(player.getUseItem().getItem(),160);
            player.stopUsingItem();
            if(player.hasEffect(StatusEffectsModded.WARDING.get())) {
                player.removeEffect(StatusEffectsModded.WARDING.get());
            }
        }
    }
    public void guardstart(Player player, Level level, int slot){

    }
}
