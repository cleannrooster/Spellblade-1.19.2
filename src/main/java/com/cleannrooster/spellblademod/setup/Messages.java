package com.cleannrooster.spellblademod.setup;


import com.cleannrooster.spellblademod.items.AmorphousPacket;
import com.cleannrooster.spellblademod.items.ParticlePacket;
import com.cleannrooster.spellblademod.items.ParticlePacket2;
import com.cleannrooster.spellblademod.manasystem.client.ParticleReverb;
import com.cleannrooster.spellblademod.manasystem.network.*;
import com.cleannrooster.spellblademod.patreon.CatsAndDogsPacket;
import com.cleannrooster.spellblademod.patreon.EmeraldPacket;
import com.cleannrooster.spellblademod.patreon.RefreshPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Messages {

    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation("spellblademod", "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(ClickSpell.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ClickSpell::new)
                .encoder(ClickSpell::toBytes)
                .consumerMainThread(ClickSpell::handle)
                .add();
        net.messageBuilder(RetrieveItem.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RetrieveItem::new)
                .encoder(RetrieveItem::toBytes)
                .consumerMainThread(RetrieveItem::handle)
                .add();
        net.messageBuilder(EmeraldPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(EmeraldPacket::new)
                .encoder(EmeraldPacket::toBytes)
                .consumerMainThread(EmeraldPacket::handle)
                .add();
        net.messageBuilder(RefreshPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RefreshPacket::new)
                .encoder(RefreshPacket::toBytes)
                .consumerMainThread(RefreshPacket::handle)
                .add();
        net.messageBuilder(ParticleReverb.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ParticleReverb::new)
                .encoder(ParticleReverb::toBytes)
                .consumerMainThread(ParticleReverb::handle)
                .add();
        net.messageBuilder(FireworkHandler.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(FireworkHandler::new)
                .encoder(FireworkHandler::toBytes)
                .consumerMainThread(FireworkHandler::handle)
                .add();
        net.messageBuilder(CatPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CatPacket::new)
                .encoder(CatPacket::toBytes)
                .consumerMainThread(CatPacket::handle)
                .add();
        net.messageBuilder(CatsAndDogsPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CatsAndDogsPacket::new)
                .encoder(CatsAndDogsPacket::toBytes)
                .consumerMainThread(CatsAndDogsPacket::handle)
                .add();
        net.messageBuilder(SyncAutouse.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncAutouse::new)
                .encoder(SyncAutouse::toBytes)
                .consumerMainThread(SyncAutouse::handle)
                .add();
        net.messageBuilder(PacketSyncManaToClient.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncManaToClient::new)
                .encoder(PacketSyncManaToClient::toBytes)
                .consumerMainThread(PacketSyncManaToClient::handle)
                .add();
        net.messageBuilder(ParticlePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ParticlePacket::new)
                .encoder(ParticlePacket::toBytes)
                .consumerMainThread(ParticlePacket::handle)
                .add();


        net.messageBuilder(ParticlePacket2.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ParticlePacket2::new)
                .encoder(ParticlePacket2::toBytes)
                .consumerMainThread(ParticlePacket2::handle)
                .add();
        net.messageBuilder(ParticlePacket3.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ParticlePacket3::new)
                .encoder(ParticlePacket3::toBytes)
                .consumerMainThread(ParticlePacket3::handle)
                .add();
        net.messageBuilder(Hurt.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(Hurt::new)
                .encoder(Hurt::toBytes)
                .consumerMainThread(Hurt::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
