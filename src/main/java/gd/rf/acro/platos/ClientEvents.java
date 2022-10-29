package gd.rf.acro.platos;

import gd.rf.acro.platos.network.MoveMessage;
import gd.rf.acro.platos.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlatosTransporters.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    @SubscribeEvent
    public static void onEvent(TickEvent.ClientTickEvent event)
    {



        while (PlatosTransporters.SHIP_UP.consumeClick())
        {

            NetworkHandler.INSTANCE.sendToServer(new MoveMessage(3));
        }
        while (PlatosTransporters.SHIP_DOWN.consumeClick())
        {
            NetworkHandler.INSTANCE.sendToServer(new MoveMessage(4));
        }
        while (PlatosTransporters.SHIP_STOP.consumeClick())
        {
            NetworkHandler.INSTANCE.sendToServer(new MoveMessage(5));
        }



        while (Minecraft.getInstance().options.keyUp.consumeClick())
        {
            NetworkHandler.INSTANCE.sendToServer(new MoveMessage(0));
        }
        while (Minecraft.getInstance().options.keyLeft.consumeClick())
        {
            NetworkHandler.INSTANCE.sendToServer(new MoveMessage(1));
        }
        while (Minecraft.getInstance().options.keyRight.consumeClick())
        {
            NetworkHandler.INSTANCE.sendToServer(new MoveMessage(2));


        }






    }


}
