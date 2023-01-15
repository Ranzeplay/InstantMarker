package me.ranzeplay.instantmarker.client.hud;

import me.ranzeplay.instantmarker.client.InstantMarkerClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class MarkerRenderer {
    public static void drawEveryTick(MinecraftClient client, MatrixStack matrixStack) {
        var textRenderer = client.textRenderer;
        final int lineHeight = textRenderer.fontHeight + 2;

        int y = 3;
        for (var marker : InstantMarkerClient.existingMarkers) {
            assert client.player != null;

            textRenderer.draw(matrixStack, marker.shortText(client.player.getPos()), 15, y, WhiteColor());
            y += lineHeight;
        }
    }

    private static int WhiteColor() {
        return ((0xFF << 24)|((short) 255 << 16)|((short) 255 << 8)| (short) 255);
    }
}
