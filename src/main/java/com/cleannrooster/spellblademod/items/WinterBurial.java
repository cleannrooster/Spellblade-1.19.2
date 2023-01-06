package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.entity.ModEntities;
import com.cleannrooster.spellblademod.entity.WinterBurialEntity;
import com.cleannrooster.spellblademod.manasystem.manatick;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import static java.lang.Math.sqrt;

public class WinterBurial extends Spell{

    public WinterBurial(Properties p_41383_) {
        super(p_41383_);
    }
    public ChatFormatting color(){
        return ChatFormatting.WHITE;
    }
    public Item getIngredient1() {return Items.PACKED_ICE;};
    public Item getIngredient2() {return ModItems.ESSENCEBOLT.get();};

    @Override
    public int triggerCooldown() {
        return 20;
    }
    public int getColor() {
        return 13434879;
    }
    public int getColor2() {
        return 12765951;
    }

    @Override
    public boolean isTargeted() {
        return true;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (((Player) player).isShiftKeyDown()) {
            CompoundTag nbt;
            if (itemstack.hasTag())
            {
                if(itemstack.getTag().get("Triggerable") != null) {
                    nbt = itemstack.getTag();
                    nbt.remove("Triggerable");
                    player.getInventory().setChanged();
                }
                else{
                    nbt = itemstack.getOrCreateTag();
                    nbt.putInt("Triggerable", 1);
                    player.getInventory().setChanged();
                }

            }
            else
            {
                nbt = itemstack.getOrCreateTag();
                nbt.putInt("Triggerable", 1);
                player.getInventory().setChanged();
            }
            return InteractionResultHolder.success(itemstack);

        }
        if(Impale.getPlayerPOVHitResult(level,player, ClipContext.Fluid.NONE, player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue()).getType() == HitResult.Type.BLOCK) {
            new WinterBurialEntity(ModEntities.WINTERBURIAL.get(), level, player,Impale.getPlayerPOVHitResult(level,player, ClipContext.Fluid.NONE, player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue()).getBlockPos() , 3);
            ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-20);

            if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
                player.hurt(DamageSource.MAGIC,2);
            }
        }
        return super.use(level, player, hand);
    }
    @Override
    public boolean trigger(Level level, Player player, float modifier, @Nullable ConduitSpearEntity spear) {
        Entity entity;
        Vec3 modifier2;
        if (spear != null){
            entity = spear;
            modifier2 = new Vec3(0,2,0);

        }
        else{
            entity = player;
            modifier2 = new Vec3(0,0,0);

        }
        boolean activated = false;
        for(int ii = 0; ii < 3; ii++){
            double xRand = -1+level.random.nextDouble()*2;
            double zRand = -1+level.random.nextDouble()*2;
            double d0 = sqrt(xRand*xRand+zRand*zRand);
            BlockHitResult result = level.clip(new ClipContext(entity.getEyePosition().add(modifier2),new Vec3(entity.getX()+20*xRand/d0,entity.getY()-20*.1666666,entity.getZ()+20*zRand/d0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,entity));
            if(result.getType() == HitResult.Type.BLOCK && result.distanceTo(player) > 3) {
                new WinterBurialEntity(ModEntities.WINTERBURIAL.get(), level, player, result.getBlockPos(), 3);
                activated = true;
            }
        }
        if (activated){
            ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-20);

            if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
                player.hurt(DamageSource.MAGIC,2);
            }
        }
        return false;
    }
    @Override
    public boolean triggeron(Level level, Player player, LivingEntity target, float modifier) {
        if(level.getBlockState(target.getOnPos()).isSuffocating(level,target.getOnPos())) {

            new WinterBurialEntity(ModEntities.WINTERBURIAL.get(), level, player, target.getOnPos(), 3);
            ((Player) player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD) - 20);

            if (((Player) player).getAttributes().getBaseValue(manatick.WARD) < -21) {
                player.hurt(DamageSource.MAGIC, 2);
            }
        }
/*
        for(int ii = 0; ii < 3; ii++){
            double xRand = level.random.nextDouble(-1,1);
            double zRand = level.random.nextDouble(-1,1);
            double d0 = sqrt(xRand*xRand+zRand*zRand);
            BlockHitResult result = level.clip(new ClipContext(player.getEyePosition(),new Vec3(player.getX()+30*xRand/d0,player.getY()-30*.1666666,player.getZ()+30*zRand/d0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,player));
            if(result.getType() == HitResult.Type.BLOCK && result.distanceTo(player) > 3) {
                new WinterBurialEntity(ModEntities.WINTERBURIAL.get(), level, player, result.getBlockPos(), 3);
            }
        }*/
        return false;
    }
    @Override
    public InteractionResult interactLivingEntity(ItemStack p_41398_, Player p_41399_, LivingEntity p_41400_, InteractionHand p_41401_) {
        triggeron(p_41399_.getLevel(),p_41399_,p_41400_,1);
        return InteractionResult.sidedSuccess(false);
    }
}
