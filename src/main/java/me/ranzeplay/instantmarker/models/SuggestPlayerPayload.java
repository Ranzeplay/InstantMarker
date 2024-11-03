package me.ranzeplay.instantmarker.models;

import me.ranzeplay.instantmarker.InstantMarker;
import net.minecraft.network.packet.CustomPayload;

public class SuggestPlayerPayload implements CustomPayload {
    public static final CustomPayload.Id<SuggestPlayerPayload> ID = new Id<>(InstantMarker.SUGGEST_PLAYER_ID);

    public String uuid;

    public boolean withNearbyItems;
    public boolean withBiome;

    public SuggestPlayerPayload(String uuid, boolean withNearbyItems, boolean withBiome) {
        this.uuid = uuid;
        this.withNearbyItems = withNearbyItems;
        this.withBiome = withBiome;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isWithNearbyItems() {
        return withNearbyItems;
    }

    public boolean isWithBiome() {
        return withBiome;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
