package gd.rf.acro.platos.entity;

import gd.rf.acro.platos.ConfigUtils;
import gd.rf.acro.platos.PlatosTransporters;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class BlockShipEntity extends Pig {
    public BlockShipEntity(EntityType<? extends BlockShipEntity> entityType, Level world) {
        super(entityType, world);
    }


    public boolean canStandOnFluid(Fluid p_230285_1_) {
        return p_230285_1_.is(FluidTags.WATER);
    }

    @Override
    protected void registerGoals() {
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
    }



    @Override
    public float getSpeed() {
        if(this.getControllingPassenger() instanceof  Player)
        {
            if(((Player) this.getControllingPassenger()).getMainHandItem().getItem()== PlatosTransporters.CONTROL_KEY_ITEM)
            {
                float cspeed = Float.parseFloat(ConfigUtils.config.getOrDefault("cspeed","0.2"));
                float nspeed = Float.parseFloat(ConfigUtils.config.getOrDefault("nspeed","0.05"));
                if(this.getItemBySlot(EquipmentSlot.CHEST).getItem()==Items.OAK_PLANKS)
                {
                    if(this.getItemBySlot(EquipmentSlot.CHEST).getTag().getInt("type")==0)
                    {
                        if(this.level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock()== Blocks.WATER)
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
    public boolean canBeControlledByRider() {
        if(this.getItemBySlot(EquipmentSlot.HEAD).getItem()==Items.STICK)
        {
            return false;
        }
        return true;
    }



    @Override
    protected int calculateFallDamage(float p_225508_1_, float p_225508_2_) {
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
    public boolean isInvulnerableTo(DamageSource p_180431_1_) {
        if(p_180431_1_!=DamageSource.OUT_OF_WORLD)
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        if(!dead)
        {
            return true;
        }
        return false;
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
                BlockState state =level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement().above().offset(Integer.parseInt(split[1]), Integer.parseInt(split[2]) + offset, Integer.parseInt(split[3])));
                if (!state.isAir() && state.getBlock()!=(Blocks.WATER)) {
                    if (this.getControllingPassenger() instanceof Player) {
                        this.getControllingPassenger().sendMessage(new TextComponent("cannot disassemble, not enough space"), UUID.randomUUID());

                    }
                    return;
                }
            }
            list.forEach(block->
            {
                String[] split = block.getAsString().split(" ");
                level.setBlockAndUpdate(this.getBlockPosBelowThatAffectsMyMovement().offset(Integer.parseInt(split[1]),Integer.parseInt(split[2])+offset+1,Integer.parseInt(split[3])), Block.stateById(Integer.parseInt(split[0])));
            });
            storage.getAllKeys().forEach(blockEntity->
            {
                String[] split = blockEntity.split(" ");
                BlockPos newpos = this.getBlockPosBelowThatAffectsMyMovement().offset(Integer.parseInt(split[0]),Integer.parseInt(split[1])+offset+1,Integer.parseInt(split[2]));
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
        }
    }

    @Override
    public boolean requiresCustomPersistence() {
        return !this.dead;
    }

    @Override
    protected boolean canRide(Entity p_184228_1_) {
        return true;
    }

    @Override
    public boolean isControlledByLocalInstance() {
        if(this.getItemBySlot(EquipmentSlot.HEAD).getItem()==Items.STICK)
        {
            return false;
        }
        return true;
    }



    @Override
    public InteractionResult mobInteract(Player p_230254_1_, InteractionHand p_230254_2_) {
        if(!p_230254_1_.level.isClientSide)
        {
            p_230254_1_.startRiding(this);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 p_184199_2_, InteractionHand hand) {
        if(!player.getCommandSenderWorld().isClientSide && player.getItemInHand(hand)==ItemStack.EMPTY && hand==InteractionHand.MAIN_HAND)
        {
            return InteractionResult.SUCCESS;
        }
        if(!player.getCommandSenderWorld().isClientSide && player.getItemInHand(hand).getItem()==PlatosTransporters.LIFT_JACK_ITEM && hand==InteractionHand.MAIN_HAND)
        {
            this.getItemBySlot(EquipmentSlot.CHEST).getTag().putInt("offset",player.getItemInHand(hand).getTag().getInt("off"));
            return InteractionResult.SUCCESS;
        }
        return super.interactAt(player, p_184199_2_, hand);
    }

    @Override
    public void positionRider(Entity passenger) {
        super.positionRider(passenger);
        passenger.fallDistance=0;

        int extra = 0;
        if(this.getItemBySlot(EquipmentSlot.CHEST).getItem()==Items.OAK_PLANKS)
        {
            extra = this.getItemBySlot(EquipmentSlot.CHEST).getTag().getInt("offset")-1;
        }
        if(this.getControllingPassenger() instanceof Player)
        {
            if(passenger==this.getControllingPassenger())
            {
                passenger.setPos(this.getX(),this.getY()+0.5+extra,this.getZ());
            }
        }
    }





    @Override
    public void thunderHit(ServerLevel p_241841_1_, LightningBolt p_241841_2_) {

    }
}
