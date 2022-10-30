package com.nyfaria.nyfsairships.items;

import com.nyfaria.nyfsairships.NyfsAirships;
import com.nyfaria.nyfsairships.entity.BlockShipEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WrenchItem extends Item {
    public WrenchItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if(user.getVehicle() instanceof BlockShipEntity)
        {
            ((BlockShipEntity) user.getVehicle()).tryDisassemble();
        }
        else
        {
            NyfsAirships.givePlayerStartBook(user);
        }
        return super.use(world, user, hand);
    }


}
