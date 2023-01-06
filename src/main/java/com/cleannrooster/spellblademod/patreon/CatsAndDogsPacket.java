package com.cleannrooster.spellblademod.patreon;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CatsAndDogsPacket {
    boolean bool;
    UUID entity;
    String string;
    public CatsAndDogsPacket(UUID entity, boolean bool) {
        this.entity =  entity;
        this.bool = bool;
    }

    public CatsAndDogsPacket(FriendlyByteBuf buf) {
        this.entity = buf.readUUID();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(entity);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {
            if(Patreon.catsanddogs.contains(ctx.getSender().level.getPlayerByUUID(entity))) {
                Patreon.catsanddogs.remove(ctx.getSender().level.getPlayerByUUID(entity));
                ctx.getSender().sendSystemMessage(Component.translatable("Disabled Cats and Dogs Celebration."));
            }
            else{
                Patreon.catsanddogs.add(ctx.getSender().level.getPlayerByUUID(entity));
                ctx.getSender().sendSystemMessage(Component.translatable("Enabled Cats and Dogs Celebration."));

            }
        });
        return true;

    }
}
