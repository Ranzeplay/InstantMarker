package me.ranzeplay.instantmarker.server;

import com.google.gson.Gson;
import me.ranzeplay.instantmarker.InstantMarker;
import me.ranzeplay.instantmarker.models.BlockBroadcastPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import static me.ranzeplay.instantmarker.InstantMarker.BROADCAST_LOCATION_ID;

public class PositionBroadcast {
    public static void Broadcast(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf buf) {
        var currentWorldPlayers = server.getPlayerManager().getPlayerList();
        // Remove players not in this world
        currentWorldPlayers.removeIf(p -> p.getWorld() != sender.getWorld());

        var originalBuffer = new Gson().fromJson(buf.readString(), BlockBroadcastPacket.class);

        currentWorldPlayers.forEach((player) -> {
            InstantMarker.LOGGER.debug(player.getName().getString() + " has just broadcast his/her location");
            ServerPlayNetworking.send(player, BROADCAST_LOCATION_ID, originalBuffer.toPacketByteBuf());
        });
    }
}
