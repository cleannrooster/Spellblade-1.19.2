package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.StatusEffectsModded;
import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.entity.EndersEyeEntity;
import com.cleannrooster.spellblademod.entity.ModEntities;
import com.cleannrooster.spellblademod.manasystem.manatick;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EndersEye extends Spell {
    public EndersEye(Properties p_41383_) {
        super(p_41383_);
    }
    public Item getIngredient1() {return Items.ENDER_EYE;};
    public Item getIngredient2() {return ModItems.ESSENCEBOLT.get();};
    public ChatFormatting color(){
        return ChatFormatting.GREEN;
    }
    @Override
    public boolean isTargeted() {
        return true;
    }

    public InteractionResultHolder<ItemStack> use(Level p_43405_, Player p_43406_, InteractionHand p_43407_) {

        ItemStack itemstack = p_43406_.getItemInHand(p_43407_);
        Player player = p_43406_;

        if (((Player) p_43406_).isShiftKeyDown() && itemstack.getItem() instanceof Spell) {
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
        ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
            p_43406_.hurt(DamageSource.MAGIC,2);
        }
            ((Player) p_43406_).addEffect(new MobEffectInstance(StatusEffectsModded.ENDERSGAZE.get(), 120, 0));
        if (!((Player) p_43406_).isShiftKeyDown()) {

        }

            EndersEyeEntity eye = new EndersEyeEntity(ModEntities.ENDERS_EYE.get(),p_43406_.getLevel());
            eye.setPos(((Player) p_43406_).getEyePosition());
            eye.setOwner((Player) p_43406_);

            eye.damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        List<EndersEyeEntity> eyes = p_43405_.getEntitiesOfClass(EndersEyeEntity.class,player.getBoundingBox().inflate(32),endersEyeEntity -> endersEyeEntity.getOwner() == player);
            if(eyes.toArray().length >= 3){
                EndersEyeEntity todelete = eyes.get(0);
                for(EndersEyeEntity eyeEntity : eyes){
                    if(eyeEntity.tickCount > todelete.tickCount){
                        todelete = eyeEntity;
                    }
                }
                todelete.discard();
            }
        eye.pos1 = ((Player) p_43406_).position().add(new Vec3(0, 1.5, 0)).add(new Vec3(0, player.getBoundingBox().getYsize() / 2, 0));
        p_43405_.addFreshEntity(eye);
            return InteractionResultHolder.success(itemstack);

    }

    @Override
    public boolean triggeron(Level level, Player player, LivingEntity target, float modifier) {

        List<EndersEyeEntity> eyes = level.getEntitiesOfClass(EndersEyeEntity.class,player.getBoundingBox().inflate(32),endersEyeEntity -> endersEyeEntity.getOwner() == player);
        if(eyes.toArray().length >= 3) {
            EndersEyeEntity todelete = eyes.get(0);
            for(EndersEyeEntity eyeEntity : eyes){
                if(eyeEntity.tickCount > todelete.tickCount){
                    todelete = eyeEntity;
                }
            }
            todelete.discard();

        }
        EndersEyeEntity eye = new EndersEyeEntity(ModEntities.ENDERS_EYE.get(), player.getLevel());
        eye.setPos(target.getEyePosition());
        eye.setOwner(player);
        eye.target = target;
        eye.pos1 = ((Player) player).position().add(new Vec3(0, 1.5, 0)).add(new Vec3(0, player.getBoundingBox().getYsize() / 2, 0));;
        eye.damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);

        level.addFreshEntity(eye);
        ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
            player.hurt(DamageSource.MAGIC,2);
        }
        return false;
    }
    @Override
    public boolean trigger(Level level, Player player, float modifier, @Nullable ConduitSpearEntity spear){
        Entity entity;
        if (spear != null){
            entity = spear;
        }
        else{
            entity = player;
        }
        List<EndersEyeEntity> eyes = level.getEntitiesOfClass(EndersEyeEntity.class,entity.getBoundingBox().inflate(32),eyeEntity -> eyeEntity.getOwner() == player);
        if(eyes.toArray().length >= 3) {
            EndersEyeEntity todelete = eyes.get(0);
            for(EndersEyeEntity eyeEntity : eyes){
                if(eyeEntity.tickCount > todelete.tickCount){
                    todelete = eyeEntity;
                }
            }
            todelete.discard();

        }
        EndersEyeEntity eye = new EndersEyeEntity(ModEntities.ENDERS_EYE.get(), player.getLevel());
        eye.setPos(entity.getEyePosition());
        eye.setOwner(player);
        eye.pos1 = entity.position().add(new Vec3(0, 1.5, 0)).add(new Vec3(0, entity.getBoundingBox().getYsize() / 2, 0));;
        eye.damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);

        level.addFreshEntity(eye);
        ((Player)player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributeBaseValue(manatick.WARD)-20);

        if (((Player)player).getAttributes().getBaseValue(manatick.WARD) < -21) {
            player.hurt(DamageSource.MAGIC,2);
        }
        return false;
    }
    public int getColor() {
        return 3827506;
    }
    public int getColor2() {
        return 10026938;
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level level, Entity p_41406_, int p_41407_, boolean p_41408_) {
        if(p_41406_ instanceof Player player) {
            List<EndersEyeEntity> eyes = level.getEntitiesOfClass(EndersEyeEntity.class, player.getBoundingBox().inflate(64), endersEyeEntity -> endersEyeEntity.getOwner() == player);
            if (eyes.toArray().length > 3) {
                for (int ii = 3; ii < eyes.toArray().length; ii++) {
                    eyes.get(ii).discard();
                }
            }
        }
    }
    @Override
    public int triggerCooldown() {
        return 10;
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


}
