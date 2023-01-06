package com.cleannrooster.spellblademod.manasystem.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class RenderCrosshair implements IGuiOverlay {
    GUI gooie;
    public RenderCrosshair(GUI gui) {
        this.gooie = gui;
    }

    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        if (!Minecraft.getInstance().options.hideGui && gui.shouldDrawSurvivalElements())
        {
            int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            gui.setupOverlayRenderState(true, false);
            gooie.leftHeight = gui.leftHeight;
            gooie.renderWard(screenWidth,screenHeight,poseStack);
            gui.leftHeight = gooie.leftHeight;
        }
    }}
