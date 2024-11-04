package me.ranzeplay.instantmarker.models;

import me.ranzeplay.instantmarker.InstantMarker;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class SuggestLocationPayload extends AbstractLocationPayload implements CustomPayload {
    public static final CustomPayload.Id<SuggestLocationPayload> ID = new Id<>(InstantMarker.SUGGEST_LOCATION_ID);
    public static final PacketCodec<RegistryByteBuf, SuggestLocationPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SuggestLocationPayload::getPlayerName,
            BlockPos.PACKET_CODEC, SuggestLocationPayload::getTargetPosition,
            PacketCodecs.collection(ArrayList::new, BroadcastItem.CODEC), SuggestLocationPayload::getNearbyItems,
            PacketCodecs.STRING, SuggestLocationPayload::getBiomeKey,
            PacketCodecs.STRING, SuggestLocationPayload::getDimensionKey,
            SuggestLocationPayload::new);

    public SuggestLocationPayload(String playerName, BlockPos targetPosition, List<BroadcastItem> nearbyItems, String biomeKey, String dimensionKey) {
        super(playerName, targetPosition, nearbyItems, biomeKey, dimensionKey);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
