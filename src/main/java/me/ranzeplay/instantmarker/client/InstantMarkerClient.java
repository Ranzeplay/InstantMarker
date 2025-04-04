package me.ranzeplay.instantmarker.client;

import me.ranzeplay.instantmarker.models.*;
import me.ranzeplay.instantmarker.models.IMConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.time.Instant;
import java.util.HashSet;
import java.util.Vector;

public class InstantMarkerClient implements ClientModInitializer {
    private static KeyBinding keyBinding;

    public static Vector<BlockBroadcastPacket> existingMarkers = new Vector<>();
    public static HashSet<String> mutedPlayers = new HashSet<>();

    public static Instant LastMarkingTime = Instant.now();

    public final static IMConfig savedConfig = IMConfig.createAndLoad();

    public static IMConfigModel config;

    @Override
    public void onInitializeClient() {
        config = IMConfigModel.load(savedConfig);

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.instantmarker.mark",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                "category.instantmarker.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                PositionMarking.markPointedPosition();
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(BroadcastLocationPayload.ID, (payload, context)
                -> PositionMarking.receiveMarker(context.client(), payload));
        ClientPlayNetworking.registerGlobalReceiver(BroadcastPlayerPayload.ID, (payload, context)
                -> PositionMarking.receivePlayerLocation(context.client(), payload));

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            // Reset status
            existingMarkers.clear();
            mutedPlayers.clear();
            config = IMConfigModel.load(savedConfig);
        });
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> ClientCommand.Register(dispatcher));

        HudRenderingManager.registerEvents();
    }
}
