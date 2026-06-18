package dev.ultreon.mods.exitconfirm.mixin;

import com.mojang.blaze3d.platform.Window;
import dev.ultreon.mods.exitconfirm.ConfirmExitScreen;
import dev.ultreon.mods.exitconfirm.ExitConfirmation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Inject(method = "stop", at = @At("HEAD"), cancellable = true)
	private void exitConfirmation$init(CallbackInfo ci) {
		if (!ExitConfirmation.shouldClose()) {
			ci.cancel();

			//? >= 26.2 {
			/*if (!(Minecraft.getInstance().gui.screen() instanceof ConfirmExitScreen ))
				Minecraft.getInstance().gui.setScreen(new ConfirmExitScreen(Minecraft.getInstance().gui.screen()));
			*///? } else {
			if (!(Minecraft.getInstance().screen instanceof ConfirmExitScreen ))
				Minecraft.getInstance().setScreen(new ConfirmExitScreen(Minecraft.getInstance().screen));
			//? }
		}
	}
}
