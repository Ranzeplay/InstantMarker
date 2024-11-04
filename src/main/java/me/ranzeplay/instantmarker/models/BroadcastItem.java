package me.ranzeplay.instantmarker.models;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class BroadcastItem {
    public static final PacketCodec<RegistryByteBuf, BroadcastItem> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, BroadcastItem::getTranslationKey,
            PacketCodecs.INTEGER, BroadcastItem::getCount,
            BroadcastItem::new);

    private final String translationKey;
    private final int count;

    public BroadcastItem(String translationKey, int count) {
        this.translationKey = translationKey;
        this.count = count;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public int getCount() {
        return count;
    }
}
