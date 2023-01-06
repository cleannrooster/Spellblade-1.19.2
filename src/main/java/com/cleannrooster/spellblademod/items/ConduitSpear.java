package com.cleannrooster.spellblademod.items;

import com.cleannrooster.spellblademod.entity.ConduitSpearEntity;
import com.cleannrooster.spellblademod.entity.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class ConduitSpear extends Spellblade{
    public ConduitSpear(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.SPEAR;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity p_41414_, int count) {
        List<String> spellList = new ArrayList<>();

            stack.hurtAndBreak(1,p_41414_,(p_41300_) -> {
                p_41300_.broadcastBreakEvent(p_41414_.getUsedItemHand());
            });
            if (p_41414_ instanceof Player p_41433_ ) {
                CompoundTag tag = stack.getOrCreateTag();
                for (ItemStack item : p_41433_.getInventory().items) {
                    if (item.getItem() instanceof Flask flask && tag.contains("AutoUse") && tag.getCompound("AutoUse").contains(Flask.getSpellItem(item))) {
                        flask.applyFlask(p_41433_, null, item, stack, false);
                    }
                }
                if (tag.contains("Oils")) {
                    for (int iii = 0; iii < tag.getCompound("Oils").getAllKeys().size(); iii++) {
                        boolean worked = true;
                        String spell = tag.getCompound("Oils").getAllKeys().stream().toList().get(iii);
                        if (tag.getCompound("Oils").getInt(spell) > 0) {

                            spellList.add(spell);
                            tag.getCompound("Oils").putInt(spell, tag.getCompound("Oils").getInt(spell) - 1);
                        }
                    }
                }

                for (ItemStack item : p_41433_.getInventory().items) {
                    if (item.getItem() instanceof Flask flask && tag.contains("AutoUse") && tag.getCompound("AutoUse").contains(Flask.getSpellItem(item))) {
                        flask.applyFlask(p_41433_, null, item, stack, false);
                    }
                }
            }
            if (p_41414_ instanceof Player player) {

                HitResult hitResult = Impale.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE, 16 * player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue());
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                    double xdist = hitResult.getLocation().x - player.getX();
                    double ydist = blockHitResult.getBlockPos().above().getY() - player.getEyeY();
                    double zdist = hitResult.getLocation().z - player.getZ();

                    double hordis = 0;
                    double theta = 90;
                    hordis = sqrt(blockHitResult.getBlockPos().above().distToCenterSqr(player.getEyePosition()) - ydist * ydist);
                    double Vy = ydist + 20;
                    double Vxz = (hordis * 20) / (Vy + sqrt(Vy * Vy + 2 * 20 * -ydist));
                    if (Float.isNaN((float) Vxz)) {
                        Vxz = 0;
                    }
                    if (!(abs(ydist) < 16)) {
                        return;
                    }
                    double Vxx = Vxz * player.getLookAngle().x;
                    double Vzz = Vxz * player.getLookAngle().z;

                    System.out.println(spellList);
                    ConduitSpearEntity spear = new ConduitSpearEntity(ModEntities.SPEAR.get(), level, spellList);
                    spear.setPos(p_41414_.getEyePosition());
                    spear.setOwner(p_41414_);
                    spear.setDeltaMovement(Vxx/20  + level.random.nextDouble()*0, Vy/20+ -0+level.random.nextDouble()*0, Vzz/20 + -0+level.random.nextDouble()*0);
                    level.addFreshEntity(spear);
                    player.getCooldowns().addCooldown(this,10);

                }

        }

    }
}
