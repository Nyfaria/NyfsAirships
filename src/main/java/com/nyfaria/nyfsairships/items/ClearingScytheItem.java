package com.nyfaria.nyfsairships.items;

import com.nyfaria.nyfsairships.NyfsAirships;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ClearingScytheItem extends Item {
    public ClearingScytheItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        for (int i = user.blockPosition().getX()-10; i < user.blockPosition().getX()+10; i++) {
            for (int j = user.blockPosition().getY()-3; j < user.blockPosition().getY()+3; j++) {
                for (int k = user.blockPosition().getZ()-10; k < user.blockPosition().getZ()+10; k++) {
                    if(world.getBlockState(new BlockPos(i,j,k)).is(NyfsAirships.SCYTHEABLE))
                    {
                        world.destroyBlock(new BlockPos(i,j,k),true,user);
                        user.getItemInHand(hand).hurtAndBreak(1,user,(dobreak)-> dobreak.broadcastBreakEvent(hand));
                    }
                }
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        tooltip.add(new TranslatableComponent("scythe.nyfsairships.tooltip"));
    }
}
