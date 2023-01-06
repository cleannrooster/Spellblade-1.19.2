package com.cleannrooster.spellblademod.manasystem.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import java.util.Map;
import java.util.UUID;

public class WardBarGUI extends ForgeGui {
    public Minecraft minecraft;
    public WardBarBossOverlay overlay;
    public WardBarGUI(Minecraft mc) {
        super(mc);
        this.minecraft = mc;
        this.overlay = new WardBarBossOverlay(mc);
    }
    final Map<UUID, LerpingBossEvent> events = Maps.newLinkedHashMap();


    protected void renderBossHealth(PoseStack poseStack, int ii)
    {
        bind(new ResourceLocation("spellblademod", "textures/gui/bars.png"));
        RenderSystem.defaultBlendFunc();
        minecraft.getProfiler().push("wardBar");
        this.overlay.render(poseStack, ii);
        minecraft.getProfiler().pop();
    }
    private void bind(ResourceLocation res)
    {
        RenderSystem.setShaderTexture(0, res);
    }

}
