package com.cleannrooster.spellblademod.patreon;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class EmeraldPacket {
    boolean bool;
    UUID entity;
    String string;
    public EmeraldPacket(UUID entity, boolean bool) {
        this.entity =  entity;
        this.bool = bool;
    }

    public EmeraldPacket(FriendlyByteBuf buf) {
        this.entity = buf.readUUID();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(entity);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {
            if(Patreon.emeraldbladeflurry.contains(ctx.getSender().level.getPlayerByUUID(entity))) {
                Patreon.emeraldbladeflurry.remove(ctx.getSender().level.getPlayerByUUID(entity));
                ctx.getSender().sendSystemMessage(Component.translatable("Disabled Emerald Blade Flurry."));

            }
            else{
                Patreon.emeraldbladeflurry.add(ctx.getSender().level.getPlayerByUUID(entity));
                ctx.getSender().sendSystemMessage(Component.translatable("Enabled Emerald Blade Flurry."));

            }
        });
        return true;

    }
}
