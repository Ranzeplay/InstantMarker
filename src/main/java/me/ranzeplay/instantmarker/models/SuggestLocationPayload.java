package me.ranzeplay.instantmarker.models;

import me.ranzeplay.instantmarker.InstantMarker;
import net.minecraft.network.packet.CustomPayload;

import java.util.List;

public class SuggestLocationPayload implements CustomPayload {
    public static final CustomPayload.Id<SuggestLocationPayload> ID = new Id<>(InstantMarker.SUGGEST_LOCATION_ID);

    private String playerName;
    private BroadcastBlockPos targetPosition;
    private List<BroadcastItem> nearbyItems;
    private String biomeKey;
    private long broadcastTimestamp;
    private String dimensionKey;

    public SuggestLocationPayload(String playerName, BroadcastBlockPos targetPosition, List<BroadcastItem> nearbyItems, String biomeKey, String dimensionKey) {
        this.playerName = playerName;
        this.targetPosition = targetPosition;
        this.nearbyItems = nearbyItems;
        this.biomeKey = biomeKey;
        this.broadcastTimestamp = System.currentTimeMillis();
        this.dimensionKey = dimensionKey;
    }

    public String getPlayerName() {
        return playerName;
    }

    public BroadcastBlockPos getTargetPosition() {
        return targetPosition;
    }

    public List<BroadcastItem> getNearbyItems() {
        return nearbyItems;
    }

    public String getBiomeKey() {
        return biomeKey;
    }

    public long getBroadcastTimestamp() {
        return broadcastTimestamp;
    }

    public String getDimensionKey() {
        return dimensionKey;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
