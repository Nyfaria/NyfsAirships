package gd.rf.acro.platos.items;

import gd.rf.acro.platos.PlatosTransporters;
import gd.rf.acro.platos.entity.BlockShipEntity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class BoardingStairsItem extends Item {
    public BoardingStairsItem(Properties settings) {
        super(settings);
    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {

        HitResult result = user.pick(100,0,true);
        for (int i = 0; i < 30; i++) {
            world.addParticle(ParticleTypes.SMOKE,user.getLookAngle().scale(i).x,user.getLookAngle().scale(i).y,user.getLookAngle().scale(i).z,0,0,0);
        }
        List<BlockShipEntity> vv = world.getEntities(PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE,new AABB(result.getLocation().add(-10,-10,-10),result.getLocation().add(10,10,10)), LivingEntity::isAlive);
        if(vv.size()>0)
        {
            user.startRiding(vv.get(0));
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable Level p_77624_2_, List<Component> p_77624_3_, TooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        p_77624_3_.add(new TranslatableComponent("boardingstairs.platos.tooltip"));
    }

}
