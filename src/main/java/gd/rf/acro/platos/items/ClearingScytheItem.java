package gd.rf.acro.platos.items;

import gd.rf.acro.platos.PlatosTransporters;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class ClearingScytheItem extends Item {


    public ClearingScytheItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

   

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player user = context.getPlayer();
        BlockPos pos = new BlockPos(user.getX(),user.getY(),user.getZ());
        for (int i = pos.getX()-10; i < pos.getX()+10; i++) {
            for (int j = pos.getY()-3; j < pos.getY()+3; j++) {
                for (int k = pos.getZ()-10; k < pos.getZ()+10; k++) {
                    if(PlatosTransporters.SCYTHEABLE.contains(context.getLevel().getBlockState(new BlockPos(i,j,k)).getBlock()))
                    {
                        context.getLevel().destroyBlock(new BlockPos(i,j,k),true,user);
                        user.getItemInHand(context.getHand()).hurtAndBreak(1,user,(dobreak)-> dobreak.broadcastBreakEvent(context.getHand()));
                    }
                }
            }
        }
        return super.useOn(context);
    }


    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable Level p_77624_2_, List<Component> p_77624_3_, TooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        p_77624_3_.add(new TranslatableComponent("scythe.platos.tooltip"));
    }
}
