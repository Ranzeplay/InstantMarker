package me.ranzeplay.instantmarker.client;

import me.ranzeplay.instantmarker.BlockBroadcastPacket;
import me.ranzeplay.instantmarker.InstantMarker;
import me.ranzeplay.instantmarker.Localization;
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

            player.sendMessage(Localization.SelfMarkBlock(block, blockPos));

            var nearbyItems = player.getWorld().getEntitiesByClass(ItemEntity.class, Box.of(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), 5, 3, 5), itemEntity -> true);
            if (!nearbyItems.isEmpty()) {
                player.sendMessage(Text.translatable("chat.instantmarker.nearby_items").formatted(Formatting.AQUA));
                for(var item : nearbyItems) {
                    player.sendMessage(item.getDisplayName().copy().append(" x").append(String.valueOf(item.getStack().getCount())));
                }
            }

            ClientPlayNetworking.send(InstantMarker.SUGGEST_LOCATION_ID, PacketByteBufs.create().writeBlockPos(blockPos));
        } else if (hit.getType() == HitResult.Type.ENTITY) {
            var entity = ((EntityHitResult) hit).getEntity();

            player.sendMessage(Localization.SelfMarkEntity(entity));
        } else {
            player.sendMessage(Text.translatable("chat.instantmarker.mark_too_far").formatted(Formatting.RED));
        }
    }

    public static void ReceiveMarker(MinecraftClient client, PacketByteBuf buf) {
        var packetContent = BlockBroadcastPacket.fromPacketByteBuf(buf);

        assert client.player != null;
        client.player.sendMessage(packetContent.fullText(client.player.getPos()), true);
        client.worldRenderer.playSong(SoundEvents.ENTITY_ARROW_HIT_PLAYER, client.player.getBlockPos());

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
