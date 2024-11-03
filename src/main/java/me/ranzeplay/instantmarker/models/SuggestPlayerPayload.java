package me.ranzeplay.instantmarker.models;

import me.ranzeplay.instantmarker.InstantMarker;
import net.minecraft.network.packet.CustomPayload;

public class SuggestPlayerPayload implements CustomPayload {
    public static final CustomPayload.Id<SuggestPlayerPayload> ID = new Id<>(InstantMarker.SUGGEST_PLAYER_ID);

    public String uuid;

    public SuggestPlayerPayload(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
