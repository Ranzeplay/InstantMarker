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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashSet;

public class PositionMarking {
    public static void MarkPosition() {
        var player = MinecraftClient.getInstance().player;
        assert player != null;

        HitResult hit = player.raycast(800, 0, true);

        if (hit.getType() == HitResult.Type.BLOCK) {
            var blockPos = ((BlockHitResult) hit).getBlockPos();
            var block = player.getWorld().getBlockState(blockPos).getBlock();

            player.sendMessage(LocalizationManager.SelfMarkBlock(block, blockPos));

            // Get nearby items
            var nearbyItems = player.getWorld().getEntitiesByClass(ItemEntity.class, Box.of(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), 5, 3, 5), itemEntity -> true);
            ArrayList<BroadcastItem> transformedNearbyItems = new ArrayList<>();
            for (var item : nearbyItems) {
                transformedNearbyItems.add(new BroadcastItem(item.getStack().getTranslationKey(), item.getStack().getCount()));
            }

            var packet = new BlockBroadcastPacket(player.getDisplayName().getString(), new BroadcastBlockPos(blockPos), transformedNearbyItems);
            var json = packet.toJsonString();
            // InstantMarker.LOGGER.debug(json);
            if (InstantMarkerClient.localMode) {
                // Send internally when local mode enabled
                ReceiveMarker(MinecraftClient.getInstance(), PacketByteBufs.create().writeText(Text.of(json)));
            } else {
                ClientPlayNetworking.send(InstantMarker.SUGGEST_LOCATION_ID, PacketByteBufs.create().writeString(json));
            }
        } else if (hit.getType() == HitResult.Type.ENTITY) {
            var entity = ((EntityHitResult) hit).getEntity();

            player.sendMessage(LocalizationManager.SelfMarkEntity(entity));
        } else {
            player.sendMessage(Text.translatable("chat.instantmarker.mark_too_far").formatted(Formatting.RED));
        }
    }

    public static void ReceiveMarker(MinecraftClient client, PacketByteBuf buf) {
        var packetContent = BlockBroadcastPacket.fromPacketByteBuf(buf);

        var player = client.player;
        assert player != null;
        if (!InstantMarkerClient.mutedPlayers.contains(player.getName().getString())) {
            player.sendMessage(packetContent.fullText(client.player.getPos()), true);
            client.worldRenderer.playSong(SoundEvents.ENTITY_ARROW_HIT_PLAYER, client.player.getBlockPos().up(5));

            // Show nearby items
            var nearbyItems = packetContent.getNearbyItems();
            if (!nearbyItems.isEmpty()) {
                player.sendMessage(Text.translatable("chat.instantmarker.nearby_items").formatted(Formatting.AQUA));
                for (var item : nearbyItems) {
                    var translatedBlockName = Text.translatable(item.getTranslationKey());
                    player.sendMessage(translatedBlockName.append(" x").append(String.valueOf(item.getCount())));
                }
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
