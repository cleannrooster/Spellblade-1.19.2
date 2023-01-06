package com.cleannrooster.spellblademod.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.Wolf;

import java.util.Objects;

import static net.minecraft.client.renderer.entity.LivingEntityRenderer.isEntityUpsideDown;

public class FireworkRenderer extends EntityRenderer<FireworkEntity> {
    private final ItemRenderer itemRenderer;
    private final CatModel<Cat> cat;
    private final WolfModel<Wolf> dog;
    private final RenderType dogtype;

    public FireworkRenderer(EntityRendererProvider.Context p_174114_) {
        super(p_174114_);
        this.itemRenderer = p_174114_.getItemRenderer();
        this.cat = new CatModel<>(p_174114_.bakeLayer(ModelLayers.CAT));

        this.dog = new WolfModel<>(p_174114_.bakeLayer(ModelLayers.WOLF));
        this.dogtype = this.dog.renderType(new ResourceLocation("textures/entity/wolf/wolf.png"));
    }

    @Override
    public void render(FireworkEntity p_114656_, float p_114657_, float p_114658_, PoseStack p_114659_, MultiBufferSource p_114660_, int p_114661_) {
        if(Objects.equals(p_114656_.getCustomName(), Component.translatable("cat"))) {
            int p_115310_ = p_114661_;
            PoseStack p_115311_ = p_114659_;
            Cat p_115308_ = new Cat(EntityType.CAT,p_114656_.getLevel());
            p_115308_.setXRot(p_114656_.getXRot());
            p_115308_.setYRot(p_114656_.getYRot());
            boolean shouldSit = p_115308_.isPassenger() && (p_115308_.getVehicle() != null && p_115308_.getVehicle().shouldRiderSit());

            float f = Mth.rotLerp(p_115310_, p_115308_.getYRot(), p_115308_.getYRot());
            float f1 = Mth.rotLerp(p_115310_, p_115308_.getYRot(), p_115308_.getYRot());
            float f2 = f1 - f;
            if (shouldSit && p_115308_.getVehicle() instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)p_115308_.getVehicle();
                f = Mth.rotLerp(p_115310_, livingentity.getYRot(), livingentity.getYRot());
                f2 = f1 - f;
                float f3 = Mth.wrapDegrees(f2);
                if (f3 < -85.0F) {
                    f3 = -85.0F;
                }

                if (f3 >= 85.0F) {
                    f3 = 85.0F;
                }

                f = f1 - f3;
                if (f3 * f3 > 2500.0F) {
                    f += f3 * 0.2F;
                }

                f2 = f1 - f;
            }

            float f6 = Mth.lerp(p_115310_, p_115308_.xRotO, p_115308_.getXRot());


            if (p_115308_.hasPose(Pose.SLEEPING)) {
                Direction direction = p_115308_.getBedOrientation();
                if (direction != null) {
                    float f4 = p_115308_.getEyeHeight(Pose.STANDING) - 0.1F;
                    p_115311_.translate((double)((float)(-direction.getStepX()) * f4), 0.0D, (double)((float)(-direction.getStepZ()) * f4));
                }
            }

            float f7 = getBob(p_115308_, p_115310_);
            this.setupRotations(p_115308_, p_115311_, f7, f, p_115310_);

            p_115311_.scale(-1.0F, -1.0F, 1.0F);
            p_115311_.translate(0.0D, (double)-1.501F, 0.0D);
            float f8 = 0.0F;
            float f5 = 0.0F;
            if (!shouldSit && p_115308_.isAlive()) {
                f8 = Mth.lerp(p_115310_, p_115308_.animationSpeedOld, p_115308_.animationSpeed);
                f5 = p_115308_.animationPosition - p_115308_.animationSpeed * (1.0F - p_115310_);
                if (p_115308_.isBaby()) {
                    f5 *= 3.0F;
                }

                if (f8 > 1.0F) {
                    f8 = 1.0F;
                }
            }
            boolean flag = true;
            Minecraft minecraft = Minecraft.getInstance();

            boolean flag1 = !flag && !p_114656_.isInvisibleTo(minecraft.player);
            boolean flag2 = minecraft.shouldEntityAppearGlowing(p_114656_);
            VertexConsumer vertexconsumer = p_114660_.getBuffer(this.cat.renderType(p_114656_.type));
            int i = OverlayTexture.pack(OverlayTexture.u(0.0F), OverlayTexture.v(new Cat(EntityType.CAT,p_114656_.getLevel()).hurtTime > 0 || new Cat(EntityType.CAT,p_114656_.getLevel()).deathTime > 0));
            this.cat.renderToBuffer(p_115311_, vertexconsumer, p_114661_, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
        }

            if (Objects.equals(p_114656_.getCustomName(), Component.translatable("dog"))) {
                int p_115310_ = p_114661_;
                PoseStack p_115311_ = p_114659_;
                Wolf p_115308_ = new Wolf(EntityType.WOLF,p_114656_.getLevel());
                p_115308_.setXRot(p_114656_.getXRot());
                p_115308_.setYRot(p_114656_.getYRot());

                boolean shouldSit = p_115308_.isPassenger() && (p_115308_.getVehicle() != null && p_115308_.getVehicle().shouldRiderSit());

                float f = Mth.rotLerp(p_115310_, p_115308_.getYRot(), p_115308_.getYRot());
                float f1 = Mth.rotLerp(p_115310_, p_115308_.getYRot(), p_115308_.getYRot());
                float f2 = f1 - f;
                if (shouldSit && p_115308_.getVehicle() instanceof LivingEntity) {
                    LivingEntity livingentity = (LivingEntity)p_115308_.getVehicle();
                    f = Mth.rotLerp(p_115310_, livingentity.getYRot(), livingentity.getYRot());
                    f2 = f1 - f;
                    float f3 = Mth.wrapDegrees(f2);
                    if (f3 < -85.0F) {
                        f3 = -85.0F;
                    }

                    if (f3 >= 85.0F) {
                        f3 = 85.0F;
                    }

                    f = f1 - f3;
                    if (f3 * f3 > 2500.0F) {
                        f += f3 * 0.2F;
                    }

                    f2 = f1 - f;
                }

                float f6 = Mth.lerp(p_115310_, p_115308_.xRotO, p_115308_.getXRot());


                if (p_115308_.hasPose(Pose.SLEEPING)) {
                    Direction direction = p_115308_.getBedOrientation();
                    if (direction != null) {
                        float f4 = p_115308_.getEyeHeight(Pose.STANDING) - 0.1F;
                        p_115311_.translate((double)((float)(-direction.getStepX()) * f4), 0.0D, (double)((float)(-direction.getStepZ()) * f4));
                    }
                }

                float f7 = getBob(p_115308_, p_115310_);
                this.setupRotations(p_115308_, p_115311_, f7, f, p_115310_);
                p_115311_.scale(-1.0F, -1.0F, 1.0F);
                p_115311_.translate(0.0D, (double)-1.501F, 0.0D);
                float f8 = 0.0F;
                float f5 = 0.0F;
                if (!shouldSit && p_115308_.isAlive()) {
                    f8 = Mth.lerp(p_115310_, p_115308_.animationSpeedOld, p_115308_.animationSpeed);
                    f5 = p_115308_.animationPosition - p_115308_.animationSpeed * (1.0F - p_115310_);
                    if (p_115308_.isBaby()) {
                        f5 *= 3.0F;
                    }

                    if (f8 > 1.0F) {
                        f8 = 1.0F;
                    }
                }
                boolean flag = true;
                Minecraft minecraft = Minecraft.getInstance();

                boolean flag1 = !flag && !p_114656_.isInvisibleTo(minecraft.player);
                boolean flag2 = minecraft.shouldEntityAppearGlowing(p_114656_);
                VertexConsumer vertexconsumer = p_114660_.getBuffer(this.dogtype);
                int i = OverlayTexture.pack(OverlayTexture.u(0.0F), OverlayTexture.v(new Cat(EntityType.CAT,p_114656_.getLevel()).hurtTime > 0 || new Cat(EntityType.CAT,p_114656_.getLevel()).deathTime > 0));
                this.dog.renderToBuffer(p_115311_, vertexconsumer, p_114661_, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
            }
            if (!p_114656_.hasCustomName()) {

                p_114659_.pushPose();
                p_114659_.mulPose(this.entityRenderDispatcher.cameraOrientation());
                p_114659_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                if (p_114656_.isShotAtAngle()) {
                    p_114659_.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
                    p_114659_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                    p_114659_.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                }
                this.itemRenderer.renderStatic(p_114656_.getItem(), ItemTransforms.TransformType.GROUND, p_114661_, OverlayTexture.NO_OVERLAY, p_114659_, p_114660_, p_114656_.getId());
                p_114659_.popPose();
            }

    }
    protected float getBob(LivingEntity p_115305_, float p_115306_) {
        return (float)p_115305_.tickCount + p_115306_;
    }
    protected void setupRotations(LivingEntity p_115317_, PoseStack p_115318_, float p_115319_, float p_115320_, float p_115321_) {


        if (!p_115317_.hasPose(Pose.SLEEPING)) {
            p_115318_.mulPose(Vector3f.YP.rotationDegrees(180+p_115320_));
            p_115318_.mulPose(Vector3f.XP.rotationDegrees(p_115317_.getXRot()));

        }

        if (p_115317_.deathTime > 0) {
            float f = ((float)p_115317_.deathTime + p_115321_ - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            p_115318_.mulPose(Vector3f.ZP.rotationDegrees(f * 90));
        } else if (p_115317_.isAutoSpinAttack()) {
            p_115318_.mulPose(Vector3f.XP.rotationDegrees(-90.0F - p_115317_.getXRot()));
            p_115318_.mulPose(Vector3f.YP.rotationDegrees(((float)p_115317_.tickCount + p_115321_) * -75.0F));
        }

        else if (isEntityUpsideDown(p_115317_)) {
            p_115318_.translate(0.0D, (double)(p_115317_.getBbHeight() + 0.1F), 0.0D);
            p_115318_.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        }

    }
    @Override
    public ResourceLocation getTextureLocation(FireworkEntity p_114482_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
