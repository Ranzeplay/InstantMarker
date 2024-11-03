package me.ranzeplay.instantmarker.client;

import me.ranzeplay.instantmarker.models.*;
import me.ranzeplay.instantmarker.models.IMConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;

public class InstantMarkerClient implements ClientModInitializer {
    private static KeyBinding keyBinding;

    public static ArrayList<BlockBroadcastPacket> existingMarkers = new ArrayList<>();
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
                PositionMarking.MarkPointedPosition();
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(BroadcastLocationPayload.ID, (payload, context)
                -> PositionMarking.ReceiveMarker(context.client(), payload));
        ClientPlayNetworking.registerGlobalReceiver(BroadcastPlayerPayload.ID, (payload, context)
                -> PositionMarking.ReceivePlayerLocation(context.client(), payload));

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            // Reset status
            existingMarkers.clear();
            mutedPlayers.clear();
            config = IMConfigModel.load(savedConfig);
        });
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> ClientCommand.Register(dispatcher));

        HudRenderCallback.EVENT.register((drawContext, renderTickCounter) -> {
            var client = MinecraftClient.getInstance();
            var textRenderer = client.textRenderer;
            final float lineHeight = textRenderer.fontHeight + 0.7f;
            float y = client.getWindow().getScaledHeight() - lineHeight;

            for (var marker : InstantMarkerClient.existingMarkers) {
                drawContext.drawText(textRenderer, marker.shortText(client.player.getPos()), 3, (int)y, RGB2Int((short) 255, (short) 255, (short) 255), true);
            }
        });
    }

    private static int RGB2Int(short r, short g, short b) {
        return ((0xFF << 24) | (r << 16) | (g << 8) | b);
    }
}
