package gd.rf.acro.platos.entity;

import com.mojang.math.Vector3f;
import gd.rf.acro.platos.ConfigUtils;
import gd.rf.acro.platos.PlatosTransporters;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class BlockShipEntity extends Pig {
    public BlockShipEntity(EntityType<? extends Pig> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public boolean canStandOnFluid(Fluid fluid) {
        if(this.getItemBySlot(EquipmentSlot.CHEST).getItem()==Items.OAK_PLANKS)
        {
            if(this.getItemBySlot(EquipmentSlot.CHEST).getTag().getInt("type")==0)
            {
                return FluidTags.WATER.contains(fluid);
            }
        }
        return false;
    }


    @Override
    public float getSpeed() {

        float cspeed = Float.parseFloat(ConfigUtils.config.getOrDefault("cspeed","0.2"));
        float nspeed = Float.parseFloat(ConfigUtils.config.getOrDefault("nspeed","0.05"));
        if(this.getControllingPassenger() instanceof  Player)
        {
            if(((Player) this.getControllingPassenger()).getMainHandItem().getItem()== PlatosTransporters.CONTROL_KEY_ITEM)
            {
                if(this.getItemBySlot(EquipmentSlot.CHEST).getItem()==Items.OAK_PLANKS)
                {
                    if(this.getItemBySlot(EquipmentSlot.CHEST).getTag().getInt("type")==0)
                    {
                        if(this.level.getBlockState(this.blockPosition().below()).getBlock()== Blocks.WATER)
                        {
                            ListTag go = (ListTag) this.getItemBySlot(EquipmentSlot.CHEST).getTag().get("addons");
                            if(go.contains(StringTag.valueOf("engine")))
                            {
                                return cspeed*1.5f;
                            }
                            return cspeed;
                        }
                        return nspeed;
                    }
                    if(this.getItemBySlot(EquipmentSlot.CHEST).getTag().getInt("type")==1)
                    {
                        return 0f;
                    }
                    else
                    {
                        ListTag go = (ListTag) this.getItemBySlot(EquipmentSlot.CHEST).getTag().get("addons");
                        if(go.contains(StringTag.valueOf("engine")))
                        {
                            return cspeed*1.5f;
                        }
                        return cspeed;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    protected void registerGoals() { }

    @Override
    public boolean rideableUnderWater() {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BOAT_PADDLE_WATER;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BOAT_PADDLE_WATER;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BOAT_PADDLE_WATER;
    }

    @Override
    protected SoundEvent getFallDamageSound(int distance) {
        return SoundEvents.BOAT_PADDLE_WATER;
    }

    @Override
    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.BOAT_PADDLE_WATER;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.BOAT_PADDLE_WATER;
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    public void setModel(ListTag input, int direction, int offset, int type, CompoundTag storage,ListTag addons)
    {
        ItemStack itemStack = new ItemStack(Items.OAK_PLANKS);
        CompoundTag tag = new CompoundTag();
        tag.putString("model", UUID.randomUUID().toString());
        tag.put("parts",input);
        tag.putInt("direction",direction);
        tag.putInt("offset",offset);
        tag.putInt("type",type);
        tag.put("storage",storage);
        tag.put("addons",addons);
        itemStack.setTag(tag);
        this.setItemSlot(EquipmentSlot.CHEST,itemStack);
    }



    @Override
    public int getMaxFallDistance() {
        return 400;
    }

    public void tryDisassemble()
    {
        if(this.getItemBySlot(EquipmentSlot.CHEST).getItem()==Items.OAK_PLANKS)
        {
            ListTag list = (ListTag) this.getItemBySlot(EquipmentSlot.CHEST).getTag().get("parts");
            int offset = this.getItemBySlot(EquipmentSlot.CHEST).getTag().getInt("offset");
            CompoundTag storage = this.getItemBySlot(EquipmentSlot.CHEST).getTag().getCompound("storage");
            for (Tag tag : list)
            {
                String[] split = tag.getAsString().split(" ");
                BlockState state =level.getBlockState(this.blockPosition().offset(Integer.parseInt(split[1]), Integer.parseInt(split[2]) + offset, Integer.parseInt(split[3])));
                if (!state.isAir() && !state.is(Blocks.WATER)) {
                    if (this.getControllingPassenger() instanceof Player) {
                        ((Player) this.getControllingPassenger()).displayClientMessage(new TextComponent("cannot disassemble, not enough space"), false);
                    }
                    return;
                }
            }
            list.forEach(block->
            {
                String[] split = block.getAsString().split(" ");
                level.setBlockAndUpdate(this.blockPosition().offset(Integer.parseInt(split[1]),Integer.parseInt(split[2])+offset,Integer.parseInt(split[3])), Block.stateById(Integer.parseInt(split[0])));
            });
            storage.getAllKeys().forEach(blockEntity->
            {
                String[] split = blockEntity.split(" ");
                BlockPos newpos = this.blockPosition().offset(Integer.parseInt(split[0]),Integer.parseInt(split[1])+offset,Integer.parseInt(split[2]));
                BlockEntity entity = level.getBlockEntity(newpos);
                CompoundTag data = storage.getCompound(blockEntity);
                if(data!=null)
                {
                    data.putInt("x", newpos.getX());
                    data.putInt("y", newpos.getY());
                    data.putInt("z", newpos.getZ());
                    entity.load(data);
                    entity.setChanged();
                }
            });
            this.ejectPassengers();
            this.teleportToWithTicket(0,-1000,0);
            this.dead=true;
            this.remove(RemovalReason.DISCARDED);
        }
    }

    private Integer[] shouldRotateStructure(int i, int j, int k)
    {
        if(!level.isClientSide){
            int direction = this.getItemBySlot(EquipmentSlot.CHEST).getTag().getInt("direction");
            int curDir = getClosestAxis();
            if(direction==curDir)
            {
                return new Integer[]{i,j,k};
            }
            if(direction+90==curDir)
            {
                return new Integer[]{k,j,i};
            }
            if(direction+180==curDir)
            {
                return new Integer[]{i,j,k};
            }
        }
        return new Integer[]{0,0,0};
    }
    private int getClosestAxis()
    {
        if(this.getYRot()>135 && this.getYRot()<225)
        {
            return 180;
        }
        if(this.getYRot()>225 && this.getYRot()<315)
        {
            return 270;
        }
        if(this.getYRot()>45 && this.getYRot()<135)
        {
            return 90;
        }
        return 0;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    protected boolean shouldPassengersInheritMalus() {
        if(this.getItemBySlot(EquipmentSlot.HEAD).getItem()==Items.STICK)
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean isEffectiveAi() {
        if(this.getItemBySlot(EquipmentSlot.HEAD).getItem()==Items.STICK)
        {
            return true;
        }
        return false;
    }

    @Override
    protected boolean isImmobile() {
        return true;
    }

    @Override
    public boolean canBeControlledByRider() {
        if(this.getItemBySlot(EquipmentSlot.HEAD).getItem()==Items.STICK)
        {
            return false;
        }
        return true;
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 hitPos, InteractionHand hand) {
        if(!player.getCommandSenderWorld().isClientSide && player.getItemInHand(hand)==ItemStack.EMPTY && hand==InteractionHand.MAIN_HAND)
        {
            player.startRiding(this,true);
            return InteractionResult.SUCCESS;
        }
        if(!player.getCommandSenderWorld().isClientSide && player.getItemInHand(hand).getItem()==PlatosTransporters.LIFT_JACK_ITEM && hand==InteractionHand.MAIN_HAND)
        {
            this.getItemBySlot(EquipmentSlot.CHEST).getTag().putInt("offset",player.getItemInHand(hand).getTag().getInt("off"));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public boolean isInvulnerable() {
        return !dead;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if(damageSource!=DamageSource.OUT_OF_WORLD)
        {
            return true;
        }
        return false;
    }



    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public void positionRider(Entity passenger) {
        int extra = 0;
        passenger.fallDistance=0;
        if(this.getItemBySlot(EquipmentSlot.CHEST).getItem()==Items.OAK_PLANKS)
        {
            extra = this.getItemBySlot(EquipmentSlot.CHEST).getTag().getInt("offset")-1;
        }
        if(this.getControllingPassenger() instanceof Player)
        {
            if(passenger==this.getControllingPassenger())
            {
                passenger.absMoveTo(this.getX(),this.getY()+0.5+extra,this.getZ());
            }
            else
            {
                Vector3f dir = this.getMotionDirection().getOpposite().step();
                int vv = this.getPassengers().indexOf(passenger);
                passenger.absMoveTo(this.getControllingPassenger().getX()+dir.x()*vv, this.getControllingPassenger().getY()+dir.y()*vv, this.getControllingPassenger().getZ()+dir.z()*vv);
            }
        }
    }

    @Override
    public void thunderHit(ServerLevel world, LightningBolt lightning) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canCollideWith(Entity other) {
        return true;
    }
}
