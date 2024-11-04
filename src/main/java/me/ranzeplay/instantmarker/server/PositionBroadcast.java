package me.ranzeplay.instantmarker.server;

import me.ranzeplay.instantmarker.InstantMarker;
import me.ranzeplay.instantmarker.models.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.UUID;

@Environment(EnvType.SERVER)
public class PositionBroadcast {
    public static void BroadcastLocation(MinecraftServer server, ServerPlayerEntity sender, SuggestLocationPayload payload) {
        var currentWorldPlayers = server.getPlayerManager().getPlayerList();
        // Remove players not in this world
        currentWorldPlayers.removeIf(p -> p.getWorld() != sender.getWorld());

        var broadcastPayload = new BroadcastLocationPayload(payload);
        currentWorldPlayers.forEach((player) -> {
            InstantMarker.LOGGER.debug("{} has just broadcast a position marker", sender.getName().getString());
            ServerPlayNetworking.send(player, broadcastPayload);
        });
    }

    public static void BroadcastPlayer(MinecraftServer server, ServerPlayerEntity sender, SuggestPlayerPayload payload) {
        var targetPlayer = server.getPlayerManager().getPlayer(UUID.fromString(payload.uuid));

        assert targetPlayer != null;
        var blockPos = targetPlayer.getBlockPos();
        // Get nearby items
        var nearbyItems = targetPlayer.getWorld().getEntitiesByClass(ItemEntity.class, Box.of(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), 5, 3, 5), itemEntity -> true);
        ArrayList<BroadcastItem> transformedNearbyItems = new ArrayList<>();
        // Add nearby items when it enabled
        if (payload.isWithNearbyItems()) {
            for (var item : nearbyItems) {
                transformedNearbyItems.add(new BroadcastItem(item.getStack().getTranslationKey(), item.getStack().getCount()));
            }
        }

        var biome = targetPlayer.getWorld().getBiome(targetPlayer.getBlockPos()).getKey();
        String biomeKey = "";
        if (biome.isPresent() && payload.isWithBiome()) {
            biomeKey = "biome." + biome.get().getValue().toTranslationKey();
        }

        var dimension = targetPlayer.getWorld().getDimensionEntry().getKey();
        String dimensionKey = "";
        if (dimension.isPresent()) {
            dimensionKey = dimension.get().getValue().toTranslationKey();
        }

        var broadcastPayload = new BroadcastPlayerPayload(targetPlayer.getDisplayName().getString(),blockPos, transformedNearbyItems, biomeKey, dimensionKey);
        if (sender.isPlayer()) {
            sender.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 10));

            var currentWorldPlayers = server.getPlayerManager().getPlayerList();
            currentWorldPlayers.forEach((player) -> {
                InstantMarker.LOGGER.debug("{} has just broadcast his/her location", sender.getName().getString());
                ServerPlayNetworking.send(player, broadcastPayload);
            });
        } else {
            InstantMarker.LOGGER.warn("Received a non-player player marking request");
        }
    }
}
