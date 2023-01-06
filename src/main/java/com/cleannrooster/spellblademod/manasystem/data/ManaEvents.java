package com.cleannrooster.spellblademod.manasystem.data;

import com.cleannrooster.spellblademod.manasystem.client.GUI;
import com.cleannrooster.spellblademod.manasystem.manatick;
import com.cleannrooster.spellblademod.manasystem.network.Hurt;
import com.cleannrooster.spellblademod.patreon.Listener;
import com.cleannrooster.spellblademod.setup.Messages;
import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;


public class ManaEvents {

    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        // Don't do anything client side
        if (event.level.isClientSide()) {
            return;
        }
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        ManaManager.tick(event.level);
    }

}
