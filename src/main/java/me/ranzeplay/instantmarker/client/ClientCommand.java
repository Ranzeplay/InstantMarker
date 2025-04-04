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
                .then(ClientCommandManager.literal("clear")
                        .executes(ClientCommand::ClearMarkers))
                .then(ClientCommandManager.literal("mute")
                        .then(ClientCommandManager.argument("player", StringArgumentType.word())
                                .suggests(PlayerManager::getOnlinePlayers)
                                .executes(ClientCommand::MutePlayer)))
                .then(ClientCommandManager.literal("unmute")
                        .then(ClientCommandManager.argument("player", StringArgumentType.word())
                                .suggests((context, builder) -> PlayerManager.getMutedPlayers(builder))
                                .executes(ClientCommand::UnmutePlayer)))
                .then(ClientCommandManager.literal("local")
                        .executes(ClientCommand::SwitchLocalMode))
                .then(ClientCommandManager.literal("sound")
                        .executes(ClientCommand::SwitchSound))
                .then(ClientCommandManager.literal("share")
                        .then(ClientCommandManager.literal("items")
                                .executes(ClientCommand::SwitchItemShare))
                        .then(ClientCommandManager.literal("biome")
                                .executes(ClientCommand::SwitchBiomeShare)))
                .then(ClientCommandManager.literal("here").executes(ClientCommand::here)));
    }

    private static int here(CommandContext<FabricClientCommandSource> fabricClientCommandSourceCommandContext) {
        PositionMarking.markPlayerPosition();
        return 1;
    }

    private static int SwitchItemShare(CommandContext<FabricClientCommandSource> context) {
        InstantMarkerClient.config.shareItems = !InstantMarkerClient.config.shareItems;

        if(InstantMarkerClient.config.shareItems) {
            context.getSource().sendFeedback(Text.translatable("text.instantmarker.share_items_on").formatted(Formatting.YELLOW));
        } else {
            context.getSource().sendFeedback(Text.translatable("text.instantmarker.share_items_off").formatted(Formatting.YELLOW));
        }

        return 1;
    }

    private static int SwitchBiomeShare(CommandContext<FabricClientCommandSource> context) {
        InstantMarkerClient.config.shareBiome = !InstantMarkerClient.config.shareBiome;

        if(InstantMarkerClient.config.shareBiome) {
            context.getSource().sendFeedback(Text.translatable("text.instantmarker.share_biome_on").formatted(Formatting.YELLOW));
        } else {
            context.getSource().sendFeedback(Text.translatable("text.instantmarker.share_biome_off").formatted(Formatting.YELLOW));
        }

        return 1;
    }

    private static int SwitchSound(CommandContext<FabricClientCommandSource> context) {
        InstantMarkerClient.config.enableSound = !InstantMarkerClient.config.enableSound;

        if(InstantMarkerClient.config.enableSound) {
            context.getSource().sendFeedback(Text.translatable("text.instantmarker.sound_on").formatted(Formatting.YELLOW));
        } else {
            context.getSource().sendFeedback(Text.translatable("text.instantmarker.sound_off").formatted(Formatting.YELLOW));
        }

        return 1;
    }

    private static int SwitchLocalMode(CommandContext<FabricClientCommandSource> context) {
        InstantMarkerClient.config.localMode = !InstantMarkerClient.config.localMode;

        if(InstantMarkerClient.config.localMode) {
            context.getSource().sendFeedback(Text.translatable("text.instantmarker.local_mode_on").formatted(Formatting.YELLOW));
        } else {
            context.getSource().sendFeedback(Text.translatable("text.instantmarker.local_mode_off").formatted(Formatting.YELLOW));
        }

        return 1;
    }

    private static int ClearMarkers(CommandContext<FabricClientCommandSource> context) {
        InstantMarkerClient.existingMarkers.clear();
        context.getSource().sendFeedback(Text.translatable("text.instantmarker.markers_cleared").formatted(Formatting.GREEN));
        return 1;
    }

    public static int MutePlayer(CommandContext<FabricClientCommandSource> context) {
        var playerName = getString(context, "player");

        if (playerName.equals("@a")) {
            InstantMarkerClient.mutedPlayers.addAll(new LinkedHashSet<>(context.getSource().getPlayerNames()));
            return 1;
        }

        // Show an error if the player has already muted
        if (InstantMarkerClient.mutedPlayers.contains(playerName)) {
            context.getSource().sendError(Text.literal(playerName)
                    .append(Text.translatable("text.instantmarker.already_muted"))
                    .formatted(Formatting.RED));
            return 0;
        }

        InstantMarkerClient.mutedPlayers.add(playerName);
        context.getSource().sendFeedback(Text.literal(playerName)
                .append(Text.translatable("text.instantmarker.player_mute"))
                .formatted(Formatting.GREEN));
        return 1;
    }

    private static int UnmutePlayer(CommandContext<FabricClientCommandSource> context) {
        var playerName = getString(context, "player");

        if (playerName.equals("@a")) {
            InstantMarkerClient.mutedPlayers.clear();
            return 1;
        }

        // Show an error if the player hasn't muted
        if (!InstantMarkerClient.mutedPlayers.contains(playerName)) {
            context.getSource().sendError(Text.literal(playerName)
                    .append(Text.translatable("text.instantmarker.not_muted"))
                    .formatted(Formatting.RED));
            return 0;
        }

        InstantMarkerClient.mutedPlayers.remove(playerName);
        context.getSource().sendFeedback(Text.literal(playerName)
                .append(Text.translatable("text.instantmarker.player_unmute"))
                .formatted(Formatting.GREEN));
        return 1;
    }
}
