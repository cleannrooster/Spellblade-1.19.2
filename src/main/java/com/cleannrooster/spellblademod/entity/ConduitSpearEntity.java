package com.cleannrooster.spellblademod.entity;

import com.cleannrooster.spellblademod.ReverbTick;
import com.cleannrooster.spellblademod.effects.ReverbInstance;
import com.cleannrooster.spellblademod.items.ModItems;
import com.cleannrooster.spellblademod.items.Spell;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Objects;

public class ConduitSpearEntity extends ThrownTrident {
    List<String> spells;
    public ConduitSpearEntity(EntityType<? extends ConduitSpearEntity> p_37561_, Level p_37562_) {
        super(p_37561_, p_37562_);
        this.spells = null;
    }
    public ConduitSpearEntity(EntityType<? extends ConduitSpearEntity> p_37561_, Level p_37562_, List<String> spells) {
        super(p_37561_, p_37562_);
        this.spells = spells;
    }


    @Override
    public void tick() {
        if (this.firstTick) {
            this.playSound(SoundEvents.TRIDENT_THROW);
        }
        super.tick();
        if (this.inGroundTime > 100) {
            this.discard();
        }
        if (!this.inGround){
            this.setDeltaMovement(this.getDeltaMovement().scale(1/0.99));
        }
        if(this.inGroundTime > 0 && this.inGround && this.getOwner() instanceof Player player && this.inGroundTime % 20 == 5) {
            if (this.spells != null) {
                this.spells.forEach(spellname -> {
                    for (RegistryObject<Item> spell3 : ModItems.ITEMS.getEntries()) {
                        if (spell3.get() instanceof Spell spell) {
                            if (Objects.equals(spell.getDescriptionId(), spellname)) {
                                if (spell.canFail()) {
                                    if (this.spells.contains("item.spellblademod.reverb")) {
                                        ReverbInstance instance = new ReverbInstance(spell, (Player) this.getOwner(), 2, null, InteractionHand.MAIN_HAND, this);
                                        ReverbTick.reverbInstances.add(instance);
                                    }
                                } else {
                                    if (this.spells.contains("item.spellblademod.reverb")) {
                                        ReverbInstance instance = new ReverbInstance(spell, (Player) this.getOwner(), 2, null, InteractionHand.MAIN_HAND, this);
                                        ReverbTick.reverbInstances.add(instance);

                                    }
                                    ((Spell) spell).trigger(level, (Player) this.getOwner(), 1, this);
                                }

                            }

                        }

                    }
                });
            }
        }
    }

    @Override
    protected boolean tryPickup(Player p_150196_) {
        return false;
    }
}
