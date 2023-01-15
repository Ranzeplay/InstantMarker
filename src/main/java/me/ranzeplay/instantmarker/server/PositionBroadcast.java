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
    public static void Broadcast(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf buf) {
        var currentWorldPlayers = server.getPlayerManager().getPlayerList();
        // Remove players not in this world
        currentWorldPlayers.removeIf(p -> p.getWorld() != sender.getWorld());

        var packet = new BlockBroadcastPacket(sender.getDisplayName().getString(), buf.readBlockPos()).toPacketByteBuf();

        currentWorldPlayers.forEach((player) -> {
            sender.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 0.5f, 0);

            ServerPlayNetworking.send(player, BROADCAST_LOCATION_ID, packet);
        });
    }
}
