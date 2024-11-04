package me.ranzeplay.instantmarker.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

@Environment(EnvType.CLIENT)
public class HudRenderingManager {
    public static void registerEvents() {
        HudRenderCallback.EVENT.register(HudRenderingManager::renderMarkers);
    }

    private static void renderMarkers(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        var client = MinecraftClient.getInstance();
        var textRenderer = client.textRenderer;
        final float lineHeight = textRenderer.fontHeight + 0.7f;
        float y = client.getWindow().getScaledHeight() - lineHeight;

        for (var marker : InstantMarkerClient.existingMarkers) {
            drawContext.drawText(textRenderer, marker.shortText(client.player.getPos()), 3, (int) y, RGB2Int((short) 255, (short) 255, (short) 255), true);
            y -= lineHeight;
        }
    }

    private static int RGB2Int(short r, short g, short b) {
        return ((0xFF << 24) | (r << 16) | (g << 8) | b);
    }
}
