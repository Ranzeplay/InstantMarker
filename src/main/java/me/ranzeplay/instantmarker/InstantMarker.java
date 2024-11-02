package me.ranzeplay.instantmarker;

import me.ranzeplay.instantmarker.server.PositionBroadcast;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstantMarker implements ModInitializer {
    public static final String MOD_ID = "instantmarker";
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

    public static final Identifier SUGGEST_LOCATION_ID = Identifier.of(MOD_ID, "suggest_location");
    public static final Identifier BROADCAST_MARKER_ID = Identifier.of(MOD_ID, "broadcast_location");
    public static final Identifier SUGGEST_PLAYER_ID = Identifier.of(MOD_ID, "suggest_player");
    public static final Identifier BROADCAST_PLAYER_LOCATION_ID = Identifier.of(MOD_ID, "broadcast_player");

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(new CustomPayload.Id<>(SUGGEST_LOCATION_ID), (payload, context)
                -> PositionBroadcast.BroadcastLocation(context.server(), context.player(), payload));
        ServerPlayNetworking.registerGlobalReceiver(new CustomPayload.Id<>(SUGGEST_PLAYER_ID), (payload, context)
                -> PositionBroadcast.BroadcastPlayer(context.server(), context.player(), payload));
    }
}
