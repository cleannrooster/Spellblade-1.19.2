package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.SpellbladeMod;
import com.cleannrooster.spellblademod.StatusEffectsModded;
import com.cleannrooster.spellblademod.effects.DamageSourceModded;
import com.cleannrooster.spellblademod.manasystem.manatick;
import com.cleannrooster.spellblademod.effects.FluxHandler;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.ItemLayersModelBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
import java.util.*;
import java.util.stream.Stream;

public class Spellblade extends SwordItem{
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    private final float attackSpeed;
    public int tier = 0;
    public int tier1 = 0;
    public int getColor(ItemStack itemStack){
        CompoundTag nbt = itemStack.getOrCreateTag();
        if(nbt.getInt("ward") >= 4) {
            if (nbt.getInt("ward") % 4 == 0) {
                return 16763903;
            }
            if (nbt.getInt("ward") % 4 == 1) {
                return 16748183;
            }
            if (nbt.getInt("ward") % 4 == 2) {
                return 13172735;
            }
            if (nbt.getInt("ward") % 4 == 3) {
                return 16777153;
            }
        }

        return 16777215;
    }

    public Spellblade(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
        this.attackDamage = Math.max(0,(float) (((float)p_43270_ + p_43269_.getAttackDamageBonus())));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        this.attackSpeed = p_43271_;
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)p_43271_, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }
    public ArrayList<Item> items = new ArrayList<>(0);
    Stream<RegistryObject<Item>> oils;
    Stream<RegistryObject<Item>> items2;

    @Override
    public boolean isValidRepairItem(ItemStack p_43311_, ItemStack p_43312_) {
        if(p_43312_.getItem() instanceof WardingMail){
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack thisStack, ItemStack onStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if(clickAction == ClickAction.SECONDARY){
            if (onStack.getItem() instanceof Flask flask && (thisStack.getItem() instanceof Spellblade)) {
                ItemStack itemStack = thisStack;
                CompoundTag compoundtag = new CompoundTag();

                flask.applyFlask(player,null,onStack,itemStack, false);

                CompoundTag nbt = itemStack.getOrCreateTag();
                CompoundTag autoUse = nbt.getCompound("AutoUse");

                if (nbt.getCompound("AutoUse").contains(onStack.getOrCreateTag().getString("Spell"))) {
                    nbt.getCompound("AutoUse").remove(onStack.getOrCreateTag().getString("Spell"));

                } else {
                    nbt.getCompound("AutoUse").putBoolean(onStack.getOrCreateTag().getString("Spell"), true);

                }
                if(!nbt.contains("AutoUse")){
                    autoUse.putBoolean(onStack.getOrCreateTag().getString("Spell"), true);
                    nbt.put("AutoUse",autoUse);
                }
                return true;
            }
            else {
                CompoundTag nbt = thisStack.getOrCreateTag();
                if (thisStack.hasTag()) {
                    if (thisStack.getTag().get("OnHit") != null) {
                        nbt = thisStack.getTag();
                        nbt.remove("OnHit");
                        return true;
                    } else {
                        nbt = thisStack.getOrCreateTag();
                        nbt.putInt("OnHit", 1);
                        return true;
                    }

                } else {
                    nbt = thisStack.getOrCreateTag();
                    nbt.putInt("OnHit", 1);
                    return true;

                }
            }


        }
        if(clickAction == ClickAction.PRIMARY) {
            if (onStack.getItem() instanceof Flask flask && (thisStack.getItem() instanceof Spellblade)) {
                ItemStack itemStack = thisStack;
                CompoundTag compoundtag = new CompoundTag();
                for (RegistryObject<Item> spell3 : ModItems.ITEMS.getEntries()) {
                    if (spell3.get() instanceof Spell spell && !spell.isTriggerable()) {
                        if (Objects.equals(spell.getDescriptionId(), Flask.getSpellItem(onStack))) {
                            return false;
                        }
                    }
                }
                flask.applyFlask(player,null,onStack,itemStack,true);

                CompoundTag nbt = itemStack.getOrCreateTag();
                CompoundTag autoUse = nbt.getCompound("AutoTrigger");


                    if (nbt.getCompound("AutoTrigger").contains(onStack.getOrCreateTag().getString("Spell"))) {
                        nbt.getCompound("AutoTrigger").remove(onStack.getOrCreateTag().getString("Spell"));

                    } else {
                        nbt.getCompound("AutoTrigger").putBoolean(onStack.getOrCreateTag().getString("Spell"), true);

                    }
                    if (!nbt.contains("AutoTrigger")) {
                        autoUse.putBoolean(onStack.getOrCreateTag().getString("Spell"), true);
                        nbt.put("AutoTrigger", autoUse);
                    }
                    nbt.put("AutoTrigger", autoUse);

                return true;
            }
        }
                return false;
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", Math.max(0,(double)this.attackDamage+ (int)((stack.getOrCreateTag().getInt("ward")))), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)this.attackSpeed, AttributeModifier.Operation.ADDITION));
        if(slot == EquipmentSlot.MAINHAND) {
            return builder.build();
        }
        else{
            return super.getAttributeModifiers(slot, stack);
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        Player playa = (Player) player;
        if(this.getUseDuration(stack)-count == (int)Math.max(5,(40/player.getAttributeValue(Attributes.ATTACK_SPEED)))){
            ((Player)playa).getAttribute(manatick.WARD).setBaseValue(((Player) playa).getAttributeBaseValue(manatick.WARD)+40);
        }
        if((this.getUseDuration(stack)-count) % (int)Math.max(5,(40/player.getAttributeValue(Attributes.ATTACK_SPEED))) == 0) {
            if (player.hasEffect(StatusEffectsModded.WARDING.get())) {
                player.addEffect(new MobEffectInstance(StatusEffectsModded.WARDING.get(), 80, Math.min(2 * (1+EnchantmentHelper.getEnchantmentLevel(SpellbladeMod.wardTempered, player))+1, player.getEffect(StatusEffectsModded.WARDING.get()).getAmplifier() + 1)));
            } else {
                player.addEffect(new MobEffectInstance(StatusEffectsModded.WARDING.get(), 80, 0));
            }
        }

        boolean flag = false;
        int ii = 0;
        for (int i = 0; i <= playa.getInventory().getContainerSize(); i++) {
            if (playa.getInventory().getItem(i).getItem() instanceof Guard) {
                if (playa.getInventory().getItem(i).hasTag()) {
                    if (playa.getInventory().getItem(i).getTag().getInt("Triggerable") == 1) {
                        ((Guard) playa.getInventory().getItem(i).getItem()).guardtick(playa, player.level,i , count);
                        ii++;
                    }
                }
            }
            if (ii > 0) {
                //player.addEffect(new MobEffectInstance(StatusEffectsModded.SPELLWEAVING.get(), 30, ii - 1));
            }
        }
        /*if (player.hasEffect(StatusEffectsModded.SPELLWEAVING.get()) && ((Player)player).getAttributes().getBaseValue(manatick.WARD) < 0 && count % 10 ==5){
            player.addEffect(new MobEffectInstance(StatusEffectsModded.SPELLWEAVING.get(), 80, ii - 1));
        }*/

    }
    /*public void releaseUsing(ItemStack p_43394_, Level p_43395_, LivingEntity p_43396_, int p_43397_) {
        if ( p_43396_.getEffect((StatusEffectsModded.WARDLOCKED.get())) == null) {
            p_43396_.addEffect(new MobEffectInstance(StatusEffectsModded.WARDLOCKED.get(), 30, 0));
        }
    }*/

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemstack, Player player, LivingEntity p_41400_, InteractionHand p_41401_) {
        if(!player.getCooldowns().isOnCooldown(this)) {
            use(player.getLevel(), player, p_41401_);
            return InteractionResult.sidedSuccess(false);

        }
        return InteractionResult.FAIL;
    }

    @Override
    public InteractionResult useOn(UseOnContext p_41427_) {
        if(p_41427_.getLevel().getBlockState(p_41427_.getClickedPos()).getBlock() == Blocks.WATER_CAULDRON){
            p_41427_.getItemInHand().getOrCreateTag().remove("Oils");
            p_41427_.getItemInHand().getOrCreateTag().remove("AutoUse");
            p_41427_.getItemInHand().getOrCreateTag().remove("Triggers");
            p_41427_.getLevel().playSound(null, p_41427_.getClickedPos(), SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1, 1);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(p_41427_);
    }

    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        if (p_41406_ instanceof Player player && !(player.getUseItem().getOrCreateTag().getBoolean("shield") && player.getUseItem() == p_41404_) ){
            CompoundTag nbt = p_41404_.getOrCreateTag();
            int ward = 0;
            int wardreave = 0;
            if (player.hasEffect(StatusEffectsModded.WARDING.get())) {
                ward = player.getEffect(StatusEffectsModded.WARDING.get()).getAmplifier() + 1;
            }
            if (player.hasEffect(StatusEffectsModded.WARDREAVE.get())) {
                wardreave = player.getEffect(StatusEffectsModded.WARDREAVE.get()).getAmplifier() + 1;
            }
            ward = wardreave + ward;
            int custom = ward;
            if (custom > 4) custom =  4;
            nbt.putInt("CustomModelData", (custom));
            nbt.putBoolean("shield", (false));


            nbt.putInt("ward", ward);
        }

    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {

        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity p_41414_, int count) {
        if(p_41414_ instanceof Player p_41433_) {
            CompoundTag tag = stack.getOrCreateTag();
            for (ItemStack item : p_41433_.getInventory().items) {
                if (item.getItem() instanceof Flask flask && tag.contains("AutoUse") && tag.getCompound("AutoUse").contains(Flask.getSpellItem(item))) {
                    flask.applyFlask(p_41433_, null, item, stack, false);
                }
            }
            if (tag.contains("Oils")) {
                for (String key : tag.getCompound("Oils").getAllKeys()) {
                    if (((Player) p_41433_).getAttributes().getBaseValue(manatick.WARD) > -20.5) {
                        boolean worked = true;
                        if (tag.getCompound("Oils").getInt(key) > 0) {

                            worked = Flask.useSpell(key, p_41433_.level, p_41433_, InteractionHand.MAIN_HAND, stack);
                            if (worked) {
                                tag.getCompound("Oils").putInt(key, tag.getCompound("Oils").getInt(key) - 1);
                            }
                        } else if (!tag.getCompound("AutoUse").contains(key)) {
                            tag.getCompound("Oils").remove(key);
                        }
                    }
                    else{
                        ChatFormatting chatFormatting = ChatFormatting.GRAY;

                        MutableComponent text1 = Component.translatable("Not enough Ward to cast ").withStyle(chatFormatting);
                        MutableComponent text3 = Component.translatable(".").withStyle(chatFormatting);
                        for (RegistryObject<Item> spell3 : ModItems.ITEMS.getEntries()) {
                            if (spell3.get() instanceof Spell spell && Objects.equals(spell.getDescriptionId(), key)) {

                                chatFormatting = spell.color();
                            }
                        }

                        MutableComponent text2 = Component.translatable(key).withStyle(chatFormatting);
                        if(!p_41433_.level.isClientSide()) {
                            p_41433_.sendSystemMessage(text1.append(text2).append(text3));
                        }
                            p_41433_.getLevel().playSound(null, p_41433_.getOnPos(), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 1, 1);


                    }
                }
            }

            for (ItemStack item : p_41433_.getInventory().items) {
                if (item.getItem() instanceof Flask flask && tag.contains("AutoUse") && tag.getCompound("AutoUse").contains(Flask.getSpellItem(item))) {
                    flask.applyFlask(p_41433_, null, item, stack, false);
                }
            }
            p_41433_.getCooldowns().addCooldown(this,2*(int)Math.ceil(20/p_41433_.getAttributeValue(Attributes.ATTACK_SPEED)));
        }
        if(p_41414_.hasEffect(StatusEffectsModded.WARDING.get())) {
            p_41414_.removeEffect(StatusEffectsModded.WARDING.get());
        }
        super.releaseUsing(stack, level, p_41414_, count);
    }
    public boolean hurtEnemy(ItemStack itemstack, LivingEntity living, LivingEntity p_41433_) {
        List<Spell> spells = new ArrayList<>();
        if (p_41433_.hasEffect(StatusEffectsModded.WARDREAVE.get())) {
            p_41433_.addEffect(new MobEffectInstance(StatusEffectsModded.WARDREAVE.get(), 80, Math.min((1 + EnchantmentHelper.getEnchantmentLevel(SpellbladeMod.wardTempered, p_41433_)), p_41433_.getEffect(StatusEffectsModded.WARDREAVE.get()).getAmplifier() + 1)));
        } else {
            p_41433_.addEffect(new MobEffectInstance(StatusEffectsModded.WARDREAVE.get(), 80, 0));
        }
        if (itemstack.hasTag()) {
            if (itemstack.getTag().get("OnHit") != null) {
                if (p_41433_ instanceof Player player) {
                    CompoundTag tag = itemstack.getOrCreateTag();

                    if(tag.contains("Triggers")){
                        for(String key : tag.getCompound("Triggers").getAllKeys()) {
                            if (((Player) p_41433_).getAttributes().getBaseValue(manatick.WARD) > -20.5) {

                                if (tag.getCompound("Triggers").getInt(key) > 0) {

                                    Spell spell2 = (Flask.triggerOrTriggeron(key, player.getLevel(), player, living, 1, itemstack, true, null));
                                    if (spell2 != null) {
                                        spells.add(spell2);
                                    }
                                    tag.getCompound("Triggers").putInt(key, tag.getCompound("Triggers").getInt(key) - 1);
                                } else if (!tag.getCompound("AutoTrigger").contains(key)) {
                                    tag.getCompound("Triggers").remove(key);
                                }

                            }
                            else{
                                ChatFormatting chatFormatting = ChatFormatting.GRAY;
                                MutableComponent text1 = Component.translatable("Not enough Ward to trigger ").withStyle(chatFormatting);
                                MutableComponent text3 = Component.translatable(" on ").withStyle(chatFormatting);
                                MutableComponent text4 = living.getDisplayName().copy().withStyle(ChatFormatting.WHITE);

                                for (RegistryObject<Item> spell3 : ModItems.ITEMS.getEntries()) {
                                    if (spell3.get() instanceof Spell spell && Objects.equals(spell.getDescriptionId(), key)) {

                                        chatFormatting = spell.color();
                                    }
                                }

                                MutableComponent text2 = Component.translatable(key).withStyle(chatFormatting);

                                    if(!p_41433_.level.isClientSide()) {
                                        p_41433_.sendSystemMessage(text1.append(text2).append(text3).append(text4).append("."));
                                    }
                                    p_41433_.getLevel().playSound(null, p_41433_.getOnPos(), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 1, 1);

                            }
                        }
                    }
                    for(ItemStack item : player.getInventory().items){
                        if(item.getItem() instanceof Flask flask && tag.contains("AutoTrigger") && tag.getCompound("AutoTrigger").contains(Flask.getSpellItem(item))){
                            for (RegistryObject<Item> spell3 : ModItems.ITEMS.getEntries()) {
                                if (spell3.get() instanceof Spell spell) {
                                    if (Objects.equals(spell.getDescriptionId(), Flask.getSpellItem(item))) {
                                        if(spell.isTriggerable()){
                                            flask.applyFlask(player, null, item, itemstack, true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(living.hasEffect(StatusEffectsModded.FLUXED.get()) && p_41433_ instanceof Player player){
            living.invulnerableTime = 0;

            living.hurt(DamageSourceModded.fluxed((Player) player), (float) p_41433_.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5F);
            FluxHandler.fluxHandler2(living,player, (float) p_41433_.getAttributeValue(Attributes.ATTACK_DAMAGE),p_41433_.getLevel(),new ArrayList<>(),spells, UUID.randomUUID());
            living.removeEffect(StatusEffectsModded.FLUXED.get());
            living.removeEffect(MobEffects.GLOWING);

        }
        super.hurtEnemy(itemstack,living,p_41433_);
        return true;
    }
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        if(p_41452_.getOrCreateTag().getInt("CustomModelData") == 5){
            return UseAnim.BLOCK;
        }
        return UseAnim.BOW;
    }
    public int getUseDuration(ItemStack p_43419_) {
        return 72000;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        Component text = Component.translatable("Trigger Spells On Hit");
        Component text2 = Component.translatable("Right click to toggle On Hit Mode");
            if (p_41421_.getOrCreateTag().get("OnHit") != null) {
                p_41423_.add(text);
            }
            else{
                p_41423_.add(text2);
            }
            if(p_41421_.getOrCreateTag().contains("Oils")){
                Set<String> keys = p_41421_.getOrCreateTag().getCompound("Oils").getAllKeys();
                for(String key : keys){
                    if(p_41421_.getOrCreateTag().getCompound("Oils").contains(key)) {


                        MutableComponent mutablecomponent1 = Component.translatable("On Cast").withStyle(ChatFormatting.GRAY);
                        if(this instanceof ConduitSpear){
                            mutablecomponent1 = Component.translatable("On Throw").withStyle(ChatFormatting.GRAY);

                        }
                        mutablecomponent1.append(": ");
                        ChatFormatting color = ChatFormatting.WHITE;
                        for (RegistryObject<Item> spell3 : ModItems.ITEMS.getEntries()) {
                            if(spell3.get() instanceof Spell spell){
                                if(Objects.equals(spell.getDescriptionId(), key)){
                                    color = spell.color();
                                }
                            }
                        }

                        MutableComponent mutablecomponent = Component.translatable(key).withStyle(color);


                        if (p_41421_.getOrCreateTag().contains("AutoUse") && p_41421_.getOrCreateTag().getCompound("AutoUse").contains(key)) {
                            mutablecomponent.append(" ").append(Component.translatable("Auto"));
                        } else {
                            mutablecomponent.append(" ").append(Component.translatable(/*"enchantment.level." +*/ String.valueOf(p_41421_.getOrCreateTag().getCompound("Oils").getInt(key))));
                        }
                        mutablecomponent1.append(mutablecomponent);
                        p_41423_.add(mutablecomponent1);
                    }
                }


            }
        if(p_41421_.getOrCreateTag().contains("Triggers")){
            Set<String> keys = p_41421_.getOrCreateTag().getCompound("Triggers").getAllKeys();
            for(String key : keys){
                if(p_41421_.getOrCreateTag().getCompound("Triggers").contains(key)) {
                    MutableComponent mutablecomponent1 = Component.translatable("On Hit").withStyle(ChatFormatting.WHITE);
                    mutablecomponent1.append(": ");
                    ChatFormatting color = ChatFormatting.WHITE;
                    for (RegistryObject<Item> spell3 : ModItems.ITEMS.getEntries()) {
                        if(spell3.get() instanceof Spell spell){
                            if(Objects.equals(spell.getDescriptionId(), key)){
                                color = spell.color();
                            }
                        }
                    }

                    MutableComponent mutablecomponent = Component.translatable(key).withStyle(color);


                    if (p_41421_.getOrCreateTag().contains("AutoTrigger") && p_41421_.getOrCreateTag().getCompound("AutoTrigger").contains(key)) {
                        mutablecomponent.append(" ").append(Component.translatable("Auto"));
                    } else {
                        mutablecomponent.append(" ").append(Component.translatable(/*"enchantment.level." +*/ String.valueOf(p_41421_.getOrCreateTag().getCompound("Triggers").getInt(key))));
                    }
                    mutablecomponent1.append(mutablecomponent);
                    p_41423_.add(mutablecomponent1);
                }
            }


        }

        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }


    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        ItemStack itemstack = p_41433_.getItemInHand(p_41434_);

        if(((Player)p_41433_).getAttributes().getBaseValue(manatick.WARD) > -20.5) {

            if (!p_41433_.getMainHandItem().isEdible() && !p_41433_.getCooldowns().isOnCooldown(this)) {
                if (p_41433_.getMainHandItem().getItem() instanceof Flask flask) {
                    flask.applyFlask(p_41433_, InteractionHand.MAIN_HAND, p_41433_.getMainHandItem(), p_41433_.getItemInHand(p_41434_), false);
                    return InteractionResultHolder.success(p_41433_.getItemInHand(p_41434_));

                }
                if (p_41433_.getOffhandItem().getItem() instanceof Flask flask) {
                    flask.applyFlask(p_41433_, InteractionHand.OFF_HAND, p_41433_.getOffhandItem(), p_41433_.getItemInHand(p_41434_), false);
                    return InteractionResultHolder.success(p_41433_.getItemInHand(p_41434_));

                }
                // p_41433_.getCooldowns().addCooldown(this,2*(int)Math.ceil(20/p_41433_.getAttributeValue(Attributes.ATTACK_SPEED)));
                int ii = 0;
                for (int i = 0; i <= p_41433_.getInventory().getContainerSize(); i++) {
                    if (p_41433_.getInventory().getItem(i).getItem() instanceof Guard) {
                        if (p_41433_.getInventory().getItem(i).hasTag()) {
                            if (p_41433_.getInventory().getItem(i).getTag().getInt("Triggerable") == 1) {
                                ((Guard) p_41433_.getInventory().getItem(i).getItem()).guardstart(p_41433_, p_41433_.level, i);
                                ii++;
                            }
                        }
                    }
                    if (ii > 0) {
                        //p_41433_.addEffect(new MobEffectInstance(StatusEffectsModded.SPELLWEAVING.get(), 80, ii - 1));
                    }
                }
                p_41433_.startUsingItem(p_41434_);

                return InteractionResultHolder.sidedSuccess(itemstack, false);
            }
        }
        else{
            p_41433_.getCooldowns().addCooldown(this,2*(int)Math.ceil(20/p_41433_.getAttributeValue(Attributes.ATTACK_SPEED)));
            if(!p_41432_.isClientSide()) {
                p_41433_.sendSystemMessage(Component.translatable("Not enough Ward to cast.").withStyle(ChatFormatting.GRAY));
            }
                p_41433_.getLevel().playSound(null, p_41433_.getOnPos(), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 1, 1);

        }
        return InteractionResultHolder.fail(itemstack);

    }



    public static boolean isSpellblade(Item item) {
        if (item instanceof Spellblade){
            return true;
        }
        else{
            return false;
        }
    }
}
