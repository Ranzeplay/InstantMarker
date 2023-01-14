package me.ranzeplay.instantmarker.client;

import me.ranzeplay.instantmarker.BlockBroadcastPacket;
import me.ranzeplay.instantmarker.InstantMarker;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class PositionMarking {
    public static void MarkPosition() {
        var player = MinecraftClient.getInstance().player;
        assert player != null;

        HitResult hit = player.raycast(800, 0, true);

        if (hit.getType() == HitResult.Type.BLOCK) {
            var blockPos = ((BlockHitResult) hit).getBlockPos();
            var block = player.getWorld().getBlockState(blockPos).getBlock();

            player.sendMessage(Text.of(String.format("Marked block at (%d, %d, %d), whose type is %s",
                    blockPos.getX(),
                    blockPos.getY(),
                    blockPos.getZ(),
                    Text.translatable(block.getTranslationKey()).getString())));

            ClientPlayNetworking.send(InstantMarker.SUGGEST_LOCATION_ID, PacketByteBufs.create().writeBlockPos(blockPos));
        } else if (hit.getType() == HitResult.Type.ENTITY) {
            var entity = ((EntityHitResult) hit).getEntity();

            player.sendMessage(Text.of(String.format("Marked entity at (%.2f, %.2f, %.2f), whose type is %s", entity.getX(), entity.getY(), entity.getZ(), entity.getEntityName())));
        } else {
            player.sendMessage(Text.of("Nothing found!"));
        }
    }

    public static void ReceiveMarker(MinecraftClient client, PacketByteBuf buf) {
        var packetContent = BlockBroadcastPacket.fromPacketByteBuf(buf);
        var playerName = packetContent.playerName();
        var pos = packetContent.targetPosition();

        assert client.player != null;

        var playerNameText = Text.literal(playerName).formatted(Formatting.BOLD, Formatting.YELLOW);
        var locationText = Text.literal(String.format("(%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ())).formatted(Formatting.AQUA);

        var distance = new Vec3d(pos.getX(), pos.getY(), pos.getZ()).distanceTo(client.player.getPos());
        var distanceText = Text.literal(String.format(" (%.1fm)", distance)).formatted(Formatting.GREEN);

        client.player.sendMessage(Text.empty()
                        .append(playerNameText)
                        .append(" suggested a position ")
                        .append(locationText)
                        .append(distanceText),
                true);
    }
}
