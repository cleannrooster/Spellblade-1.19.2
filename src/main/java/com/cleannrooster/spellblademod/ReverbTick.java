package com.cleannrooster.spellblademod;

import com.cleannrooster.spellblademod.effects.ReverbInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = "spellblademod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReverbTick {
    /*@SubscribeEvent
    public static void WardOnHit(LivingDamageEvent event){
        if (event.getSource().getEntity() == null){
            return;
        }

        if (event.getSource().getEntity() instanceof Player){
            Player player = (Player) event.getSource().getEntity();
            PlayerMana playerMana = player.getCapability(PlayerManaProvider.PLAYER_MANA).orElse(null);
            playerMana.addMana(24);
        }
    }*/
    public static ArrayList<ReverbInstance> reverbInstances = new ArrayList<>();

    @SubscribeEvent
    public static void reverb(TickEvent.ServerTickEvent event){
        if (event.phase == TickEvent.Phase.START && event.side.isServer()) {
            reverbInstances.removeIf(attackevent -> attackevent.time < 0);
            for (ReverbInstance reverbInstance : reverbInstances) {

                reverbInstance.tick();
            }
        }
    }


}
