package gd.rf.acro.platos.items;

import gd.rf.acro.platos.PlatosTransporters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class LiftJackItem extends Item {
    public LiftJackItem(Properties settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if(!stack.hasTag())
        {
            CompoundTag tag = new CompoundTag();
            tag.putInt("off",1);
            stack.setTag(tag);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().getBlockState(context.getClickedPos()).getBlock()!= PlatosTransporters.BLOCK_CONTROL_WHEEL)
        {
            Player user = context.getPlayer();
            InteractionHand hand = context.getHand();
            CompoundTag tag = new CompoundTag();
            tag.putInt("off",1);
            if(user.getItemInHand(hand).hasTag())
            {
                tag=user.getItemInHand(hand).getTag();
            }
            if(user.isShiftKeyDown() && tag.getInt("off")>1)
            {
                tag.putInt("off",tag.getInt("off")-1);

            }
            else
            {
                tag.putInt("off",tag.getInt("off")+1);
            }
            user.displayClientMessage(new TextComponent("new height: "+tag.getInt("off")),true);
            user.getItemInHand(hand).setTag(tag);
            PlatosTransporters.givePlayerStartBook(user);
        }
        return super.useOn(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        if(stack.hasTag())
        {
            tooltip.add(new TranslatableComponent("liftjack.platos.tooltip"));
            tooltip.add(new TextComponent(stack.getTag().getInt("off")+" ").append(new TranslatableComponent("liftjack.platos.tooltip2")));

        }
    }
}
