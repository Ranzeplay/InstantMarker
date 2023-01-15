package me.ranzeplay.instantmarker.mixin;

import me.ranzeplay.instantmarker.client.hud.MarkerRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud
{
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "render", at = @At("RETURN"))
	private void onGameOverlayPost(MatrixStack matrixStack, float partialTicks, CallbackInfo ci)
	{
		MarkerRenderer.drawEveryTick(client, matrixStack);
	}
}
