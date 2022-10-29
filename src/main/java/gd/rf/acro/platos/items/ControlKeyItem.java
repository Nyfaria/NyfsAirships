package gd.rf.acro.platos.items;

import gd.rf.acro.platos.PlatosTransporters;
import gd.rf.acro.platos.entity.BlockShipEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ControlKeyItem extends Item {
    public ControlKeyItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if(user.getVehicle() instanceof BlockShipEntity && !world.isClientSide)
        {
            CompoundTag tag =((BlockShipEntity) user.getVehicle()).getItemBySlot(EquipmentSlot.CHEST).getTag();
            if(tag.getInt("type")==1)
            {
                user.getVehicle().setDeltaMovement(user.getLookAngle().x,user.getLookAngle().y,user.getLookAngle().z);
                if(((ListTag)tag.get("addons")).contains(StringTag.valueOf("altitude")))
                {
                    user.getVehicle().setNoGravity(false);
                    ((BlockShipEntity) user.getVehicle()).addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 9999, 2, true, false));
                }

            }
        }
        PlatosTransporters.givePlayerStartBook(user);
        return super.use(world, user, hand);
    }
}
