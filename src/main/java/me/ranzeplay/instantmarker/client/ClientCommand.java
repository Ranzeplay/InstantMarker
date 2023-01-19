package me.ranzeplay.instantmarker.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.LinkedHashSet;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;

public class ClientCommand {
    public static void Register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("im")
                .then(ClientCommandManager.literal("clear").executes(ClientCommand::ClearMarkers))
                .then(ClientCommandManager.literal("mute")
                        .then(ClientCommandManager.argument("player", StringArgumentType.word())
                                .suggests(PlayerManager::getOnlinePlayers)
                                .executes(ClientCommand::MutePlayer)))
                .then(ClientCommandManager.literal("unmute")
                        .then(ClientCommandManager.argument("player", StringArgumentType.word())
                                .suggests((context, builder) -> PlayerManager.getMutedPlayers(builder))
                                .executes(ClientCommand::UnmutePlayer))));
    }



    private static int ClearMarkers(CommandContext<FabricClientCommandSource> context) {
        InstantMarkerClient.existingMarkers.clear();
        context.getSource().sendFeedback(Text.literal("Markers cleared!").formatted(Formatting.GREEN));
        return 1;
    }

    public static int MutePlayer(CommandContext<FabricClientCommandSource> context) {
        var playerName = getString(context, "player");

        if(playerName.equals("@a")) {
            InstantMarkerClient.mutedPlayers.addAll(new LinkedHashSet<>(context.getSource().getPlayerNames()));
            return 1;
        }

        if(InstantMarkerClient.mutedPlayers.contains(playerName)) {
            context.getSource().sendError(Text.literal("You have already muted " + playerName).formatted(Formatting.RED));
            return 0;
        }

        InstantMarkerClient.mutedPlayers.add(playerName);
        return 1;
    }

    private static int UnmutePlayer(CommandContext<FabricClientCommandSource> context) {
        var playerName = getString(context, "player");

        if(playerName.equals("@a")) {
            InstantMarkerClient.mutedPlayers.clear();
            return 1;
        }

        if(!InstantMarkerClient.mutedPlayers.contains(playerName)) {
            context.getSource().sendError(Text.literal(playerName + " is not in the list").formatted(Formatting.RED));
            return 0;
        }

        InstantMarkerClient.mutedPlayers.remove(playerName);
        return 1;
    }
}
