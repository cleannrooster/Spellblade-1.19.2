package com.cleannrooster.spellblademod.client;

import com.cleannrooster.spellblademod.entity.*;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.renderer.entity.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "spellblademod", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    private ClientModEvents() {}
    @SubscribeEvent
    public static void registerlayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(HammerModel.LAYER_LOCATION, HammerModel::createLayer);
        event.registerLayerDefinition(SentinelModel.LAYER_LOCATION, SentinelModel::createBodyLayer);
        event.registerLayerDefinition(sword1model.LAYER_LOCATION, sword1model::createBodyLayer);
        event.registerLayerDefinition(BattlemageImpaler.LAYER_LOCATION, BattlemageImpaler::createBodyLayer);
        event.registerLayerDefinition(shield.LAYER_LOCATION, shield::createBodyLayer);


    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(ModEntities.TRIDENT.get(), HammerRenderer::new);
        event.registerEntityRenderer(ModEntities.ENDERS_EYE.get(), (p_174088_) -> {
            return new ThrownItemRenderer<>(p_174088_, 1.0F, true);
        });
        event.registerEntityRenderer(ModEntities.VOLATILE_ENTITY.get(), (p_174086_) -> {
            return new ThrownItemRenderer<>(p_174086_, 3.0F, true);
        });
        event.registerEntityRenderer(ModEntities.FLUX_ENTITY.get(), (p_174086_) -> {
            return new ThrownItemRenderer<>(p_174086_, 2.0F, true);
        });
        event.registerEntityRenderer(ModEntities.REVERBERATING_RAY_ORB.get(), (p_174086_) -> {
            return new ThrownItemRenderer<>(p_174086_, 2.0F, true);
        });
        event.registerEntityRenderer(ModEntities.SENTINEL.get(), SentinelRenderer::new);
        event.registerEntityRenderer(ModEntities.VOLLEYBALL.get(), VolleyballRenderer::new);
        event.registerEntityRenderer(ModEntities.SPARK.get(), SpiderSparkRenderer::new);

        event.registerEntityRenderer(ModEntities.INVISIVEX.get(), VexRenderer::new);
        event.registerEntityRenderer(ModEntities.SWORD.get(), sword1renderer::new);
        event.registerEntityRenderer(ModEntities.BETTY.get(), (p_174086_) -> {
            return new ThrownItemRenderer<>(p_174086_, 2.0F, true);});
        event.registerEntityRenderer(ModEntities.FIREBALL.get(), (p_174086_) -> {
            return new ThrownItemRenderer<>(p_174086_, 2.0F, true);});
        event.registerEntityRenderer(ModEntities.IMPALE.get(), FallingBlockRenderer::new);
        event.registerEntityRenderer(ModEntities.WINTERBURIAL.get(), FallingBlockRenderer::new);
        event.registerEntityRenderer(ModEntities.ESSENCEBOLT.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.FIREWORK.get(), FireworkRenderer::new);
        event.registerEntityRenderer(ModEntities.CATSPARK.get(), CatRenderer::new);
        event.registerEntityRenderer(ModEntities.SPEAR.get(), BattlemageImpalerRenderer::new);
        event.registerEntityRenderer(ModEntities.LIGHTNING.get(), LightningBoltRenderer::new);
        event.registerEntityRenderer(ModEntities.SHIELD.get(), ShieldRenderer::new);
        event.registerEntityRenderer(ModEntities.BINDPROJ.get(), (p_174081_) -> {
            return new ThrownItemRenderer<>(p_174081_, 1.0F, true);});
        event.registerEntityRenderer(ModEntities.ENDERBIND.get(), (p_174082_) -> {
            return new ThrownItemRenderer<>(p_174082_, 1.0F, true);});
        event.registerEntityRenderer(ModEntities.AMETHYST.get(), (p_174082_) -> {
            return new sword1renderer<>(p_174082_, 1.0F, true);});
        event.registerEntityRenderer(ModEntities.SPECTRALBLADES.get(), (p_174082_) -> {
            return new sword1renderer<>(p_174082_, 1.0F, true);});



    }
}
