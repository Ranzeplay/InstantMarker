package me.ranzeplay.instantmarker.models;

import me.ranzeplay.instantmarker.InstantMarker;
import net.minecraft.network.packet.CustomPayload;

import java.util.List;

public class BroadcastLocationPayload extends AbstractLocationPayload implements CustomPayload {
    public static final CustomPayload.Id<BroadcastLocationPayload> ID = new Id<>(InstantMarker.BROADCAST_MARKER_ID);

    public BroadcastLocationPayload(String playerName, BroadcastBlockPos targetPosition, List<BroadcastItem> nearbyItems, String biomeKey, String dimensionKey) {
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
