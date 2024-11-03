package me.ranzeplay.instantmarker.models;

import net.minecraft.network.packet.CustomPayload;

import java.util.List;

public class BlockBroadcastPacket extends AbstractLocationPayload implements CustomPayload {

    public BlockBroadcastPacket(String playerName, BroadcastBlockPos targetPosition, List<BroadcastItem> nearbyItems, String biomeKey, String dimensionKey) {
        super(playerName, targetPosition, nearbyItems, biomeKey, dimensionKey);
    }

    public BlockBroadcastPacket(BroadcastLocationPayload payload) {
        super(payload.getPlayerName(), payload.getTargetPosition(), payload.getNearbyItems(), payload.getBiomeKey(), payload.getDimensionKey());
    }

    public BlockBroadcastPacket(BroadcastPlayerPayload payload) {
        super(payload.getPlayerName(), payload.getTargetPosition(), payload.getNearbyItems(), payload.getBiomeKey(), payload.getDimensionKey());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return null;
    }
}
