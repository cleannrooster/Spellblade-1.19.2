package com.cleannrooster.spellblademod.manasystem.network;

import com.cleannrooster.spellblademod.patreon.Patreon;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CatPacket {
    boolean bool;
    UUID entity;
    String string;
    public CatPacket(UUID entity, boolean bool) {
        this.entity =  entity;
        this.bool = bool;
    }

    public CatPacket(FriendlyByteBuf buf) {
        this.entity = buf.readUUID();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(entity);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {
            if(Patreon.cat.contains(ctx.getSender().level.getPlayerByUUID(entity))) {
                Patreon.cat.remove(ctx.getSender().level.getPlayerByUUID(entity));
                ctx.getSender().sendSystemMessage(Component.translatable("Disabled Cat Spiders."));

            }
            else{
                Patreon.cat.add(ctx.getSender().level.getPlayerByUUID(entity));
                ctx.getSender().sendSystemMessage(Component.translatable("Enabled Cat Spiders."));

            }
        });
        return true;

    }
}
