package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.entity.InvisiVex;
import com.cleannrooster.spellblademod.entity.ModEntities;
import com.cleannrooster.spellblademod.entity.VolatileEntity;
import com.cleannrooster.spellblademod.manasystem.manatick;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Volatile extends Spell{

    public Volatile(Properties p_41383_) {
        super(p_41383_);
    }
    public Item getIngredient1() {return Items.FIRE_CHARGE;};
    public Item getIngredient2() {return ModItems.ESSENCEBOLT.get();};
    public ChatFormatting color(){
        return ChatFormatting.DARK_RED;
    }
    @Override
    public boolean isTargeted() {
        return true;
    }

    @Override
    public boolean triggeron(Level level, Player player, LivingEntity target1, float modifier) {

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(6D),livingEntity -> {return FriendshipBracelet.PlayerFriendshipPredicate((Player) player,livingEntity);});          Object[] entitiesarray = entities.toArray();
        boolean flag1 = false;
        if (player.getInventory().contains(ModItems.FRIENDSHIP.get().getDefaultInstance())){
            flag1 = true;
        }
        VolatileEntity volatile5 = new VolatileEntity(ModEntities.VOLATILE_ENTITY.get(),level);
        volatile5.explosionPower = 2;
        volatile5.damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        Random rand = new Random();
        volatile5.setPos(player.position().add( new Vec3(rand.nextDouble(-1, 1),rand.nextDouble(0, 1),rand.nextDouble(-1, 1))));
        volatile5.setOwner(player);
        volatile5.target = target1;
        level.addFreshEntity(volatile5);

        ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
            player.hurt(DamageSource.MAGIC, 2);
        }
            return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            CompoundTag nbt;
            if (itemstack.hasTag())
            {
                if(itemstack.getTag().get("Triggerable") != null) {
                    nbt = itemstack.getTag();
                    nbt.remove("Triggerable");
                }
                else{
                    nbt = itemstack.getOrCreateTag();
                    nbt.putInt("Triggerable", 1);
                }

            }
            else
            {
                nbt = itemstack.getOrCreateTag();
                nbt.putInt("Triggerable", 1);
            }
            return InteractionResultHolder.success(itemstack);

        }
        if (!player.getMainHandItem().isEdible()) {
            ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player)player).getAttributes().getBaseValue(manatick.WARD)-20);
            if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
                player.hurt(DamageSource.MAGIC,2);
            }
            if (!player.isShiftKeyDown()) {

            }
            VolatileEntity volatile1 = new VolatileEntity(ModEntities.VOLATILE_ENTITY.get(),level);
            VolatileEntity volatile2 = new VolatileEntity(ModEntities.VOLATILE_ENTITY.get(),level);
            VolatileEntity volatile3 = new VolatileEntity(ModEntities.VOLATILE_ENTITY.get(),level);
            volatile1.damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            volatile2.damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            volatile3.damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);

            volatile1.setPos(player.getEyePosition().add( player.getViewVector(0).multiply(1+player.getRandom().nextFloat(),1+player.getRandom().nextFloat(),1+player.getRandom().nextFloat())));
            volatile2.setPos(player.getEyePosition().add( player.getViewVector(0).multiply(1+player.getRandom().nextFloat(),1+player.getRandom().nextFloat(),1+player.getRandom().nextFloat())));
            volatile3.setPos(player.getEyePosition().add( player.getViewVector(0).multiply(1+player.getRandom().nextFloat(),1+player.getRandom().nextFloat(),1+player.getRandom().nextFloat())));
            level.addFreshEntity(volatile1);
            level.addFreshEntity(volatile2);
            level.addFreshEntity(volatile3);


        }

        return InteractionResultHolder.fail(itemstack);

    }
    @Override
    public boolean isFoil(ItemStack p_41453_) {
        if (p_41453_.hasTag()){
            if(p_41453_.getTag().getInt("Triggerable") == 1){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean trigger(Level level, Player player, float modifier, @Nullable ConduitSpearEntity spear) {

        Entity entity;
        if (spear != null){
            entity = spear;
        }
        else{
            entity = player;
        }
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class,entity.getBoundingBox().inflate(6D),livingEntity -> {return FriendshipBracelet.PlayerFriendshipPredicate((Player) player,livingEntity);});          Object[] entitiesarray = entities.toArray();
        boolean flag1 = false;
        if (player.getInventory().contains(ModItems.FRIENDSHIP.get().getDefaultInstance())){
            flag1 = true;
        }
        List<LivingEntity> validtargets = new ArrayList<>();
        int entityamount = entitiesarray.length;
        for (int ii = 0; ii < entityamount; ii = ii + 1) {
            LivingEntity target = (LivingEntity) entities.get(ii);
            boolean flag2 = false;
            if (target.getClassification(false).isFriendly() || target instanceof Player || (target instanceof NeutralMob)) {
                flag2 = true;
            }
            if (target != player && target.hasLineOfSight(player)) {
                validtargets.add(target);
            }
        }
        validtargets.removeIf(livingEntity -> {
            if (livingEntity instanceof InvisiVex vex) {
                return vex.owner2 == player;
            } else {
                return false;
            }
        });
        if (!validtargets.isEmpty()){

            if(((Player)player).getAttributes().getBaseValue(manatick.WARD)< -1 && player.getHealth() <= 2)
            {
                return true;
            }
            VolatileEntity volatile1 = new VolatileEntity(ModEntities.VOLATILE_ENTITY.get(),level);
            VolatileEntity volatile2 = new VolatileEntity(ModEntities.VOLATILE_ENTITY.get(),level);
            VolatileEntity volatile3 = new VolatileEntity(ModEntities.VOLATILE_ENTITY.get(),level);
            volatile1.explosionPower = 2;
            volatile3.explosionPower = 2;
            volatile2.explosionPower = 2;
            volatile1.damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            volatile2.damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            volatile3.damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);

            Random rand = new Random();
            volatile1.setPos(entity.position().add( new Vec3(rand.nextDouble(-1, 1),rand.nextDouble(0, 1),rand.nextDouble(-1, 1))));
            volatile2.setPos(entity.position().add( new Vec3(rand.nextDouble(-1, 1),rand.nextDouble(0, 1),rand.nextDouble(-1, 1))));
            volatile3.setPos(entity.position().add( new Vec3(rand.nextDouble(-1, 1),rand.nextDouble(0, 1),rand.nextDouble(-1, 1))));
            volatile1.setOwner(player);
            volatile2.setOwner(player);
            volatile3.setOwner(player);
            if(validtargets.toArray().length == 1){
                volatile1.target = validtargets.get(0);
                level.addFreshEntity(volatile1);

            }
            if(validtargets.toArray().length == 2){
                volatile1.target = validtargets.get(0);
                volatile2.target = validtargets.get(1);
                if (rand.nextBoolean()){
                    volatile3.target = validtargets.get(0);
                }
                else{
                    volatile3.target = validtargets.get(1);
                }
                level.addFreshEntity(volatile1);
                level.addFreshEntity(volatile2);

            }
            if(validtargets.toArray().length == 3){
                volatile1.target = validtargets.get(0);
                volatile2.target = validtargets.get(1);
                volatile3.target = validtargets.get(2);
                level.addFreshEntity(volatile1);
                level.addFreshEntity(volatile2);
                level.addFreshEntity(volatile3);

            }
            if(validtargets.toArray().length > 3){
                int size = validtargets.toArray().length;
                ArrayList<Integer> list = new ArrayList<Integer>(size);
                for(int i = 1; i <= size; i++) {
                    list.add(i);
                }

                ArrayList<Integer> index = new ArrayList<Integer>();
                ArrayList<VolatileEntity> volatilelist = new ArrayList<VolatileEntity>(3);

                volatilelist.add(volatile1);
                volatilelist.add(volatile2);
                volatilelist.add(volatile3);
                int iii = 0;
                while(list.size() > 0) {
                    int toadd = rand.nextInt(list.size());
                    if (iii < 3 && list.get(toadd) > 0 && iii >= 0) {
                        volatilelist.get(iii).target = validtargets.get(list.get(toadd)-1);
                    }
                    if (iii > 10) break;
                    iii++;

                }
                level.addFreshEntity(volatile1);
                level.addFreshEntity(volatile2);
                level.addFreshEntity(volatile3);

            }
            ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-20);

            if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
                player.hurt(DamageSource.MAGIC,2);
            }

        }
        return false;
    }
    public int getColor() {
        return 10944512;
    }
    public int getColor2() {
        return 16751871;
    }

    @Override
    public int triggerCooldown() {
        return 20;
    }
}
