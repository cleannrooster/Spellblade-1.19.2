package com.cleannrooster.spellblademod;

import com.cleannrooster.spellblademod.items.ModItems;
import com.cleannrooster.spellblademod.items.ParticlePacket;
import com.cleannrooster.spellblademod.items.Spellblade;
import com.cleannrooster.spellblademod.items.WardArmorItem;
import com.cleannrooster.spellblademod.manasystem.manatick;
import com.cleannrooster.spellblademod.manasystem.network.Hurt;
import com.cleannrooster.spellblademod.setup.Messages;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.icu.text.CaseMap;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.pow;
@Mod.EventBusSubscriber(modid = "spellblademod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorHandler {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void damageevent(LivingDamageEvent event){
        Random rand = new Random();

        if (event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            float amount = event.getAmount();
            event.setAmount((float) Math.max(0,(amount - player.getAttribute(manatick.WARD).getValue()/40)));
            player.getAttribute(manatick.WARD).setBaseValue(Math.max(0,player.getAttribute(manatick.WARD).getValue() - amount*40));

        }
    }
}
