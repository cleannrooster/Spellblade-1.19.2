package com.cleannrooster.spellblademod.patreon;

import com.cleannrooster.spellblademod.SpellbladeMod;
import com.cleannrooster.spellblademod.manasystem.network.CatPacket;
import com.cleannrooster.spellblademod.setup.Messages;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.cleannrooster.spellblademod.patreon.Patreon.allowed;

@OnlyIn(Dist.CLIENT)
public class PatreonMenu extends Screen {
    protected PatreonMenu(Component p_96550_) {
        super(p_96550_);
    }
    Widget emeraldbutton;
    Widget refreshbutton;
    Widget catbutton;
    Widget catsanddogs;
    public static final Component EMERALD_BLADE_FLURRY = Component.translatable("Emerald Blade Flurry Toggle");
    public static final Component EMERALD_BLADE_FLURRY_Enabled = Component.translatable("Emerald Blade Flurry: Enabled");
    public static final Component CATSPIDERS = Component.translatable("Cat Spiders");
    public static final Component CATSANDDOGS = Component.translatable("Cats and Dogs Celebration");


    @Override
    protected void init() {
        this.refreshbutton = this.addRenderableWidget(new Button(this.width / 2 - 102, this.height / 4 + 120 + -16 - 23, 204, 20,Component.translatable("OP: Refresh List"),(p_210872_) -> {
            Messages.sendToServer(new RefreshPacket());
        }));
        if(allowed(Minecraft.getInstance().player, SpellbladeMod.UUIDS)) {
            this.emeraldbutton = this.addRenderableWidget(new Button(this.width / 2 - 102, this.height / 4 + 120 + -16, 204, 20,EMERALD_BLADE_FLURRY,(p_210872_) -> {
                setEnabled("emeraldbladeflurry");
            }));
        }
        if(allowed(Minecraft.getInstance().player,SpellbladeMod.CATUUIDS)) {
            this.catbutton = this.addRenderableWidget(new Button(this.width / 2 - 102, this.height / 4 + 120 + -16 + 23, 204, 20,CATSPIDERS,(p_210872_) -> {
                setEnabled("catspiders");
            }));
        }
            this.catsanddogs = this.addRenderableWidget(new Button(this.width / 2 - 102, this.height / 4 + 120 + -16 + 23 + 23, 204, 20,CATSANDDOGS,(p_210872_) -> {
                setEnabled("catsanddogs");
            }));
    }


    protected void setEnabled(String string) {
        if (string.equals("emeraldbladeflurry")) {

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(Minecraft.getInstance().player.getUUID());
            Messages.sendToServer(new EmeraldPacket(buf));
        }
        if (string.equals("catspiders")) {

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(Minecraft.getInstance().player.getUUID());
            Messages.sendToServer(new CatPacket(buf));
        }
        if (string.equals("catsanddogs")) {

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(Minecraft.getInstance().player.getUUID());
            Messages.sendToServer(new CatsAndDogsPacket(buf));
        }
    }
}
