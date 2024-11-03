package me.ranzeplay.instantmarker.models;

import me.ranzeplay.instantmarker.InstantMarker;
import net.minecraft.network.packet.CustomPayload;

import java.util.List;

public class BroadcastPlayerPayload extends AbstractLocationPayload implements CustomPayload {
    public static final Id<BroadcastPlayerPayload> ID = new Id<>(InstantMarker.BROADCAST_PLAYER_LOCATION_ID);

    public BroadcastPlayerPayload(String playerName, BroadcastBlockPos targetPosition, List<BroadcastItem> nearbyItems, String biomeKey, String dimensionKey) {
        super(playerName, targetPosition, nearbyItems, biomeKey, dimensionKey);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
