package me.ranzeplay.instantmarker;

import me.ranzeplay.instantmarker.models.BroadcastLocationPayload;
import me.ranzeplay.instantmarker.models.BroadcastPlayerPayload;
import me.ranzeplay.instantmarker.models.SuggestLocationPayload;
import me.ranzeplay.instantmarker.models.SuggestPlayerPayload;
import me.ranzeplay.instantmarker.server.PositionBroadcast;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
        PayloadTypeRegistry.playC2S().register(SuggestLocationPayload.ID, SuggestLocationPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SuggestPlayerPayload.ID, SuggestPlayerPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(BroadcastPlayerPayload.ID, BroadcastPlayerPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(BroadcastLocationPayload.ID, BroadcastLocationPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SuggestLocationPayload.ID, (payload, context)
                -> PositionBroadcast.broadcastLocation(context.server(), context.player(), payload));
        ServerPlayNetworking.registerGlobalReceiver(SuggestPlayerPayload.ID, (payload, context)
                -> PositionBroadcast.broadcastPlayer(context.server(), context.player(), payload));
    }
}
