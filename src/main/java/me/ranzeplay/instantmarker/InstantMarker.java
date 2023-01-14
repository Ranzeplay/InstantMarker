package me.ranzeplay.instantmarker;

import me.ranzeplay.instantmarker.server.PositionBroadcast;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstantMarker implements ModInitializer {
    public static final String MOD_ID = "instantmarker";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

    public static final Identifier SUGGEST_LOCATION_ID = new Identifier(MOD_ID, "suggest_location");
    public static final Identifier BROADCAST_LOCATION_ID = new Identifier(MOD_ID, "broadcast_location");

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Hello Fabric world!");

        ServerPlayNetworking.registerGlobalReceiver(SUGGEST_LOCATION_ID, (minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, packetSender)
                -> PositionBroadcast.Broadcast(minecraftServer, sender, packetByteBuf));
    }
}
