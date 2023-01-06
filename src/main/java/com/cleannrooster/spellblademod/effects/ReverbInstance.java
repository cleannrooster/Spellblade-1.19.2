package com.cleannrooster.spellblademod.effects;

import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.items.Flask;
import com.cleannrooster.spellblademod.items.Reverb;
import com.cleannrooster.spellblademod.items.Spell;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class ReverbInstance {
    public int time;
    Player player;
    Spell spell;
    int mode;
    LivingEntity target;
    InteractionHand hand;
    ConduitSpearEntity spear;
    public ReverbInstance(Spell p_19451_, Player play, int mode, LivingEntity target, InteractionHand hand, @Nullable ConduitSpearEntity spear) {
        this.time = 10;
        this.player = play;
        this.spell = p_19451_;
        this.mode = mode;
        this.target = target;
        this.hand = hand;
        this.spear = spear;
    }
    public void tick() {
        if (this.time == 0 && !(this.spell instanceof Reverb)) {
            this.execute(this.spell, this.mode, this.target, this.hand, this.spear);
        }
        this.time--;
    }
    public void execute(Spell spell, int mode, @Nullable LivingEntity target, InteractionHand hand,@Nullable ConduitSpearEntity spear ){
        if(mode == 0){
            spell.triggeron(this.player.getLevel(),this.player,target,1);
        }
        if(mode == 1){
            spell.use(this.player.getLevel(),this.player, hand);
        }
        if(mode == 2){
            spell.trigger(this.player.getLevel(),this.player, 1, spear);
        }
        if(mode == 3){
            spell.failState(this.player.getLevel(),this.player, hand);
        }
    }

}
