package com.nyfaria.nyfsairships;

import com.mojang.blaze3d.platform.InputConstants;
import com.nyfaria.nyfsairships.entity.BlockShipEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.KeyMapping;
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
            while (client.options.keyUp.consumeClick()) {
                FriendlyByteBuf forw = PacketByteBufs.create();
                forw.writeInt(0);
                ClientPlayNetworking.send(NyfsAirships.forwardPacket,forw);
            }

        });
        ClientTickEvents.START_CLIENT_TICK.register(client ->
        {
            while (client.options.keyLeft.consumeClick()) {
                FriendlyByteBuf forw = PacketByteBufs.create();
                forw.writeInt(1);
                ClientPlayNetworking.send(NyfsAirships.forwardPacket,forw);
            }

        });
        ClientTickEvents.START_CLIENT_TICK.register(client ->
        {
            while (client.options.keyRight.consumeClick()) {
                FriendlyByteBuf forw = PacketByteBufs.create();
                forw.writeInt(2);
                ClientPlayNetworking.send(NyfsAirships.forwardPacket,forw);
            }

        });
        ClientTickEvents.START_CLIENT_TICK.register(client ->
        {
            while (up.consumeClick()) {
                FriendlyByteBuf forw = PacketByteBufs.create();
                forw.writeInt(3);
                ClientPlayNetworking.send(NyfsAirships.forwardPacket,forw);
            }

        });
        ClientTickEvents.START_CLIENT_TICK.register(client ->
        {
            while (down.consumeClick()) {
                FriendlyByteBuf forw = PacketByteBufs.create();
                forw.writeInt(4);
                ClientPlayNetworking.send(NyfsAirships.forwardPacket,forw);
            }

        });
        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (stop.consumeClick()) {
                FriendlyByteBuf forw =PacketByteBufs.create();
                forw.writeInt(5);
                ClientPlayNetworking.send(NyfsAirships.forwardPacket,forw);
            }

        });

        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (client.options.keyUp.consumeClick()) {
                FriendlyByteBuf forw = PacketByteBufs.create();
                forw.writeInt(0);
                ClientPlayNetworking.send(NyfsAirships.forwardPacket,forw);
            }

        });
        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (client.options.keyLeft.consumeClick()) {
                FriendlyByteBuf forw = PacketByteBufs.create();
                forw.writeInt(1);
                ClientPlayNetworking.send(NyfsAirships.forwardPacket,forw);
            }

        });
        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (client.options.keyRight.consumeClick()) {
                FriendlyByteBuf forw = PacketByteBufs.create();
                forw.writeInt(2);
                ClientPlayNetworking.send(NyfsAirships.forwardPacket,forw);
            }

        });
        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (up.consumeClick()) {
                FriendlyByteBuf forw = PacketByteBufs.create();
                forw.writeInt(3);
                ClientPlayNetworking.send(NyfsAirships.forwardPacket,forw);
            }

        });
        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (down.consumeClick()) {
                FriendlyByteBuf forw = PacketByteBufs.create();
                forw.writeInt(4);
                ClientPlayNetworking.send(NyfsAirships.forwardPacket,forw);
            }

        });
        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (stop.consumeClick()) {
                FriendlyByteBuf forw =PacketByteBufs.create();
                forw.writeInt(5);
                ClientPlayNetworking.send(NyfsAirships.forwardPacket,forw);
            }

        });
    }
}
