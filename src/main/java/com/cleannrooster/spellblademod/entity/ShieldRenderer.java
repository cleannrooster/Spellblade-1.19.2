package com.cleannrooster.spellblademod.entity;

import com.cleannrooster.spellblademod.items.Shield;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Objects;

public class ShieldRenderer<T extends ShieldEntity> extends EntityRenderer<T> {
    public static final ResourceLocation TEXTURE  = new ResourceLocation("spellblademod", "textures/item/shield_base_nopattern.png");
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean fullBright;
    private final shield model;
    public ShieldRenderer(EntityRendererProvider.Context p_174420_, float p_174417_, boolean p_174418_) {
        super(p_174420_);
        this.model = new shield<>(p_174420_.bakeLayer(shield.LAYER_LOCATION));
        this.itemRenderer = p_174420_.getItemRenderer();
        this.scale = p_174417_;
        this.fullBright = p_174418_;
    }


    public void render(T p_116111_, float p_116112_, float p_116113_, PoseStack p_116114_, MultiBufferSource p_116115_, int p_116116_) {

        if(p_116111_.getOwner() != null) {
            p_116114_.pushPose();
            p_116114_.mulPose(Vector3f.YP.rotationDegrees(180-p_116111_.getOwner().getYRot()));
            p_116114_.mulPose(Vector3f.XP.rotationDegrees(-p_116111_.getOwner().getXRot()));
            VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(p_116115_, this.model.renderType(this.getTextureLocation(p_116111_)), false, false);
            this.model.renderToBuffer(p_116114_, vertexconsumer, p_116116_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

            p_116114_.popPose();
            super.render(p_116111_, p_116112_, p_116113_, p_116114_, p_116115_, p_116116_);
        }
    }




    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
            return TEXTURE;

    }
}