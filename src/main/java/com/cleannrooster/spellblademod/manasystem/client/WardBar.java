package com.cleannrooster.spellblademod.manasystem.client;

import com.cleannrooster.spellblademod.manasystem.manatick;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class WardBar implements IGuiOverlay {
    WardBarGUI gooie;
    public WardBar(WardBarGUI gui) {
        this.gooie = gui;
    }
    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        if (!Minecraft.getInstance().options.hideGui)
        {
            if(Minecraft.getInstance().player.getAttributeValue(manatick.WARD) > 2) {

                gui.leftHeight -= 3;
                gui.rightHeight -= 3;
            }
                gooie.renderBossHealth(poseStack, gui.leftHeight);
                if(Minecraft.getInstance().player.getAttributeValue(manatick.WARD) > 2) {
                gui.leftHeight += 10;
                gui.rightHeight += 10;
            }
        }
    }

    private void bind(ResourceLocation res)
    {
        RenderSystem.setShaderTexture(0, res);
    }


}
