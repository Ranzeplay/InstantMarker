package me.ranzeplay.instantmarker.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ClientCommand {
    public static void Register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("im")
                .then(ClientCommandManager.literal("clear").executes(ClientCommand::ClearMarkers)));
    }

    private static int ClearMarkers(CommandContext<FabricClientCommandSource> serverCommandSourceCommandContext) {
        InstantMarkerClient.existingMarkers.clear();
        return 1;
    }
}
