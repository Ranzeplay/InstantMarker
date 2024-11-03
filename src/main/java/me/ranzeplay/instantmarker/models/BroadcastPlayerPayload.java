package me.ranzeplay.instantmarker.models;

import com.google.gson.Gson;
import me.ranzeplay.instantmarker.InstantMarker;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.time.Duration;
import java.util.List;

public class BroadcastPlayerPayload implements CustomPayload {
    public static final Id<BroadcastPlayerPayload> ID = new Id<>(InstantMarker.BROADCAST_PLAYER_LOCATION_ID);

    private String playerName;
    private BroadcastBlockPos targetPosition;
    private List<BroadcastItem> nearbyItems;
    private String biomeKey;
    private long broadcastTimestamp;
    private String dimensionKey;

    public BroadcastPlayerPayload(String playerName, BroadcastBlockPos targetPosition, List<BroadcastItem> nearbyItems, String biomeKey, String dimensionKey) {
        this.playerName = playerName;
        this.targetPosition = targetPosition;
        this.nearbyItems = nearbyItems;
        this.biomeKey = biomeKey;
        this.dimensionKey = dimensionKey;

        this.broadcastTimestamp = System.currentTimeMillis();
    }

    public double getDistance(Vec3d source) {
        return new Vec3d(targetPosition.getX(), targetPosition.getY(), targetPosition.getZ()).distanceTo(source);
    }

    public Text shortText(Vec3d sourcePos) {
        var playerNameText = Text.literal(playerName).formatted(Formatting.BOLD, Formatting.YELLOW);
        var locationText = Text.literal(String.format("(%d, %d, %d)", targetPosition.getX(), targetPosition.getY(), targetPosition.getZ())).formatted(Formatting.AQUA);

        var distanceText = Text.literal(String.format("(%.1fm)", getDistance(sourcePos))).formatted(Formatting.GREEN);
        var spanText = Text.literal(String.format("(%s)", getFormattedTimeSpanUntilNow())).formatted(Formatting.YELLOW);

        return Text.empty()
                .append(playerNameText)
                .append(" ")
                .append(locationText)
                .append(" ")
                .append(distanceText)
                .append(" ")
                .append(spanText);
    }

    public Text markerText(Vec3d sourcePos) {
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

    public Text locationText(Vec3d sourcePos) {
        var playerNameText = Text.literal(playerName).formatted(Formatting.BOLD, Formatting.YELLOW);

        var locationText = Text.literal(String.format("(%d, %d, %d)", targetPosition.getX(), targetPosition.getY(), targetPosition.getZ())).formatted(Formatting.AQUA);
        var dimensionText = Text.translatable(dimensionKey).formatted(Formatting.AQUA);

        var distanceText = Text.literal(String.format("(%.1fm)", getDistance(sourcePos))).formatted(Formatting.GREEN);

        return Text.empty()
                .append(playerNameText)
                .append(" @ ")
                .append(dimensionText)
                .append(" ")
                .append(locationText)
                .append(" ")
                .append(distanceText);
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

    public long getBroadcastTimestamp() {
        return broadcastTimestamp;
    }

    public String getFormattedTimeSpanUntilNow() {
        var now = System.currentTimeMillis();

        var span = Duration.ofMillis(now - broadcastTimestamp);

        if (span.toHoursPart() > 0) {
            return String.format("%dh", span.toHoursPart());
        } else if (span.toMinutesPart() > 0) {
            return String.format("%dm", span.toMinutesPart());
        } else if (span.toSecondsPart() > 0) {
            return String.format("%ds", span.toSecondsPart());
        } else if (span.toMillisPart() > 0) {
            return String.format("%dms", span.toMillisPart());
        } else {
            return "null";
        }
    }

    public String getBiomeKey() {
        return biomeKey;
    }

    public String getDimensionKey() {
        return dimensionKey;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
