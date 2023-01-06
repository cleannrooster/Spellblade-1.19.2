package com.cleannrooster.spellblademod.items;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class ParticlePacket2 {
    public static final String MESSAGE_NO_MANA = "message.nomana";
    public static int[] bytearray;
    public static double x = 0;
    public static double y = 0;
    public static double z = 0;
    public static double xx = 0;
    public static double yy = 0;
    public static double zz = 0;
    public static double size = 1;



    public ParticlePacket2(double x1, double y1, double z1, double x2, double y2, double z2, double big) {
        x = x1;
        y = y1;
        z = z1;
        xx = x2;
        yy = y2;
        zz = z2;
        size = big;
    }

    public ParticlePacket2(FriendlyByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        xx = buf.readDouble();
        yy = buf.readDouble();
        zz = buf.readDouble();
        size = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeDouble(xx);
        buf.writeDouble(yy);
        buf.writeDouble(zz);
        buf.writeDouble(size);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            int i = 0;
            int num_pts = (int) (5*4*Math.pow(size,2));
            int num_pts_line = 25;
            if(Minecraft.getInstance().level != null)
            Minecraft.getInstance().level.playLocalSound(new BlockPos((int)x,(int) y, (int) z), SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 1.0F,1.0F,false);
            if(!(x == xx)) {

                for (int iii = 0; iii < num_pts_line; iii++) {
                    double X = x + (xx - x) * ((double) iii / (num_pts_line));
                    double Y = y + (yy - y) * ((double) iii / (num_pts_line));
                    double Z = z + (zz - z) * ((double) iii / (num_pts_line));
                    Minecraft.getInstance().level.addParticle(DustParticleOptions.REDSTONE, X, Y, Z, 0, 0, 0);
                }
            }
            for (i = 0; i <= num_pts; i = i + 1) {
                double[] indices = IntStream.rangeClosed(0, (int) ((num_pts)))
                        .mapToDouble(xxx -> xxx).toArray();

                double phi = Math.acos(1 - 2 * indices[i] / num_pts);
                double theta = Math.PI * (1 + Math.pow(5, 0.5) * indices[i]);
                double x2 = cos(theta) * sin(phi);
                double y2 = Math.sin(theta) * sin(phi);
                double z2 = cos(phi);

                Minecraft.getInstance().level.addParticle((ParticleOptions) DustParticleOptions.REDSTONE, xx + size * x2, yy + size * y2, zz + size * z2, 0, 0, 0);
            }
            for(int ii = 0; ii < 4*Math.pow(size,3)/3; ii++) {
                double d0 = Minecraft.getInstance().level.random.nextGaussian() * 0.02D;
                double d1 = Minecraft.getInstance().level.random.nextGaussian() * 0.02D;
                double d2 = Minecraft.getInstance().level.random.nextGaussian() * 0.02D;
                Minecraft.getInstance().level.addParticle(ParticleTypes.ELECTRIC_SPARK, xx+size* (-1+Minecraft.getInstance().level.random.nextDouble()*2), yy+size* (-1+Minecraft.getInstance().level.random.nextDouble()*2), zz+size* (-1+Minecraft.getInstance().level.random.nextDouble()*2), d0, d1, d2);
            }

        });
        return true;
    }
}
