package me.ranzeplay.instantmarker.models;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class BlockBroadcastPacket {
    private String playerName;
    private BroadcastBlockPos targetPosition;
    private List<BroadcastItem> nearbyItems;

    public PacketByteBuf toPacketByteBuf() {
        var buffer = PacketByteBufs.create();
        buffer.writeText(Text.literal(this.toJsonString()));
        return buffer;
    }

    public double getDistance(Vec3d source) {
        return new Vec3d(targetPosition.getX(), targetPosition.getY(), targetPosition.getZ()).distanceTo(source);
    }

    public static BlockBroadcastPacket fromPacketByteBuf(PacketByteBuf buffer) {
        var text = buffer.readText().getString();
        return fromJsonString(text);
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

    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public BlockBroadcastPacket(String playerName, BroadcastBlockPos targetPosition, ArrayList<BroadcastItem> nearbyItems) {
        this.playerName = playerName;
        this.targetPosition = targetPosition;
        this.nearbyItems = nearbyItems;
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

    public static BlockBroadcastPacket fromJsonString(String json) {
        return new Gson().fromJson(json, BlockBroadcastPacket.class);
    }
}
