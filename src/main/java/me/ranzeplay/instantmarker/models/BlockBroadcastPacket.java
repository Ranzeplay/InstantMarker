package me.ranzeplay.instantmarker.models;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class BlockBroadcastPacket extends AbstractLocationPayload implements CustomPayload {
    public static final PacketCodec<RegistryByteBuf, BlockBroadcastPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, BlockBroadcastPacket::getPlayerName,
            BlockPos.PACKET_CODEC, BlockBroadcastPacket::getTargetPosition,
            PacketCodecs.collection(ArrayList::new, BroadcastItem.CODEC), BlockBroadcastPacket::getNearbyItems,
            PacketCodecs.STRING, BlockBroadcastPacket::getBiomeKey,
            PacketCodecs.STRING, BlockBroadcastPacket::getDimensionKey,
            BlockBroadcastPacket::new);


    public BlockBroadcastPacket(String playerName, BlockPos targetPosition, List<BroadcastItem> nearbyItems, String biomeKey, String dimensionKey) {
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
