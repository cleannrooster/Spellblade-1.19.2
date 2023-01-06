package com.cleannrooster.spellblademod.manasystem.client;

import com.cleannrooster.spellblademod.SpellbladeMod;
import com.cleannrooster.spellblademod.items.AmorphousPacket;
import com.cleannrooster.spellblademod.items.ModItems;
import com.cleannrooster.spellblademod.items.Spell;
import com.cleannrooster.spellblademod.items.Spellblade;
import com.cleannrooster.spellblademod.manasystem.manatick;
import com.cleannrooster.spellblademod.setup.Messages;
import io.netty.buffer.Unpooled;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.Set;

@Mod.EventBusSubscriber(modid = "spellblademod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TooltipEvent {
    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event){
        if (event.getItemStack().hasTag() && !(event.getItemStack().getItem() instanceof Spellblade) && EnchantmentHelper.getEnchantments(event.getItemStack()).containsKey(SpellbladeMod.spellproxy)) {
            if(event.getItemStack().getOrCreateTag().contains("Triggers")){
                ItemStack stack = event.getItemStack();
                Set<String> keys = stack.getOrCreateTag().getCompound("Triggers").getAllKeys();
                for(String key : keys){
                    if(stack.getOrCreateTag().getCompound("Triggers").getInt(key) > 0) {
                        MutableComponent mutablecomponent1 = Component.translatable("On Hit").withStyle(ChatFormatting.WHITE);
                        mutablecomponent1.append(": ");
                        ChatFormatting color = ChatFormatting.WHITE;
                        for (RegistryObject<Item> spell3 : ModItems.ITEMS.getEntries()) {
                            if(spell3.get() instanceof Spell spell){
                                if(Objects.equals(spell.getDescriptionId(), key)){
                                    color = spell.color();
                                }
                            }
                        }

                        MutableComponent mutablecomponent = Component.translatable(key).withStyle(color);


                        if (stack.getOrCreateTag().contains("AutoTrigger") && stack.getOrCreateTag().getCompound("AutoTrigger").contains(key)) {
                            mutablecomponent.append(" ").append(Component.translatable("Auto"));
                        } else {
                            mutablecomponent.append(" ").append(Component.translatable(/*"enchantment.level." +*/ String.valueOf(stack.getOrCreateTag().getCompound("Triggers").getInt(key))));
                        }
                        mutablecomponent1.append(mutablecomponent);
                        event.getToolTip().add(1,mutablecomponent1);
                    }
                }

            }
        }
    }


        @SubscribeEvent
    public static void rightclickevent(PlayerInteractEvent.RightClickEmpty event){
        if(event.getEntity().getInventory().getSelected().getItem() == Items.AIR && event.getEntity().isShiftKeyDown() && event.getEntity().getAttributeValue(manatick.BASEWARD) >=39){
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(event.getEntity().getUUID());
            Messages.sendToServer(new AmorphousPacket(buf));

        }
    }

}
