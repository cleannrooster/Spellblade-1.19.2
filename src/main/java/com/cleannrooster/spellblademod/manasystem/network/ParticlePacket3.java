package com.cleannrooster.spellblademod.manasystem.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class ParticlePacket3 {
    public static final String MESSAGE_NO_MANA = "message.nomana";
    public static int[] bytearray;
    public  double x = 0;
    public  double y = 0;
    public  double z = 0;
    public  double xx = 0;
    public  double yy = 0;
    public  double zz = 0;
    public  double size = 1;



    public ParticlePacket3(double x1, double y1, double z1) {
        this.x = x1;
        this.y = y1;
        this.z = z1;
    }

    public ParticlePacket3(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        double X = this.x;
        double Y = this.y;
        double Z = this.z;
        ctx.enqueueWork(() -> {

            Minecraft.getInstance().level.playLocalSound(X,Y,Z,(SoundEvents.AMETHYST_CLUSTER_BREAK),SoundSource.PLAYERS,1,1,false);
            for(int ii = 0; ii < 10; ii++){
                Minecraft.getInstance().level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AMETHYST_BLOCK.defaultBlockState()), X + -1 + 2 * Minecraft.getInstance().level.random.nextDouble(), Y - 1 + 2 * Minecraft.getInstance().level.random.nextDouble(), Z - 1 + 2 * Minecraft.getInstance().level.random.nextDouble(), 0, 0, 0);
            }

        });
        return true;
    }
}
