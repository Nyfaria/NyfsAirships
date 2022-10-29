package gd.rf.acro.platos.items;

import gd.rf.acro.platos.PlatosTransporters;
import gd.rf.acro.platos.entity.BlockShipEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class ControlKeyItem extends Item {


    public ControlKeyItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }




    @Override
    public InteractionResultHolder<ItemStack> use(Level p_77659_1_, Player user, InteractionHand p_77659_3_) {
        if(user.getVehicle() instanceof BlockShipEntity)
        {
            CompoundTag tag =((BlockShipEntity) user.getVehicle()).getItemBySlot(EquipmentSlot.CHEST).getTag();
            user.getVehicle().setDeltaMovement(user.getLookAngle().x, user.getLookAngle().y, user.getLookAngle().z);
            if(tag.getInt("type")==1)
            {
                if(((ListTag)tag.get("addons")).contains(StringTag.valueOf("altitude")))
                {
                    user.getVehicle().setNoGravity(false);
                    ((BlockShipEntity) user.getVehicle()).addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 9999, 2, true, false));

                }

            }
        }
        if(p_77659_1_.isClientSide)
        {
            PlatosTransporters.givePlayerStartBook(user);
        }
        return super.use(p_77659_1_, user, p_77659_3_);
    }
}
