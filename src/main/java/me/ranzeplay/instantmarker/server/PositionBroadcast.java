package me.ranzeplay.instantmarker.server;

import me.ranzeplay.instantmarker.BlockBroadcastPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import static me.ranzeplay.instantmarker.InstantMarker.BROADCAST_LOCATION_ID;

public class PositionBroadcast {
    public static void Broadcast(MinecraftServer minecraftServer, ServerPlayerEntity sender, PacketByteBuf packetByteBuf) {
        var currentWorldPlayers = minecraftServer.getPlayerManager().getPlayerList();
        // Remove players not in this world
        currentWorldPlayers.removeIf(p -> p.getWorld() != sender.getWorld());

        var packet = new BlockBroadcastPacket(sender.getDisplayName().getString(), packetByteBuf.readBlockPos()).toPacketByteBuf();

        currentWorldPlayers.forEach((player) -> {
            sender.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS);

            ServerPlayNetworking.send(player, BROADCAST_LOCATION_ID, packet);
        });
    }
}
