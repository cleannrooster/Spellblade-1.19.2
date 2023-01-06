package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.SpellbladeMod;
import com.cleannrooster.spellblademod.entity.CatSpark;
import com.cleannrooster.spellblademod.entity.ModEntities;
import com.cleannrooster.spellblademod.entity.SentinelEntity;
import com.cleannrooster.spellblademod.entity.SpiderSpark;
import com.cleannrooster.spellblademod.manasystem.manatick;
import com.cleannrooster.spellblademod.patreon.Patreon;
import com.cleannrooster.spellblademod.setup.Messages;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.CatVariantTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SummonSentinel extends Spell{
    public SummonSentinel(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public int triggerCooldown() {
        return 20;
    }
    public int getColor() {
        return 4935503;
    }
    public Item getIngredient1() {return Items.IRON_BLOCK;};
    public Item getIngredient2() {return ModItems.ESSENCEBOLT.get();};
    public ChatFormatting color(){
        return ChatFormatting.DARK_GRAY;
    }
    public int getColor2() {
        return 16777215;
    }
    @Override
    public boolean canSpellweave() {
        return false;
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
    public boolean isFoil(ItemStack p_41453_) {
        if (p_41453_.hasTag()){
            if(p_41453_.getTag().getInt("Triggerable") == 1){
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean triggeron(Level level, Player player, LivingEntity target, float modifier) {
        for (SentinelEntity sentinel: level.getEntitiesOfClass(SentinelEntity.class, player.getBoundingBox().inflate(18),sentinelEntity -> !sentinelEntity.getLevel().isClientSide() && sentinelEntity.owner == player)) {
            if(sentinel.hasLineOfSight(target) && !(target instanceof SentinelEntity)) {
                sentinel.setTarget(target);
                ((Player) player).getAttribute(manatick.WARD).setBaseValue(((Player) player).getAttributes().getBaseValue(manatick.WARD) - 10);
                if (((Player) player).getAttributes().getBaseValue(manatick.WARD) < -21) {
                    player.hurt(DamageSource.MAGIC, 2);
                }
            }
        }

        return super.triggeron(level, player, target, modifier);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        ItemStack itemstack = p_41433_.getItemInHand(p_41434_);
        Player player = p_41433_;
        Vec3 pos = p_41433_.getEyePosition().add(player.getViewVector(0));
        if(!p_41432_.getBlockState(new BlockPos((int)(pos.x),(int)(pos.y),
                (int)(pos.z))).isSuffocating(p_41432_,new BlockPos((int)(pos.x),(int)(pos.y),
                (int)(pos.z)))) {

            SentinelEntity spider = new SentinelEntity(ModEntities.SENTINEL.get(), p_41432_);
            spider.setPos(pos);
            spider.owner = player;
            spider.damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            p_41432_.addFreshEntity(spider);
            ((Player) p_41433_).getAttribute(manatick.WARD).setBaseValue(((Player) p_41433_).getAttributes().getBaseValue(manatick.WARD) - 20);
            if (((Player) p_41433_).getAttributes().getBaseValue(manatick.WARD) < -21) {
                p_41433_.hurt(DamageSource.MAGIC, 2);
            }
            return InteractionResultHolder.success(p_41433_.getItemInHand(p_41434_));
        }
        return InteractionResultHolder.fail(p_41433_.getItemInHand(p_41434_));

    }
}
