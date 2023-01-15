package me.ranzeplay.instantmarker.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ClientCommand {
    public static int ClearMarkers(CommandContext<ServerCommandSource> context) {
        InstantMarkerClient.existingMarkers.clear();
        return 1;
    }

    public static void Register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("im")
                .then(CommandManager.literal("clear").executes(ClientCommand::ClearMarkers)));
    }
}
