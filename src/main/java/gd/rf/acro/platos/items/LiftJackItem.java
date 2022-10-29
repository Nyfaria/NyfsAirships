package gd.rf.acro.platos.items;

import gd.rf.acro.platos.PlatosTransporters;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import net.minecraft.world.item.Item.Properties;

public class LiftJackItem extends Item {


    public LiftJackItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
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
            if(context.getHand()==InteractionHand.MAIN_HAND)
            {
                user.sendMessage(new TextComponent("new height: "+tag.getInt("off")), UUID.randomUUID());
            }
            user.getItemInHand(hand).setTag(tag);
            PlatosTransporters.givePlayerStartBook(user);
        }
        return super.useOn(context);
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_77624_2_, List<Component> tooltip, TooltipFlag p_77624_4_) {
        super.appendHoverText(stack, p_77624_2_, tooltip, p_77624_4_);
        if(stack.hasTag())
        {
            tooltip.add(new TranslatableComponent("liftjack.platos.tooltip"));
            tooltip.add(new TextComponent(stack.getTag().getInt("off")+""));
            tooltip.add(new TranslatableComponent("liftjack.platos.tooltip2"));
        }
    }
}
