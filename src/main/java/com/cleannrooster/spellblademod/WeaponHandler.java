package com.cleannrooster.spellblademod;

import com.cleannrooster.spellblademod.effects.DamageSourceModded;
import com.cleannrooster.spellblademod.effects.FluxHandler;
import com.cleannrooster.spellblademod.entity.HammerEntity;
import com.cleannrooster.spellblademod.entity.ModEntities;
import com.cleannrooster.spellblademod.entity.SpiderSpark;
import com.cleannrooster.spellblademod.items.*;
import com.cleannrooster.spellblademod.manasystem.manatick;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.FireAspectEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;

import static com.cleannrooster.spellblademod.manasystem.manatick.SMOTE;

@Mod.EventBusSubscriber(modid = "spellblademod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WeaponHandler {




    @SubscribeEvent
    public static void SpellbladeHandler(LivingHurtEvent event){
        if (event.getSource().getDirectEntity() instanceof Player) {
            Player player = (Player) event.getSource().getEntity();

            if (player.getOffhandItem().getItem() instanceof LightningWhirl && player.isAutoSpinAttack() && player.getMainHandItem().getItem() instanceof Spellblade){
                if (event.getEntity().hasEffect(StatusEffectsModded.SHOCKED.get())){
                    int amplifier = event.getEntity().getEffect(StatusEffectsModded.SHOCKED.get()).getAmplifier();
                    if (amplifier >= 2){
                        event.getEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 9));
                        event.getEntity().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0));
                    }
                    else{
                        event.getEntity().addEffect(new MobEffectInstance(StatusEffectsModded.SHOCKED.get(),80, amplifier+1));
                    }
                }
                else {
                    event.getEntity().addEffect(new MobEffectInstance(StatusEffectsModded.SHOCKED.get(), 80, 0));
                }
            }
            if (player.getOffhandItem().getItem() instanceof IceEffigy && player.getMainHandItem().getItem() instanceof Spellblade){
                player.addEffect(new MobEffectInstance(StatusEffectsModded.WARD_DRAIN.get(),80,0));
                event.getEntity().addEffect(new MobEffectInstance(StatusEffectsModded.ECHOES.get(),10, (int) Math.floor(event.getAmount())));
            }
        }
    }
    @SubscribeEvent
    public static void spellproxy(AttackEntityEvent event) {
        List<Spell> spells = new ArrayList<>();
        if((EnchantmentHelper.getEnchantmentLevel(SpellbladeMod.wardTempered, event.getEntity())> 0 && EnchantmentHelper.getEnchantmentLevel(SpellbladeMod.spellproxy, event.getEntity()) > 0) || EnchantmentHelper.getEnchantmentLevel(SpellbladeMod.spellproxy, event.getEntity()) > 0){
            if (event.getEntity().hasEffect(StatusEffectsModded.WARDREAVE.get())) {
                event.getEntity().addEffect(new MobEffectInstance(StatusEffectsModded.WARDREAVE.get(), 80, Math.min(-1+(EnchantmentHelper.getEnchantmentLevel(SpellbladeMod.spellproxy, event.getEntity())+(EnchantmentHelper.getEnchantmentLevel(SpellbladeMod.wardTempered, event.getEntity()))), event.getEntity().getEffect(StatusEffectsModded.WARDREAVE.get()).getAmplifier() + 1)));
            } else {
                event.getEntity().addEffect(new MobEffectInstance(StatusEffectsModded.WARDREAVE.get(), 80, 0));
            }
        }
        if (event.getEntity().getAttackStrengthScale(0) > 0.5 && EnchantmentHelper.getEnchantmentLevel(SpellbladeMod.spellproxy, event.getEntity()) > 0  && event.getTarget() instanceof LivingEntity living &&  !(event.getTarget() instanceof SpiderSpark)) {
            //player.getPersistentData().putBoolean("cast", true);
            Player player = event.getEntity();
            ItemStack itemstack = new ItemStack(ModItems.SPELLBLADE.get());
                itemstack = ItemStack.of(player.getPersistentData().getCompound("spellproxy"));
            CompoundTag compoundtag = new CompoundTag();
            itemstack.save(compoundtag);
            player.setLastHurtMob(event.getTarget());

            CompoundTag tag = itemstack.getOrCreateTag();

            if(tag.contains("Triggers")){
                for(String key : tag.getCompound("Triggers").getAllKeys()) {
                    if (((Player) player).getAttributes().getBaseValue(manatick.WARD) > -0.5) {

                        String spell = key;
                        if (tag.getCompound("Triggers").getInt(spell) > 0) {

                            spells.add(Flask.triggerOrTriggeron(spell, player.getLevel(), player, living, 1, itemstack, true, null));

                            tag.getCompound("Triggers").putInt(spell, tag.getCompound("Triggers").getInt(spell) - 1);
                        } else if (!tag.getCompound("AutoTrigger").contains(spell)) {
                            tag.getCompound("Triggers").remove(spell);
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

                        if(!player.level.isClientSide()) {
                            player.sendSystemMessage(text1.append(text2).append(text3).append(text4).append("."));
                        }
                        player.getLevel().playSound(null, player.getOnPos(), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 1, 1);

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
            itemstack.save(compoundtag);
            player.getPersistentData().put("spellproxy", compoundtag);
            if(living.hasEffect(StatusEffectsModded.FLUXED.get())){
                living.invulnerableTime = 0;
                living.hurt(DamageSourceModded.fluxed((Player) player), (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5F);
                living.invulnerableTime = 0;
                FluxHandler.fluxHandler2(living,player, (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE),player.getLevel(),new ArrayList<>(),spells, UUID.randomUUID());
                living.removeEffect(StatusEffectsModded.FLUXED.get());
                living.removeEffect(MobEffects.GLOWING);

            }

        }
    }
    @SubscribeEvent
    public static void useEvent(PlayerInteractEvent event) {
        if (event.getLevel().getBlockState(event.getPos()).getBlock() == Blocks.WATER_CAULDRON && EnchantmentHelper.getEnchantmentLevel(SpellbladeMod.spellproxy,event.getEntity()) > 0) {
            Player player = event.getEntity();
            ItemStack itemstack = new ItemStack(ModItems.SPELLBLADE.get());
            itemstack = ItemStack.of(player.getPersistentData().getCompound("spellproxy"));
            CompoundTag compoundtag = new CompoundTag();
            itemstack.save(compoundtag);

            itemstack.getOrCreateTag().remove("Oils");
            itemstack.getOrCreateTag().remove("AutoUse");
            itemstack.getOrCreateTag().remove("Triggers");
            itemstack.save(compoundtag);
            player.getPersistentData().put("spellproxy", compoundtag);
            player.getLevel().playSound(null, event.getPos(), SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1, 1);


        }
    }
        @SubscribeEvent
    public static void EchoesHandler(MobEffectEvent.Remove event){
        if (event.getEffectInstance() != null) {
            if (event.getEffectInstance().getEffect() == StatusEffectsModded.ECHOES.get()) {
                event.getEntity().invulnerableTime = 0;
                event.getEntity().hurt(new DamageSource("echoes"), event.getEffectInstance().getAmplifier());
                event.getEntity().invulnerableTime = 0;
            }
        }
    }
    @SubscribeEvent
    public static void FireEffigyHandler(LivingDeathEvent event){
        if(event.getSource().getEntity() instanceof Player){
            if (((Player)event.getSource().getEntity()).getOffhandItem().getItem() instanceof FireEffigy){
                Player player = (Player) event.getSource().getEntity();
                    player.addEffect(new MobEffectInstance(StatusEffectsModded.WARD_DRAIN.get(), 80, 0));
                    if (!event.getEntity().getLevel().isClientSide()) {
                        event.getEntity().getLevel().explode(event.getSource().getEntity(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), 3, false, Explosion.BlockInteraction.NONE);
                    }
            }
        }

    }

    @SubscribeEvent
    public static void VengefulHandler(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player) {
            Player vengeant = (Player) event.getEntity();
            if (vengeant.hasEffect(StatusEffectsModded.VENGEFUL_STANCE.get())) {
                if (event.getSource().getEntity() instanceof LivingEntity) {
                }
            }
        }
    }
    @SubscribeEvent
    public static void EffigyOfChaining(LivingDamageEvent event){
        if(event.getEntity().getAttribute(SMOTE) != null) {

            if (event.getSource().getDirectEntity() instanceof Player && (int) event.getEntity().getAttribute(SMOTE).getValue() <= 0) {
                Player player = (Player) event.getSource().getDirectEntity();
                LivingEntity living = event.getEntity();
                if (player.getOffhandItem().getItem() instanceof LightningEffigy && player.getMainHandItem().getItem() instanceof Spellblade) {
                    if (event.getEntity().getAttribute(SMOTE) != null) {

                        event.getEntity().getAttribute(SMOTE).setBaseValue(10);
                    }
                    player.addEffect(new MobEffectInstance(StatusEffectsModded.WARD_DRAIN.get(), 80, 0));
                    List<LivingEntity> entities = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(3D), livingEntity -> {
                        return FriendshipBracelet.PlayerFriendshipPredicate(player, livingEntity);
                    });
                    List<LivingEntity> validentities = new ArrayList<>();
                    Object[] entitiesarray = entities.toArray();
                    HammerEntity hammer3 = new HammerEntity(ModEntities.TRIDENT.get(), player.getLevel());
                    hammer3.setPos(living.getBoundingBox().getCenter().add(0, 4 + living.getBoundingBox().getYsize() / 2, 0));
                    hammer3.setOwner(player);
                    hammer3.pickup = AbstractArrow.Pickup.DISALLOWED;
                    hammer3.secondary = true;
                    hammer3.shoot(0, -1, 0, 1.6F, 0);
                    living.getLevel().addFreshEntity(hammer3);
                }

            }
        }
    }
}
    /*@SubscribeEvent
    public static void SpellbladeTrigger(PlayerInteractEvent event) {

        if (event.getSource().getDirectEntity() instanceof Player) {
            int ii = 0;
            Player player = (Player) event.getSource().getDirectEntity();
            PlayerMana playerMana = player.getCapability(PlayerManaProvider.PLAYER_MANA).orElse(null);
            if (player.getMainHandItem().getItem() instanceof Spellblade) {
                for (int i = 0; i <= 9; i++) {
                    if (player.getInventory().getItem(i).getItem() instanceof Spell) {
                        if (player.getInventory().getItem(i).hasTag()) {
                            if (player.getInventory().getItem(i).getTag().getInt("Triggerable") == 1) {
                                if (!player.getCooldowns().isOnCooldown(player.getInventory().getItem(i).getItem())) {
                                    if (((Spell) player.getInventory().getItem(i).getItem()).trigger(player.level, player, (float) 1 / 8)) {

                                    }
                                }
                                ii++;
                            }
                        }
                        player.addEffect(new MobEffectInstance(StatusEffectsModded.SPELLWEAVING.get(), 80, ii - 1));
                    }
                }
            }
        }

    }*/
    /*@SubscribeEvent
    public static void Particles(LivingAttackEvent event){

        System.out.println(event.getEntityLiving().getLevel());
        if (event.getSource().msgId.equals("fluxed1")) {
            Random random = new Random();
            int num_pts = 100;
            LivingEntity living2 = event.getEntityLiving();

            Vec3 living = event.getEntityLiving().getBoundingBox().getCenter();
            for (int i = 0; i <= num_pts; i = i + 1) {
                double[] indices = IntStream.rangeClosed(0, (int) ((num_pts - 0) / 1))
                        .mapToDouble(x -> x * 1 + 0).toArray();

                double phi = Math.acos(1 - 2 * indices[i] / num_pts);
                double theta = Math.PI * (1 + Math.pow(5, 0.5) * indices[i]);
                double x = cos(theta) * sin(phi);
                double y = Math.sin(theta) * sin(phi);
                double z = cos(phi);
                ((event.getEntityLiving().getLevel())).addParticle(ParticleTypes.GLOW_SQUID_INK.getType(), living.x + random.nextDouble(-0.1, 0.1), living.y + random.nextDouble(-0.1, 0.1), living.z + random.nextDouble(-0.1, 0.1), x * 0.5, y * 0.5, z * 0.5);
            }
        }
        if (event.getSource().msgId.equals("fluxed")) {
            int num_pts = 100;
            Random random = new Random();
            Vec3 living = event.getEntityLiving().getBoundingBox().getCenter();
            for (int i = 0; i <= num_pts; i = i + 1) {
                double[] indices = IntStream.rangeClosed(0, (int) ((num_pts - 0) / 1))
                        .mapToDouble(x -> x * 1 + 0).toArray();

                double phi = Math.acos(1 - 2 * indices[i] / num_pts);
                double theta = Math.PI * (1 + Math.pow(5, 0.5) * indices[i]);
                double x = cos(theta) * sin(phi);
                double y = Math.sin(theta) * sin(phi);
                double z = cos(phi);
                ((event.getEntityLiving().getLevel())).gameEvent(ParticleTypes.GLOW_SQUID_INK.getType(), living.x + random.nextDouble(-0.1, 0.1), living.y + random.nextDouble(-0.1, 0.1), living.z + random.nextDouble(-0.1, 0.1), x * 0.5, y * 0.5, z * 0.5);
            }
        }
    }*/
