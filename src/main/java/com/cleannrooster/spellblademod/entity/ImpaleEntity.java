package com.cleannrooster.spellblademod.entity;

import com.cleannrooster.spellblademod.items.FriendshipBracelet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ImpaleEntity extends FallingBlockEntity {
    private Vec3 originalpos = new Vec3(0,0,0);
    public Player owner;
    private int internaltick;

    public BlockState getBlockState() {
        if(this.getCustomName().getString().equals("0")) return Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(BlockStateProperties.VERTICAL_DIRECTION, Direction.UP).setValue(BlockStateProperties.DRIPSTONE_THICKNESS, DripstoneThickness.TIP);
        else if (this.getCustomName().getString().equals("1")) return Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(BlockStateProperties.VERTICAL_DIRECTION, Direction.UP).setValue(BlockStateProperties.DRIPSTONE_THICKNESS, DripstoneThickness.MIDDLE);
        else return Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(BlockStateProperties.VERTICAL_DIRECTION, Direction.UP).setValue(BlockStateProperties.DRIPSTONE_THICKNESS, DripstoneThickness.BASE);
    }
    public float damage = 6;
    public int offset = 0;
    public boolean isTip;
    public ImpaleEntity(Level p_201972_, double v, double y, double v1, BlockState blockState, Player player,int size, float damage) {
        super(ModEntities.IMPALE.get(),p_201972_);
        this.blocksBuilding = true;
        this.setPos(v, y, v1);
        this.originalpos = new Vec3(v,y,v1);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = v;
        this.yo = y;
        this.zo = v1;
        this.setStartPos(this.blockPosition());
        this.setOwner(player);
        this.setCustomName(Component.translatable(String.valueOf(size)));
        this.damage = damage;
    }

    @Override
    public boolean shouldShowName() {
        return false;
    }

    public void setOwner(Player player){
        this.owner = player;
    }

    protected ImpaleEntity(EntityType<? extends ImpaleEntity> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }
    public ImpaleEntity(EntityType<? extends ImpaleEntity> p_36833_, Level p_36834_, Player player, BlockPos blockPos, float damage) {
        super(p_36833_, p_36834_);
        this.setOwner(player);
        this.setRot(0,-90);
        BlockState tip = Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(BlockStateProperties.VERTICAL_DIRECTION, Direction.UP).setValue(BlockStateProperties.DRIPSTONE_THICKNESS, DripstoneThickness.TIP);
        BlockState base = Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(BlockStateProperties.VERTICAL_DIRECTION, Direction.UP).setValue(BlockStateProperties.DRIPSTONE_THICKNESS, DripstoneThickness.BASE);
        ImpaleEntity.fall(level,blockPos.above().above(),tip,player,0, damage);
        ImpaleEntity.fall(level,blockPos.above(),base,player,1, damage);
        ImpaleEntity.fall(level,blockPos,base,player,2, damage);

    }
    public ImpaleEntity(EntityType<? extends ImpaleEntity> p_36833_, Level p_36834_, Player player, BlockPos blockPos, float damage, int offset1) {
        super(p_36833_, p_36834_);
        this.setOwner(player);
        this.setRot(0,-90);
        BlockState tip = Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(BlockStateProperties.VERTICAL_DIRECTION, Direction.UP).setValue(BlockStateProperties.DRIPSTONE_THICKNESS, DripstoneThickness.TIP);
        BlockState base = Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(BlockStateProperties.VERTICAL_DIRECTION, Direction.UP).setValue(BlockStateProperties.DRIPSTONE_THICKNESS, DripstoneThickness.BASE);
        ImpaleEntity.fall(level,blockPos.above().above(),tip,player,0, damage, offset1);
        ImpaleEntity.fall(level,blockPos.above(),base,player,1, damage, offset1);
        ImpaleEntity.fall(level,blockPos,base,player,2, damage, offset1);

    }
    public static ImpaleEntity fall(Level p_201972_, BlockPos p_201973_, BlockState p_201974_, Player player, int size, float damage) {
        ImpaleEntity fallingblockentity = new ImpaleEntity(p_201972_, (double)p_201973_.getX() + 0.5D, (double)p_201973_.getY()-2, (double)p_201973_.getZ() + 0.5D, p_201974_.hasProperty(BlockStateProperties.WATERLOGGED) ? p_201974_.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)) : p_201974_, player, size, damage);
        fallingblockentity.damage = damage;
        if(!p_201972_.isClientSide()) {
            p_201972_.addFreshEntity(fallingblockentity);
        }
        return fallingblockentity;
    }
    public static ImpaleEntity fall(Level p_201972_, BlockPos p_201973_, BlockState p_201974_, Player player, int size, float damage, int offset1) {
        ImpaleEntity fallingblockentity = new ImpaleEntity(p_201972_, (double)p_201973_.getX() + 0.5D, (double)p_201973_.getY()-2, (double)p_201973_.getZ() + 0.5D, p_201974_.hasProperty(BlockStateProperties.WATERLOGGED) ? p_201974_.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)) : p_201974_, player, size, damage);
        fallingblockentity.damage = damage;
        fallingblockentity.offset = Math.abs(offset1);
        fallingblockentity.tickCount = -Math.abs(offset1);
        if(!p_201972_.isClientSide()) {
            p_201972_.addFreshEntity(fallingblockentity);
        }
        return fallingblockentity;
    }


    @Override
    public void tick() {
        if(this.owner == null){
            if(!this.getLevel().isClientSide()) {
                this.discard();
                return;
            }
        }




        this.setNoGravity(true);
        this.noPhysics = true;

        if(firstTick){
            SoundEvent soundEvent = SoundEvents.BLAZE_SHOOT;
            this.playSound(soundEvent, 0.25F, 0.5F);
        }
        if(this.tickCount >= 1 && this.tickCount < 3 && !this.getLevel().isClientSide()){
            this.setPos(this.position().add(0,6F/5F,0));
            List<LivingEntity> list = this.getLevel().getEntitiesOfClass(LivingEntity.class,this.getBoundingBox().expandTowards(0,0.5,0).inflate(0.5));
            if(this.getCustomName().getString().equals("2") && !list.isEmpty()){
                for(LivingEntity living : list){
                    if(this.canAddPassenger(living) && living != this.owner && FriendshipBracelet.PlayerFriendshipPredicate(this.owner,living)){
                        living.hurt( new EntityDamageSource("impale",this.owner),(float)Math.max(6,this.damage));
                        living.startRiding(this);
                    }
                }
            }
        }
        else if (this.tickCount <= 0 && !this.getLevel().isClientSide()){
            this.setPos(this.originalpos);
        }
        if(this.tickCount > 100  && !this.getLevel().isClientSide()){
            if(this.getCustomName().getString().equals("1")) {
                this.getLevel().explode(this.owner, this.getX(), this.getY(), this.getZ(), (float)Math.max(1.5,this.damage/4), Explosion.BlockInteraction.NONE);


            }
            this.discard();
        }
        this.firstTick = false;
    }
}
