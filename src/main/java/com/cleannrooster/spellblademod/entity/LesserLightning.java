package com.cleannrooster.spellblademod.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class LesserLightning extends LightningBolt {
    public LesserLightning(EntityType<? extends LesserLightning> p_20865_, Level p_20866_) {
        super(p_20865_, p_20866_);
    }
    private int life = 6;

    @Override
    public void tick() {
        if(this.firstTick){
            this.playSound(SoundEvents.LIGHTNING_BOLT_IMPACT);
        }
        super.baseTick();
        if (this.life == 6) {
            if (this.level.isClientSide()) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 100.0F, 0.8F + this.random.nextFloat() * 0.2F, false);
            } else {
                Difficulty difficulty = this.level.getDifficulty();
                if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
                }

            }
        }

        --this.life;


        if (this.life >= 0) {
            if (this.random.nextInt(10) < 3) {
                this.life = 5;
                this.seed = this.random.nextLong();
            }
        }else {
                this.discard();
            }

        }

}
