package me.ranzeplay.instantmarker.client;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.LinkedHashSet;
import java.util.concurrent.CompletableFuture;

public class PlayerManager {
    public static CompletableFuture<Suggestions> getOnlinePlayers(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        var players = new LinkedHashSet<>(context.getSource().getPlayerNames());

        // Add all players shortcut
        // players.add("@a");

        for (var p : players) {
            builder.suggest(p);
        }

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> getMutedPlayers(SuggestionsBuilder builder) {
        var players = new LinkedHashSet<>(InstantMarkerClient.mutedPlayers);

        // Add all players shortcut
        // players.add("@a");

        for (var p : players) {
            builder.suggest(p);
        }

        return builder.buildFuture();
    }
}
