package gd.rf.acro.platos.network;

import gd.rf.acro.platos.entity.BlockShipEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class MoveMessage {
    int state;

    public MoveMessage(FriendlyByteBuf packetBuffer) {
        state=packetBuffer.readInt();
    }
    public MoveMessage(int a)
    {
        state=a;
    }



    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(state);

    }

    public boolean receive(Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide().isServer()) {
            context.get().enqueueWork(() -> {
                ServerPlayer user =context.get().getSender();
                if(user.getVehicle()!=null && user.getVehicle() instanceof BlockShipEntity)
                {
                    BlockShipEntity vehicle = (BlockShipEntity) user.getVehicle();
                    int s = vehicle.getItemBySlot(EquipmentSlot.CHEST).getTag().getInt("type");
                    int move = state;
                    if(move==0)
                    {

                        Vec3 v = new Vec3(vehicle.getLookAngle().x,vehicle.getLookAngle().y,vehicle.getLookAngle().z).scale(0.8f);
                        vehicle.setDeltaMovement(v);
                    }
                    if(move==2)
                    {
                        vehicle.setYRot(vehicle.getYRot()+5);
                        vehicle.yHeadRot+=5;

                    }
                    if(move==1)
                    {
                        vehicle.setYRot(vehicle.getYRot()-5);
                        vehicle.yHeadRot-=5;
                    }
                    if(move==3 && s==1)
                    {
                        vehicle.setDeltaMovement(0,1,0);
                    }
                    if(move==4 && s==1)
                    {
                        vehicle.setDeltaMovement(0,-1,0);

                    }
                    if(move==5)
                    {
                        if(vehicle.getItemBySlot(EquipmentSlot.HEAD).getItem()== Items.STICK)
                        {
                            vehicle.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                        else
                        {
                            vehicle.setItemSlot(EquipmentSlot.HEAD,new ItemStack(Items.STICK));
                        }

                    }
                }
            });

            return true;
        }
        return false;
    }
}
