package me.ranzeplay.instantmarker.models;

import me.ranzeplay.instantmarker.InstantMarker;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class BroadcastLocationPayload extends AbstractLocationPayload implements CustomPayload {
    public static final CustomPayload.Id<BroadcastLocationPayload> ID = new Id<>(InstantMarker.BROADCAST_MARKER_ID);
    public static final PacketCodec<RegistryByteBuf, BroadcastLocationPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, BroadcastLocationPayload::getPlayerName,
            BlockPos.PACKET_CODEC, BroadcastLocationPayload::getTargetPosition,
            PacketCodecs.collection(ArrayList::new, BroadcastItem.CODEC), BroadcastLocationPayload::getNearbyItems,
            PacketCodecs.STRING, BroadcastLocationPayload::getBiomeKey,
            PacketCodecs.STRING, BroadcastLocationPayload::getDimensionKey,
            BroadcastLocationPayload::new);

    public BroadcastLocationPayload(String playerName, BlockPos targetPosition, List<BroadcastItem> nearbyItems, String biomeKey, String dimensionKey) {
        super(playerName, targetPosition, nearbyItems, biomeKey, dimensionKey);
    }

    public BroadcastLocationPayload(SuggestLocationPayload payload) {
        super(payload.getPlayerName(), payload.getTargetPosition(), payload.getNearbyItems(), payload.getBiomeKey(), payload.getDimensionKey());
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
