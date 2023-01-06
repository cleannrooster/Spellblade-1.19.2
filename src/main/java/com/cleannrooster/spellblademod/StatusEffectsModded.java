package com.cleannrooster.spellblademod;

import com.cleannrooster.spellblademod.effects.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.UUID;

public class StatusEffectsModded extends MobEffect {
    protected StatusEffectsModded(MobEffectCategory category, int p_19452_) {
        super(category, p_19452_);
    }
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "spellblademod");
    public static void registerStatusEffects() {
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    public static final RegistryObject<MobEffect> WARDING = EFFECTS.register("warding", ()-> new Warding(MobEffectCategory.BENEFICIAL, 0xffff54));
    public static final RegistryObject<MobEffect> WARDREAVE = EFFECTS.register("wardreave", ()-> new Warding(MobEffectCategory.BENEFICIAL, 0xffff54));
    public static final RegistryObject<MobEffect> REDSTONEBURST = EFFECTS.register("redstoneburst", ()-> new RedstoneBurstEffect(MobEffectCategory.BENEFICIAL, 0x990000).addAttributeModifier(Attributes.ATTACK_SPEED, "0e5b6c50-1e2f-42b4-b613-84a63ed9fe97",0.04, AttributeModifier.Operation.MULTIPLY_BASE).addAttributeModifier(Attributes.ATTACK_DAMAGE,"b1df9eb1-2e2f-46cb-b487-b0130901c982",0.04, AttributeModifier.Operation.MULTIPLY_BASE));

    public static final RegistryObject<MobEffect> SHOCKED = EFFECTS.register("shocked", ()-> new Shocked(MobEffectCategory.HARMFUL, 0xffff54));
    public static final RegistryObject<MobEffect> WARD_DRAIN = EFFECTS.register("ward_drain", ()-> new WardDrain(MobEffectCategory.HARMFUL, 0xffff54));
    public static final RegistryObject<MobEffect> TOTEMIC_ZEAL = EFFECTS.register("totemic_zeal", ()-> new TotemicZeal(MobEffectCategory.BENEFICIAL, 0xffff54));
    public static final RegistryObject<MobEffect> LIGHTNING_WHIRL_EFFECT = EFFECTS.register("lightning_whirl_effect", ()-> new LightningWhirlEffect(MobEffectCategory.BENEFICIAL, 0xffff54));
    public static final RegistryObject<MobEffect> VENGEFUL_STANCE = EFFECTS.register("vengeful_stance", ()-> new VengefulStance(MobEffectCategory.BENEFICIAL, 0xffff54));
    public static final RegistryObject<MobEffect> WARDLOCKED = EFFECTS.register("wardlocked", ()-> new Wardlocked(MobEffectCategory.NEUTRAL, 0xffff54));
    public static final RegistryObject<MobEffect> FLUXED = EFFECTS.register("fluxed", ()-> new Flux(MobEffectCategory.HARMFUL, 0x0F52BA));
    public static final RegistryObject<MobEffect> OVERLOAD = EFFECTS.register("overload", ()-> new Overload(MobEffectCategory.NEUTRAL, 0xffff54));
    public static final RegistryObject<MobEffect> ENDERSGAZE = EFFECTS.register("endersgaze", ()-> new EndersGaze(MobEffectCategory.BENEFICIAL, 0xffff54));
    public static final RegistryObject<MobEffect> WATCHYOURHEAD = EFFECTS.register("watchyourhead", ()-> new watchyourhead(MobEffectCategory.HARMFUL, 0xffff54));
    public static final RegistryObject<MobEffect> ECHOES = EFFECTS.register("echoes", ()-> new Echoes(MobEffectCategory.HARMFUL, 0xffff54));
    public static final RegistryObject<MobEffect> SPELLWEAVING = EFFECTS.register("spellweaving", ()-> new Spellweaving(MobEffectCategory.HARMFUL, 0x0F52BA));
    public static final RegistryObject<MobEffect> WARDABSORPTION = EFFECTS.register("wardabsorption", ()-> new WardAbsorption(MobEffectCategory.BENEFICIAL, 0xffff54));
    public static final RegistryObject<MobEffect> VOLATILEBLOOD = EFFECTS.register("volatileblood", ()-> new VolatileBloodEffect(MobEffectCategory.HARMFUL, 0x990000));
    public static final RegistryObject<MobEffect> PERFECTBLOOD = EFFECTS.register("perfectblood", ()-> new PerfectBlood(MobEffectCategory.BENEFICIAL, 0x990000));









}
