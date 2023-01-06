package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.SpellbladeMod;
import com.cleannrooster.spellblademod.StatusEffectsModded;
import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.entity.ModEntities;
import com.cleannrooster.spellblademod.entity.sword1;
import com.cleannrooster.spellblademod.manasystem.manatick;
import com.cleannrooster.spellblademod.patreon.Patreon;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BladeFlurry extends Spell{
    public BladeFlurry(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public int triggerCooldown() {
        return 32;
    }
    public int getColor() {
        return 11361791;
    }
    public int getColor2() {
        return 12682647;
    }

    public Item getIngredient1() {return Items.AMETHYST_SHARD;};
    public Item getIngredient2() {return ModItems.ESSENCEBOLT.get();};
    public ChatFormatting color(){
        return ChatFormatting.DARK_PURPLE;
    }
    @Override
    public boolean isTriggerable() {
        return true;
    }

    @Override
    public boolean isTargeted() {
        return true;
    }

    @Override
    public boolean triggeron(Level level, Player player, LivingEntity target, float modifier) {
        sword1[] swords = new sword1[32];
        for(int i = 0; i < swords.length; i++) {
            swords[i] = new sword1(ModEntities.SWORD.get(), level, player, (ConduitSpearEntity)null);
            swords[i].setPos(player.getEyePosition().add(player.getViewVector(0).multiply(3.5,3.5,3.5)));
            swords[i].number = i + 1;
            swords[i].tickCount = -i-16;
            swords[i].mode = 2;
            swords[i].target = target;


      /*  try {
            if(string().contains(player.getStringUUID())) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
      /*  if(SpellbladeMod.UUIDS.contains(player.getStringUUID())) {
            swords[i].setCustomName(new TextComponent("emerald"));
        */
            int custom1 = 0;
        int custom2 = 0;

        if(player.getEffect(StatusEffectsModded.WARDING.get()) != null ) {
                custom1 = player.getEffect(StatusEffectsModded.WARDING.get()).getAmplifier()+1;
            }
            if(player.getEffect(StatusEffectsModded.WARDREAVE.get()) != null ) {
                custom2 = player.getEffect(StatusEffectsModded.WARDREAVE.get()).getAmplifier()+1;
            }

            swords[i].setCustomName(Component.translatable(String.valueOf(Math.min(4,custom1+custom2))));

            if(!level.isClientSide() && Patreon.allowed(player, SpellbladeMod.UUIDS) && Patreon.emeraldbladeflurry.contains(player)) {
                swords[i].setCustomName(Component.translatable("emerald"));
            }
            swords[i].damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);

            level.addFreshEntity(swords[i]);
        }

        ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
            player.hurt(DamageSource.MAGIC,2);
        }

        return super.triggeron(level, player, target, modifier);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && itemstack.getItem() instanceof Spell) {
            CompoundTag nbt;
            if (itemstack.hasTag()) {
                if (itemstack.getTag().get("Triggerable") != null) {
                    nbt = itemstack.getTag();
                    nbt.remove("Triggerable");
                } else {
                    nbt = itemstack.getOrCreateTag();
                    nbt.putInt("Triggerable", 1);
                }

            } else {
                nbt = itemstack.getOrCreateTag();
                nbt.putInt("Triggerable", 1);
            }
            return InteractionResultHolder.success(itemstack);

        }
        sword1[] swords = new sword1[20];

        for(int i = 0; i < swords.length; i++) {
            swords[i] = new sword1(ModEntities.SWORD.get(), level, player,player.getViewVector(0));
            swords[i].setPos(player.getEyePosition().add(-0.3+level.random.nextDouble()*0.6,-0.3+level.random.nextDouble()*0.6,-0.3+level.random.nextDouble()*0.6).add(2*player.getBbWidth()*player.getViewVector(0).x,2*player.getBbWidth()*player.getViewVector(0).y,2*player.getBbWidth()*player.getViewVector(0).z));
            swords[i].number = i + 1;
            swords[i].tickCount = -i-1;
            swords[i].mode = 3;
            swords[i].damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            int custom1 = 0;
            int custom2 = 0;

            if(player.getEffect(StatusEffectsModded.WARDING.get()) != null ) {
                custom1 = player.getEffect(StatusEffectsModded.WARDING.get()).getAmplifier()+1;
            }
            if(player.getEffect(StatusEffectsModded.WARDREAVE.get()) != null ) {
                custom2 = player.getEffect(StatusEffectsModded.WARDREAVE.get()).getAmplifier()+1;
            }

            swords[i].setCustomName(Component.translatable(String.valueOf(Math.min(4,custom1+custom2))));

            double d0 = player.getViewVector(0).horizontalDistance();
            if(!level.isClientSide() && Patreon.allowed(player, SpellbladeMod.UUIDS) && Patreon.emeraldbladeflurry.contains(player)) {
                swords[i].setCustomName(Component.translatable("emerald"));
            }

            level.addFreshEntity(swords[i]);

        }

        ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
            player.hurt(DamageSource.MAGIC,2);
        }
        return InteractionResultHolder.success(itemstack);

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
        sword1[] swords = new sword1[32];
        Vec3 pos = player.getEyePosition();
        if(spear != null){
            pos = spear.getEyePosition();
        }
        for(int i = 0; i < swords.length; i++) {
            swords[i] = new sword1(ModEntities.SWORD.get(), level, player, spear);
            swords[i].setPos(pos);
            swords[i].number = i + 1;
            swords[i].tickCount = -i;
            swords[i].mode = 1;

            int custom1 = 0;
            int custom2 = 0;

            if(player.getEffect(StatusEffectsModded.WARDING.get()) != null ) {
                custom1 = player.getEffect(StatusEffectsModded.WARDING.get()).getAmplifier()+1;
            }
            if(player.getEffect(StatusEffectsModded.WARDREAVE.get()) != null ) {
                custom2 = player.getEffect(StatusEffectsModded.WARDREAVE.get()).getAmplifier()+1;
            }

            swords[i].setCustomName(Component.translatable(String.valueOf(Math.min(4,custom1+custom2))));

          /*  try {
                if(string().contains(player.getStringUUID())) {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/
          /*  if(SpellbladeMod.UUIDS.contains(player.getStringUUID())) {
                swords[i].setCustomName(new TextComponent("emerald"));
            }*/
            swords[i].damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            if(!level.isClientSide() && Patreon.allowed(player,SpellbladeMod.UUIDS) && Patreon.emeraldbladeflurry.contains(player)) {
                swords[i].setCustomName(Component.translatable("emerald"));
            }
            level.addFreshEntity(swords[i]);
        }

        ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-30);

        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -31) {
            player.hurt(DamageSource.MAGIC,2);
        }
        return super.trigger(level, player, modifier, spear);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_,p_41422_,p_41423_,p_41424_);

    }



   /* @Override
    public void guardtick(Player player, Level level, int slot, int count) {
        if (player.getInventory().getItem(slot).getTag() != null && count % 2 == 0) {
            if(player.getInventory().getItem(slot).getTag().contains("Mode")){
                if(player.getInventory().getItem(slot).getTag().getInt("Mode") == 3){
                    sword1 sword = new sword1(ModEntities.SWORD.get(),level,player);
                    sword.setOwner(player);
                    sword.mode = Objects.requireNonNull(player.getInventory().getItem(slot).getTag()).getInt("Mode");
                    sword.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F , 1.0F);
                    sword.setPos(sword.position().add(level.random.nextDouble(-0.5,0.5),level.random.nextDouble(-0.5,0.5),level.random.nextDouble(-0.5,0.5)));


                    level.addFreshEntity(sword);
                    return;
                }
            }
        }
        super.guardtick(player, level, slot, count);
    }*/

    /*@Override
    public void guardstart(Player player, Level level, int slot){
        sword1[] swords = new sword1[32];

        for(int i = 0; i < swords.length; i++) {
            swords[i] = new sword1(ModEntities.SWORD.get(), level, player);
            swords[i].setPos(player.getEyePosition().add(0, 4, 0));
            swords[i].number = i + 1;
            swords[i].tickCount = -i;
            if (player.getInventory().getItem(slot).getTag() != null) {
                if(player.getInventory().getItem(slot).getTag().contains("Mode")) {

                    swords[i].mode = Objects.requireNonNull(player.getInventory().getItem(slot).getTag()).getInt("Mode");
                    if(swords[i].mode == 1){
                        swords[i].guard = true;
                        swords[i].setCustomName(new TextComponent("guard"));
                    }
                    if (swords[i].mode == 3) {
                        swords[i].shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                    }
                    level.addFreshEntity(swords[i]);
                }
            }
        }

    }*/

}
