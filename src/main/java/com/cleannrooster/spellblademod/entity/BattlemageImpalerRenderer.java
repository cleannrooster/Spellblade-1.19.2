package com.cleannrooster.spellblademod.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;

import java.util.Objects;

public class BattlemageImpalerRenderer<T extends Entity> extends EntityRenderer<T>  {
    public static final ResourceLocation TEXTURE  = new ResourceLocation("spellblademod", "textures/entity/sword1.png");
    public static final ResourceLocation TEXTURE2  = new ResourceLocation("spellblademod", "textures/entity/sword2.png");
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean fullBright;
    private final BattlemageImpaler model;
    public BattlemageImpalerRenderer(EntityRendererProvider.Context p_174420_, float p_174417_, boolean p_174418_) {
        super(p_174420_);
        this.model = new BattlemageImpaler<>(p_174420_.bakeLayer(BattlemageImpaler.LAYER_LOCATION));
        this.itemRenderer = p_174420_.getItemRenderer();
        this.scale = p_174417_;
        this.fullBright = p_174418_;
    }
    public BattlemageImpalerRenderer(EntityRendererProvider.Context p_174414_) {
        this(p_174414_, 1.0F, false);
    }


    public void render(T p_116111_, float p_116112_, float p_116113_, PoseStack p_116114_, MultiBufferSource p_116115_, int p_116116_) {

            p_116114_.pushPose();
            p_116114_.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(p_116113_, p_116111_.yRotO, p_116111_.getYRot()) - 90.0F));
            p_116114_.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(p_116113_, p_116111_.xRotO, p_116111_.getXRot()) + 90.0F));
            VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(p_116115_, this.model.renderType(this.getTextureLocation(p_116111_)), false, false);
            this.model.renderToBuffer(p_116114_, vertexconsumer, p_116116_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            p_116114_.popPose();


    }




    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        if(Objects.equals(p_114482_.getCustomName(), Component.translatable("guard"))){
            return TEXTURE2;
        }else {
            return TEXTURE;
        }
    }
}
