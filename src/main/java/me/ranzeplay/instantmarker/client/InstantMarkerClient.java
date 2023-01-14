package me.ranzeplay.instantmarker.client;

import me.ranzeplay.instantmarker.InstantMarker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class InstantMarkerClient implements ClientModInitializer {
    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.instantmarker.demokey",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                "category.instantmarker.demokey"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                PositionMarking.MarkPosition();
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(InstantMarker.BROADCAST_LOCATION_ID, (minecraftClient, clientPlayNetworkHandler, packetByteBuf, packetSender)
                -> PositionMarking.ReceiveMarker(minecraftClient, packetByteBuf));
    }
}
