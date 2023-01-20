package com.cleannrooster.spellblademod.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "spellblademod");
    //public static final RegistryObject<Item> HAMMER = ITEMS.register("steelball",
   //         () -> new SteelBall(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).fireResistant().stacksTo(16)));
    public static final RegistryObject<EntityType<HammerEntity>> TRIDENT = ENTITIES.register("splitting_trident",
            () -> EntityType.Builder.of(HammerEntity::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(100).setUpdateInterval(1).fireImmune().setShouldReceiveVelocityUpdates(true).build(new ResourceLocation("spellblademod","hammer").toString()));

    public static final RegistryObject<EntityType<ConduitSpearEntity>> SPEAR = ENTITIES.register("conduitspear",
            () -> EntityType.Builder.<ConduitSpearEntity>of(ConduitSpearEntity::new, MobCategory.MISC).sized(1.5F, 1.5F).setTrackingRange(100).setUpdateInterval(1).fireImmune().setShouldReceiveVelocityUpdates(true).build(new ResourceLocation("spellblademod","conduitspear").toString()));
    public static final RegistryObject<EntityType<EndersEyeEntity>> ENDERS_EYE = ENTITIES.register("enderseyeentity",
            () -> EntityType.Builder.<EndersEyeEntity>of(EndersEyeEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "enderseye").toString()));
    public static final RegistryObject<EntityType<VolatileEntity>> VOLATILE_ENTITY = ENTITIES.register("volatile_entity",
            () -> EntityType.Builder.<VolatileEntity>of(VolatileEntity::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "volatileentity").toString()));
    public static final RegistryObject<EntityType<FluxEntity>> FLUX_ENTITY = ENTITIES.register("flux_entity",
            () -> EntityType.Builder.<FluxEntity>of(FluxEntity::new, MobCategory.MISC).sized(0.8F, 0.8F).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "fluxentity").toString()));
    public static final RegistryObject<EntityType<ReverberatingRay>> REVERBERATING_RAY_ORB = ENTITIES.register("reverberatingrayorb",
            () -> EntityType.Builder.<ReverberatingRay>of(ReverberatingRay::new, MobCategory.MISC).sized(0.1F, 0.1F).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "reverberatingrayorb").toString()));
    public static final RegistryObject<EntityType<SentinelEntity>> SENTINEL = ENTITIES.register("sentinel",
            () -> EntityType.Builder.<SentinelEntity>of(SentinelEntity::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "sentinel").toString()));
    public static final RegistryObject<EntityType<InvisiVex>> INVISIVEX = ENTITIES.register("invisivex",
            () -> EntityType.Builder.<InvisiVex>of(InvisiVex::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "sentinel").toString()));
    public static final RegistryObject<EntityType<sword1>> SWORD = ENTITIES.register("sword",
            () -> EntityType.Builder.<sword1>of(sword1::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "sword").toString()));
    public static final RegistryObject<EntityType<BouncingEntity>> BETTY = ENTITIES.register("betty",
            () -> EntityType.Builder.<BouncingEntity>of(BouncingEntity::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "betty").toString()));
    public static final RegistryObject<EntityType<ThrownItems>> FIREBALL = ENTITIES.register("fireball",
            () -> EntityType.Builder.<ThrownItems>of(ThrownItems::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "fireball").toString()));
    public static final RegistryObject<EntityType<VolleyballEntity>> VOLLEYBALL = ENTITIES.register("volleyball",
            () -> EntityType.Builder.<VolleyballEntity>of(VolleyballEntity::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "volleyball").toString()));
    public static final RegistryObject<EntityType<SpiderSpark>> SPARK = ENTITIES.register("spark",
            () -> EntityType.Builder.<SpiderSpark>of(SpiderSpark::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "spark").toString()));
    public static final RegistryObject<EntityType<ImpaleEntity>> IMPALE = ENTITIES.register("impaleentity",
            () -> EntityType.Builder.<ImpaleEntity>of(ImpaleEntity::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "impale").toString()));
    public static final RegistryObject<EntityType<WinterBurialEntity>> WINTERBURIAL = ENTITIES.register("winterburialentity",
            () -> EntityType.Builder.<WinterBurialEntity>of(WinterBurialEntity::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "impale").toString()));
    public static final RegistryObject<EntityType<EssenceBoltEntity>> ESSENCEBOLT = ENTITIES.register("essenceboltentity",
            () -> EntityType.Builder.<EssenceBoltEntity>of(EssenceBoltEntity::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "essencebolt").toString()));
    public static final RegistryObject<EntityType<FireworkEntity>> FIREWORK = ENTITIES.register("fireworkentity",
            () -> EntityType.Builder.<FireworkEntity>of(FireworkEntity::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "firework").toString()));
    public static final RegistryObject<EntityType<CatSpark>> CATSPARK = ENTITIES.register("catspark",
            () -> EntityType.Builder.<CatSpark>of(CatSpark::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "catspark").toString()));
    public static final RegistryObject<EntityType<LesserLightning>> LIGHTNING = ENTITIES.register("lightning",
            () -> EntityType.Builder.<LesserLightning>of(LesserLightning::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "lightning").toString()));
    public static final RegistryObject<EntityType<BindProjectile>> BINDPROJ = ENTITIES.register("bindproj",
            () -> EntityType.Builder.<BindProjectile>of(BindProjectile::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "bind").toString()));
    public static final RegistryObject<EntityType<EnderBindEntity>> ENDERBIND = ENTITIES.register("enderbind",
            () -> EntityType.Builder.<EnderBindEntity>of(EnderBindEntity::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "enderbind").toString()));
    public static final RegistryObject<EntityType<ShieldEntity>> SHIELD = ENTITIES.register("shield",
            () -> EntityType.Builder.<ShieldEntity>of(ShieldEntity::new, MobCategory.MISC).sized(0.75F, (float)(1F+0.0625*8F)).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "enderbind").toString()));
    public static final RegistryObject<EntityType<AmethystEntity>> AMETHYST = ENTITIES.register("amethyst",
            () -> EntityType.Builder.<AmethystEntity>of(AmethystEntity::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "enderbind").toString()));
    public static final RegistryObject<EntityType<SpectralBlades>> SPECTRALBLADES = ENTITIES.register("spectralblades",
            () -> EntityType.Builder.<SpectralBlades>of(SpectralBlades::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(100).updateInterval(1).build(new ResourceLocation("spellblademod", "enderbind").toString()));


}
