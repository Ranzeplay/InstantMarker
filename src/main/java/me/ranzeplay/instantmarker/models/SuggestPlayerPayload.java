package me.ranzeplay.instantmarker.models;

import me.ranzeplay.instantmarker.InstantMarker;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public class SuggestPlayerPayload implements CustomPayload {
    public static final CustomPayload.Id<SuggestPlayerPayload> ID = new Id<>(InstantMarker.SUGGEST_PLAYER_ID);
    public static final PacketCodec<RegistryByteBuf, SuggestPlayerPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SuggestPlayerPayload::getUuid,
            PacketCodecs.BOOL, SuggestPlayerPayload::isWithNearbyItems,
            PacketCodecs.BOOL, SuggestPlayerPayload::isWithBiome,
            SuggestPlayerPayload::new);

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
