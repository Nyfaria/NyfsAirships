package com.nyfaria.nyfsairships;

import com.mojang.blaze3d.platform.InputConstants;
import com.nyfaria.nyfsairships.entity.BlockShipEntity;
import com.nyfaria.nyfsairships.entity.BlockShipEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import org.lwjgl.glfw.GLFW;

public class ClientInit implements ClientModInitializer {
    public static KeyMapping up;
    public static KeyMapping down;
    public static KeyMapping stop;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(NyfsAirships.BLOCK_SHIP_ENTITY_ENTITY_TYPE, BlockShipEntityRenderer::new);
        up = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.nyfsairships.up", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z,"category.nyfsairships.main"));
        down= KeyBindingHelper.registerKeyBinding(new KeyMapping("key.nyfsairships.down", InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_C,"category.nyfsairships.main"));
        stop = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.nyfsairships.stop", InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_V,"category.nyfsairships.main"));
        ClientTickEvents.START_CLIENT_TICK.register(client ->
        {
            if(client.player != null) {
                LocalPlayer player = client.player;
                if (player.isPassenger() && player.getVehicle() instanceof BlockShipEntity airShip) {
                    if (up.isDown()) {
                        airShip.setYya(0.8f);
                    } else if (down.isDown()) {
                        if (!airShip.isOnGround()) {
                            airShip.setYya(-0.8f);
                            player.input.shiftKeyDown = false;
                        }
                    } else {
                        airShip.setYya(0);
                    }

                }
            }
        });
    }
}
