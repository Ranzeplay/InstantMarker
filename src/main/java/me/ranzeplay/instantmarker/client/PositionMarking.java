package me.ranzeplay.instantmarker.client;

import me.ranzeplay.instantmarker.InstantMarker;
import me.ranzeplay.instantmarker.LocalizationManager;
import me.ranzeplay.instantmarker.models.BlockBroadcastPacket;
import me.ranzeplay.instantmarker.models.BroadcastBlockPos;
import me.ranzeplay.instantmarker.models.BroadcastItem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.Identifier;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;

public class PositionMarking {
    public static void MarkPointedPosition() {
        var player = MinecraftClient.getInstance().player;
        assert player != null;

        HitResult hit = player.raycast(800, 0, true);

        if (hit.getType() == HitResult.Type.BLOCK) {
            var blockPos = ((BlockHitResult) hit).getBlockPos();
            var block = player.getWorld().getBlockState(blockPos).getBlock();

            player.sendMessage(LocalizationManager.SelfMarkBlock(block, blockPos));

            MarkPosition(blockPos, InstantMarker.SUGGEST_LOCATION_ID);
        } else if (hit.getType() == HitResult.Type.ENTITY) {
            var entity = ((EntityHitResult) hit).getEntity();

            player.sendMessage(LocalizationManager.SelfMarkEntity(entity));

            MarkPosition(entity.getBlockPos(), InstantMarker.SUGGEST_LOCATION_ID);
        } else {
            player.sendMessage(Text.translatable("chat.instantmarker.mark_too_far").formatted(Formatting.RED));
        }
    }

    public static void MarkPlayerPosition() {
        var player = MinecraftClient.getInstance().player;
        assert player != null;

        MarkPosition(player.getBlockPos(), InstantMarker.SUGGEST_PLAYER_ID);
    }

    public static void MarkPosition(BlockPos blockPos, Identifier id) {
        var player = MinecraftClient.getInstance().player;
        assert player != null;

        // Get nearby items
        var nearbyItems = player.getWorld().getEntitiesByClass(ItemEntity.class, Box.of(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), 5, 3, 5), itemEntity -> true);
        ArrayList<BroadcastItem> transformedNearbyItems = new ArrayList<>();
        // Add nearby items when it enabled
        if (InstantMarkerClient.config.shareItems) {
            for (var item : nearbyItems) {
                transformedNearbyItems.add(new BroadcastItem(item.getStack().getTranslationKey(), item.getStack().getCount()));
            }
        }

        var biome = player.getWorld().getBiome(player.getBlockPos()).getKey();
        String biomeKey = "";
        if (biome.isPresent() && InstantMarkerClient.config.shareBiome) {
            biomeKey = "biome." + biome.get().getValue().toTranslationKey();
        }

        var dimension = player.getWorld().getDimensionEntry().getKey();
        String dimensionKey = "";
        if (dimension.isPresent()) {
            dimensionKey = dimension.get().getValue().toTranslationKey();
        }

        var packet = new BlockBroadcastPacket(player.getDisplayName().getString(), new BroadcastBlockPos(blockPos), transformedNearbyItems, biomeKey, dimensionKey);
        var json = packet.toJsonString();
        // InstantMarker.LOGGER.debug(json);
        if (InstantMarkerClient.config.localMode) {
            // Send internally when local mode enabled
            ReceiveMarker(MinecraftClient.getInstance(), PacketByteBufs.create().writeText(Text.of(json)));
        } else {
            var duration = Duration.between(InstantMarkerClient.LastMarkingTime, Instant.now());
            if (duration.toMillis() > 50) {
                ClientPlayNetworking.send(id, PacketByteBufs.create().writeString(json));
                InstantMarkerClient.LastMarkingTime = Instant.now();
            }
        }
    }

    public static void ReceiveMarker(MinecraftClient client, PacketByteBuf buf) {
        var packetContent = BlockBroadcastPacket.fromPacketByteBuf(buf);

        var player = client.player;
        assert player != null;
        if (!InstantMarkerClient.mutedPlayers.contains(player.getName().getString())) {
            player.sendMessage(packetContent.markerText(client.player.getPos()), true);

            // Play sound if player allows
            if (InstantMarkerClient.config.enableSound) {
                client.worldRenderer.playSong(SoundEvents.ENTITY_ARROW_HIT_PLAYER, client.player.getBlockPos().up(2));
            }

            // Show nearby items
            var nearbyItems = packetContent.getNearbyItems();
            if (!nearbyItems.isEmpty()) {
                player.sendMessage(Text.translatable("chat.instantmarker.nearby_items").formatted(Formatting.BOLD, Formatting.AQUA));
                for (var item : nearbyItems) {
                    var translatedBlockName = Text.translatable(item.getTranslationKey());
                    player.sendMessage(translatedBlockName.append(" x").append(String.valueOf(item.getCount())));
                }
            }

            // Show biome if present
            if (!packetContent.getBiomeKey().equals("")) {
                var biome = Text.translatable(packetContent.getBiomeKey());
                player.sendMessage(Text.translatable("chat.instantmarker.biome").formatted(Formatting.BOLD, Formatting.AQUA));
                player.sendMessage(biome);
            }

            // Save marker
            InstantMarkerClient.existingMarkers.add(packetContent);

            // Limit max markers
            while (InstantMarkerClient.existingMarkers.size() > 3) {
                InstantMarkerClient.existingMarkers.remove(0);
            }

            // Remove duplicated
            InstantMarkerClient.existingMarkers = new ArrayList<>(new HashSet<>(InstantMarkerClient.existingMarkers));
        }
    }

    public static void ReceivePlayerLocation(MinecraftClient client, PacketByteBuf buf) {
        var packetContent = BlockBroadcastPacket.fromPacketByteBuf(buf);

        var player = client.player;
        assert player != null;
        if (!InstantMarkerClient.mutedPlayers.contains(player.getName().getString())) {
            var text = packetContent.locationText(client.player.getPos());
            player.sendMessage(text, true);
            player.sendMessage(text, false);

            // Play sound if player allows
            if (InstantMarkerClient.config.enableSound) {
                client.worldRenderer.playSong(SoundEvents.ENTITY_ARROW_HIT_PLAYER, client.player.getBlockPos().up(2));
            }

            // Save marker
            InstantMarkerClient.existingMarkers.add(packetContent);

            // Limit max markers
            while (InstantMarkerClient.existingMarkers.size() > 3) {
                InstantMarkerClient.existingMarkers.remove(0);
            }

            // Remove duplicated
            InstantMarkerClient.existingMarkers = new ArrayList<>(new HashSet<>(InstantMarkerClient.existingMarkers));
        }
    }
}
