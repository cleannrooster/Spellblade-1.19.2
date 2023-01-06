package com.cleannrooster.spellblademod;

import com.cleannrooster.spellblademod.items.Flask;
import com.cleannrooster.spellblademod.items.ModItems;
import com.cleannrooster.spellblademod.items.Spell;
import com.cleannrooster.spellblademod.items.Spellblade;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorHandler {
    @SubscribeEvent
    public static void colorevent(RegisterColorHandlersEvent.Item event) {
        event.getItemColors().register((p_92696_, p_92697_) -> {
            return p_92697_ == 0 ? Flask.getColor(p_92696_) : Flask.getColor2(p_92696_);
        }, ModItems.OIL.get());
        for (RegistryObject<Item> spell3 : ModItems.ITEMS.getEntries()) {
            if (spell3.get() instanceof Spell spell) {
                event.getItemColors().register((p_92696_, p_92697_) -> {
                    return p_92697_ == 0 ? spell.getColor2() : spell.getColor();
                }, spell);
            }

        }
        for (RegistryObject<Item> spell3 : ModItems.ITEMS.getEntries()) {
            if (spell3.get() instanceof Spellblade spell) {
                event.getItemColors().register((p_92696_, p_92697_) -> {
                    return p_92697_ == 0 ? spell.getColor(p_92696_) : -1;
                }, spell);
            }

        }
    }

}
