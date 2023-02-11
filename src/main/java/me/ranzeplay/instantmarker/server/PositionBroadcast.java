package me.ranzeplay.instantmarker.server;

import com.google.gson.Gson;
import me.ranzeplay.instantmarker.InstantMarker;
import me.ranzeplay.instantmarker.models.BlockBroadcastPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import static me.ranzeplay.instantmarker.InstantMarker.BROADCAST_MARKER_ID;
import static me.ranzeplay.instantmarker.InstantMarker.BROADCAST_PLAYER_LOCATION_ID;

public class PositionBroadcast {
    public static void BroadcastLocation(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf buf) {
        var currentWorldPlayers = server.getPlayerManager().getPlayerList();
        // Remove players not in this world
        currentWorldPlayers.removeIf(p -> p.getWorld() != sender.getWorld());

        var originalBuffer = new Gson().fromJson(buf.readString(), BlockBroadcastPacket.class);

        currentWorldPlayers.forEach((player) -> {
            InstantMarker.LOGGER.debug(sender.getName().getString() + " has just broadcast a position marker");
            ServerPlayNetworking.send(player, BROADCAST_MARKER_ID, originalBuffer.toPacketByteBuf());
        });
    }

    public static void BroadcastPlayer(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf buf) {
        if (sender.isPlayer()) {
            sender.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 10));

            var originalBuffer = new Gson().fromJson(buf.readString(), BlockBroadcastPacket.class);

            var currentWorldPlayers = server.getPlayerManager().getPlayerList();
            currentWorldPlayers.forEach((player) -> {
                InstantMarker.LOGGER.debug(sender.getName().getString() + " has just broadcast his/her location");
                ServerPlayNetworking.send(player, BROADCAST_PLAYER_LOCATION_ID, originalBuffer.toPacketByteBuf());
            });
        } else {
            InstantMarker.LOGGER.warn("Received a non-player player marking request");
        }
    }
}
