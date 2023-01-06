package com.cleannrooster.spellblademod.manasystem.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class OverlayWard implements IGuiOverlay {
    GUI gooie;
    public OverlayWard(GUI gui) {
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
    }

}