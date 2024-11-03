package me.ranzeplay.instantmarker.server;

import com.google.gson.Gson;
import me.ranzeplay.instantmarker.InstantMarker;
import me.ranzeplay.instantmarker.client.InstantMarkerClient;
import me.ranzeplay.instantmarker.models.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.UUID;

import static me.ranzeplay.instantmarker.InstantMarker.BROADCAST_MARKER_ID;
import static me.ranzeplay.instantmarker.InstantMarker.BROADCAST_PLAYER_LOCATION_ID;

public class PositionBroadcast {
    public static void BroadcastLocation(MinecraftServer server, ServerPlayerEntity sender, SuggestLocationPayload payload) {
        var currentWorldPlayers = server.getPlayerManager().getPlayerList();
        // Remove players not in this world
        currentWorldPlayers.removeIf(p -> p.getWorld() != sender.getWorld());

        var broadcastPayload = new BroadcastLocationPayload(payload);
        currentWorldPlayers.forEach((player) -> {
            InstantMarker.LOGGER.debug(sender.getName().getString() + " has just broadcast a position marker");
            ServerPlayNetworking.send(player, broadcastPayload);
        });
    }

    public static void BroadcastPlayer(MinecraftServer server, ServerPlayerEntity sender, SuggestPlayerPayload payload) {
        var targetPlayer = server.getPlayerManager().getPlayer(UUID.fromString(payload.uuid));

        var blockPos = targetPlayer.getBlockPos();
        // Get nearby items
        var nearbyItems = targetPlayer.getWorld().getEntitiesByClass(ItemEntity.class, Box.of(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), 5, 3, 5), itemEntity -> true);
        ArrayList<BroadcastItem> transformedNearbyItems = new ArrayList<>();
        // Add nearby items when it enabled
        if (InstantMarkerClient.config.shareItems) {
            for (var item : nearbyItems) {
                transformedNearbyItems.add(new BroadcastItem(item.getStack().getTranslationKey(), item.getStack().getCount()));
            }
        }

        var biome = targetPlayer.getWorld().getBiome(targetPlayer.getBlockPos()).getKey();
        String biomeKey = "";
        if (biome.isPresent() && InstantMarkerClient.config.shareBiome) {
            biomeKey = "biome." + biome.get().getValue().toTranslationKey();
        }

        var dimension = targetPlayer.getWorld().getDimensionEntry().getKey();
        String dimensionKey = "";
        if (dimension.isPresent()) {
            dimensionKey = dimension.get().getValue().toTranslationKey();
        }

        var broadcastPayload = new BroadcastPlayerPayload(targetPlayer.getDisplayName().getString(), new BroadcastBlockPos(blockPos), transformedNearbyItems, biomeKey, dimensionKey);
        if (sender.isPlayer()) {
            sender.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 10));

            var currentWorldPlayers = server.getPlayerManager().getPlayerList();
            currentWorldPlayers.forEach((player) -> {
                InstantMarker.LOGGER.debug(sender.getName().getString() + " has just broadcast his/her location");
                ServerPlayNetworking.send(player, broadcastPayload);
            });
        } else {
            InstantMarker.LOGGER.warn("Received a non-player player marking request");
        }
    }
}
