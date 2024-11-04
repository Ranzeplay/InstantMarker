package me.ranzeplay.instantmarker.models;

import me.ranzeplay.instantmarker.InstantMarker;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class BroadcastPlayerPayload extends AbstractLocationPayload implements CustomPayload {
    public static final Id<BroadcastPlayerPayload> ID = new Id<>(InstantMarker.BROADCAST_PLAYER_LOCATION_ID);
    public static final PacketCodec<RegistryByteBuf, BroadcastPlayerPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, BroadcastPlayerPayload::getPlayerName,
            BlockPos.PACKET_CODEC, BroadcastPlayerPayload::getTargetPosition,
            PacketCodecs.collection(ArrayList::new, BroadcastItem.CODEC), BroadcastPlayerPayload::getNearbyItems,
            PacketCodecs.STRING, BroadcastPlayerPayload::getBiomeKey,
            PacketCodecs.STRING, BroadcastPlayerPayload::getDimensionKey,
            BroadcastPlayerPayload::new);

    public BroadcastPlayerPayload(String playerName, BlockPos targetPosition, List<BroadcastItem> nearbyItems, String biomeKey, String dimensionKey) {
        super(playerName, targetPosition, nearbyItems, biomeKey, dimensionKey);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
