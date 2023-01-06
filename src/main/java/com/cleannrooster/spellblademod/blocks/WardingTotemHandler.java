package com.cleannrooster.spellblademod.blocks;

import com.cleannrooster.spellblademod.ModBlocks;
import com.cleannrooster.spellblademod.StatusEffectsModded;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "spellblademod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WardingTotemHandler {
    /*@SubscribeEvent
    public static void openitems(PlayerInteractEvent event) {

        BlockPos statepos = BlockPos.findClosestMatch(event.getPos(), 32, 32, (p_186148_) -> {
            return event.getLevel().getBlockState(p_186148_).is(ModBlocks.WARDING_TOTEM_BLOCK.get());
        }).orElse(null);
        if (statepos != null) {
            BlockEntity entity = event.getLevel().getBlockEntity(statepos);
            if (!event.getEntity().getItemInHand(event.getHand()).isEdible()) {
                if (!event.getEntity().hasEffect(StatusEffectsModded.TOTEMIC_ZEAL.get())) {
                    if (!(event.getLevel().getBlockState(event.getPos()).getBlock() instanceof WardingTotemBlock)) {
                        if (event.isCancelable()) {
                            if (entity instanceof WardingTotemBlockEntity && entity.getPersistentData().get("Inscribed") != null) {
                                String name = entity.getPersistentData().getString("Inscribed");
                                event.getEntity().displayClientMessage(Component.nullToEmpty(name + "'s warding totem located at " + statepos.toShortString() + " won't let you do that."), true);
                            }
                            else {
                                event.getEntity().displayClientMessage(Component.nullToEmpty("The warding totem located at " + statepos.toShortString() + " won't let you do that."), true);
                            }
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }*/
}
