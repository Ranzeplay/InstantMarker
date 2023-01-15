package me.ranzeplay.instantmarker;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public record BlockBroadcastPacket(String playerName, BlockPos targetPosition) {

    public PacketByteBuf toPacketByteBuf() {
        var buffer = PacketByteBufs.create();
        buffer.writeText(Text.of(playerName + "," + targetPosition.getX() + "," + targetPosition.getY() + "," + targetPosition.getZ()));
        return buffer;
    }

    public double getDistance(Vec3d source) {
        return new Vec3d(targetPosition.getX(), targetPosition.getY(), targetPosition.getZ()).distanceTo(source);
    }

    public static BlockBroadcastPacket fromPacketByteBuf(PacketByteBuf buffer) {
        var text = buffer.readText();
        var comp = text.getString().split(",");

        return new BlockBroadcastPacket(comp[0], new BlockPos(Integer.parseInt(comp[1]), Integer.parseInt(comp[2]), Integer.parseInt(comp[3])));
    }

    public Text shortText(Vec3d sourcePos) {
        var playerNameText = Text.literal(playerName).formatted(Formatting.BOLD, Formatting.YELLOW);
        var locationText = Text.literal(String.format("(%d, %d, %d)", targetPosition.getX(), targetPosition.getY(), targetPosition.getZ())).formatted(Formatting.AQUA);

        var distanceText = Text.literal(String.format("(%.1fm)", getDistance(sourcePos))).formatted(Formatting.GREEN);

        return Text.empty()
                .append(playerNameText)
                .append(" ")
                .append(locationText)
                .append(" ")
                .append(distanceText);
    }

    public Text fullText(Vec3d sourcePos) {
        var playerNameText = Text.literal(playerName).formatted(Formatting.BOLD, Formatting.YELLOW);
        var locationText = Text.literal(String.format("(%d, %d, %d)", targetPosition.getX(), targetPosition.getY(), targetPosition.getZ())).formatted(Formatting.AQUA);

        var distanceText = Text.literal(String.format("%.1fm", getDistance(sourcePos))).formatted(Formatting.GREEN);

        return Text.empty()
                .append(playerNameText)
                .append(Text.translatable("text.instantmarker.suggest_position"))
                .append(locationText)
                .append(" : ")
                .append(distanceText);
    }
}
