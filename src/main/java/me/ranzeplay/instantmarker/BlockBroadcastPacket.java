package me.ranzeplay.instantmarker;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public record BlockBroadcastPacket(String playerName, BlockPos targetPosition) {

    public PacketByteBuf toPacketByteBuf() {
        var buffer = PacketByteBufs.create();
        buffer.writeText(Text.of(playerName + "," + targetPosition.getX() + "," + targetPosition.getY() + "," + targetPosition.getZ()));
        return buffer;
    }

    public static BlockBroadcastPacket fromPacketByteBuf(PacketByteBuf buffer) {
        var text = buffer.readText();
        var comp = text.getString().split(",");

        return new BlockBroadcastPacket(comp[0], new BlockPos(Integer.parseInt(comp[1]), Integer.parseInt(comp[2]), Integer.parseInt(comp[3])));
    }
}
