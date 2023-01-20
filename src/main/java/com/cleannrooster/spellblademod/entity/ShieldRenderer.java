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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

import static com.mojang.blaze3d.platform.GlStateManager.Viewport.x;

@OnlyIn(Dist.CLIENT)
public class ShieldRenderer extends EntityRenderer<ShieldEntity> {
    public static final ResourceLocation TEXTURE  = new ResourceLocation("spellblademod", "textures/item/shield_base_nopattern.png");
    private final ItemRenderer itemRenderer;
    private final shield model;
    public ShieldRenderer(EntityRendererProvider.Context p_174420_) {
        super(p_174420_);
        this.model = new shield<>(p_174420_.bakeLayer(shield.LAYER_LOCATION));
        this.itemRenderer = p_174420_.getItemRenderer();
    }


    public void render(ShieldEntity p_116111_, float p_116112_, float p_116113_, PoseStack p_116114_, MultiBufferSource p_116115_, int p_116116_) {
        if(p_116111_.getOwner() != null && p_116111_.getOwner() instanceof Player player) {

            double d0 = Mth.lerp((double) p_116113_, p_116111_.xOld, p_116111_.getX());
            double d1 = Mth.lerp((double) p_116113_, p_116111_.yOld, p_116111_.getY());
            double d2 = Mth.lerp((double) p_116113_, p_116111_.zOld, p_116111_.getZ());

            p_116114_.translate(-d0 + player.getPosition(p_116113_).x, -d1 + player.getPosition(p_116113_).y, -d2 + player.getPosition(p_116113_).z);
            Vec3 norm = player.getViewVector(p_116113_).subtract(0,player.getViewVector(p_116113_).y,0).normalize();
            p_116114_.translate(norm.x()*player.getBoundingBox().getXsize()/0.6, (0.0625 * 3), norm.z()*player.getBoundingBox().getXsize()/0.6);

            p_116114_.pushPose();
            p_116114_.mulPose(Vector3f.YP.rotationDegrees(180 - p_116111_.getOwner().getYRot()));
            p_116114_.mulPose(Vector3f.XP.rotationDegrees(0));
            VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(p_116115_, this.model.renderType(this.getTextureLocation(p_116111_)), false, false);
            this.model.renderToBuffer(p_116114_, vertexconsumer, p_116116_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

            super.render(p_116111_, p_116112_, p_116113_, p_116114_, p_116115_, p_116116_);


            p_116114_.popPose();
        }

    }

    @Override
    public ResourceLocation getTextureLocation(ShieldEntity p_114482_) {
        return TEXTURE;
    }

}