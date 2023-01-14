package me.ranzeplay.instantmarker.client;

import me.ranzeplay.instantmarker.BlockBroadcastPacket;
import me.ranzeplay.instantmarker.InstantMarker;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class PositionMarking {
    public static void MarkPosition() {
        var player = MinecraftClient.getInstance().player;
        assert player != null;

        HitResult hit = player.raycast(100, 0, true);

        if (hit.getType() == HitResult.Type.BLOCK) {
            var blockPos = ((BlockHitResult) hit).getBlockPos();
            var block = player.getWorld().getBlockState(blockPos).getBlock();

            player.sendMessage(Text.of(String.format("Hit block at (%d, %d, %d), whose type is %s", blockPos.getX(), blockPos.getY(), blockPos.getZ(), block.getName())));

            ClientPlayNetworking.send(InstantMarker.SUGGEST_LOCATION_ID, PacketByteBufs.create().writeBlockPos(blockPos));
        } else if (hit.getType() == HitResult.Type.ENTITY) {
            var entity = ((EntityHitResult) hit).getEntity();

            player.sendMessage(Text.of(String.format("Hit entity at (%.2f, %.2f, %.2f), whose type is %s", entity.getX(), entity.getY(), entity.getZ(), entity.getEntityName())));
        } else {
            player.sendMessage(Text.of("Nothing found!"));
        }
    }

    public static void ReceiveMarker(MinecraftClient minecraftClient, PacketByteBuf packetByteBuf) {
        var packetContent = BlockBroadcastPacket.fromPacketByteBuf(packetByteBuf);
        var playerName = packetContent.getPlayerName();
        var pos = packetContent.getTargetPosition();

        assert minecraftClient.player != null;
        minecraftClient.player.sendMessage(Text.of(String.format("%s suggested a location at (%d, %d, %d)", playerName, pos.getX(), pos.getY(), pos.getZ())));
    }
}
