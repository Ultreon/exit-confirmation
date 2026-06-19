package dev.ultreon.mods.exitconfirm.mixin;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import dev.ultreon.mods.exitconfirm.ConfirmDisconnectScreen;
import dev.ultreon.mods.exitconfirm.ExitConfirmation;
import dev.ultreon.mods.exitconfirm.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//? < 26.1 {
import org.spongepowered.asm.mixin.Shadow;
//? }

@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends Screen {
	protected PauseScreenMixin(Component component) {
		super(component);
	}

	//? < 1.19.2 && forge {
	/*@Redirect(method = "createPauseMenu", at = @At(value = "NEW", target = "(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button;", ordinal = 7))
	private Button exitConfirmation$createPauseMenu(int x, int y, int w, int h, Component message, Button.OnPress onPress) {
		return new Button(x, y, w, h, message, button -> {
			button.active = false;
			var minecraft = Minecraft.getInstance();
			if (minecraft.screen == this) {
				if (ExitConfirmation.CONFIG.disconnectPrompt.get()) {
					minecraft.setScreen(new ConfirmDisconnectScreen(minecraft.screen));
				} else {
					WorldUtils.saveWorldThenOpenTitle();
				}
			}
		});
	}
	*///? } else < 1.19.4 && forge {
	/*@Redirect(method = "createPauseMenu", at = @At(value = "NEW", target = "(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button;", ordinal = 9))
	private Button exitConfirmation$createPauseMenu(int x, int y, int w, int h, Component message, Button.OnPress onPress) {
		return new Button(x, y, w, h, message, button -> {
			button.active = false;
			var minecraft = Minecraft.getInstance();
			if (minecraft.screen == this) {
				if (ExitConfirmation.CONFIG.disconnectPrompt.get()) {
					minecraft.setScreen(new ConfirmDisconnectScreen(minecraft.screen));
				} else {
					WorldUtils.saveWorldThenOpenTitle();
				}
			}
		});
	}
	*///? } else forge {
	/*@Shadow
    protected abstract void onDisconnect();

	@Redirect(method = "createPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button;builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button$Builder;", ordinal = 2))
	private Button.Builder exitConfirmation$createPauseMenu(Component message, Button.OnPress onPress) {
		return Button.builder(message, button -> {
			button.active = false;
			var minecraft = Minecraft.getInstance();
			if (minecraft.screen == this) {
				if(ExitConfirmation.CONFIG.disconnectPrompt.get()) {
					minecraft.setScreen(new ConfirmDisconnectScreen(minecraft.screen));
				} else {
					minecraft.getReportingContext().draftReportHandled(this.minecraft, this, WorldUtils::saveWorldThenOpenTitle, true);
				}
			}
		});
	}
	*///? } else neoforge {

	/*//? <= 1.21.1 {
	@Shadow
    protected abstract void onDisconnect();
	//? }

	//? < 26.1 && >= 1.21.7 {
	/^@Shadow
	public static void disconnectFromWorld(Minecraft par1, Component par2) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}
	^///?}

	@Redirect(method = "createPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button;builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button$Builder;", ordinal = 2))
	private Button.Builder exitConfirmation$createPauseMenu(Component message, Button.OnPress onPress) {
		return Button.builder(message, button -> {
			button.active = false;
			var minecraft = Minecraft.getInstance();
			//? >= 26.2 {
			/^if (minecraft.gui.screen() == this) {
				^///? } else {
				if (minecraft.screen == this) {
				 //? }
				if(ExitConfirmation.CONFIG.disconnectPrompt.get()) {
					//? >= 26.2 {
					/^minecraft.gui.setScreen(new ConfirmDisconnectScreen(minecraft.gui.screen()));
					^///? } else {
					minecraft.setScreen(new ConfirmDisconnectScreen(minecraft.screen));
					 //? }
				} else {
					//? >= 26.1 {
					/^this.minecraft
							.getReportingContext()
							.draftReportHandled(this.minecraft, this, () -> this.minecraft.disconnectFromWorld(ClientLevel.DEFAULT_QUIT_MESSAGE), true);
					^///? } else >= 1.21.7 {
					/^this.minecraft
							.getReportingContext()
							.draftReportHandled(this.minecraft, this, () -> disconnectFromWorld(this.minecraft, ClientLevel.DEFAULT_QUIT_MESSAGE), true);
					^///? } else >= 1.20.1 {
					/^this.minecraft
							.getReportingContext()
							.draftReportHandled(this.minecraft, this, this::onDisconnect, true);
					^///? } else {
					this.minecraft
							.getReportingContext()
							.draftReportHandled(this.minecraft, this, () -> this.minecraft.disconnectFromWorld(ClientLevel.DEFAULT_QUIT_MESSAGE), true);
					//? }
				}
			}
		});
	}
	*///? } else < 1.19.2 && fabric {
	/*@Redirect(method = "createPauseMenu", at = @At(value = "NEW", target = "(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button;", ordinal = 7))
	private Button exitConfirmation$createPauseMenu(int x, int y, int w, int h, Component message, Button.OnPress onPress) {
		return new Button(x, y, w, h, message, button -> {
			button.active = false;
			var minecraft = Minecraft.getInstance();
			if (minecraft.screen == this) {
				if (ExitConfirmation.CONFIG.disconnectPrompt.get()) {
					minecraft.setScreen(new ConfirmDisconnectScreen(minecraft.screen));
				} else {
					WorldUtils.saveWorldThenOpenTitle();
				}
			}
		});
	}
	*///? } else < 1.19.4 && fabric {
	/*@Redirect(method = "createPauseMenu", at = @At(value = "NEW", target = "(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button;", ordinal = 8))
	private Button exitConfirmation$createPauseMenu(int x, int y, int w, int h, Component message, Button.OnPress onPress) {
		return new Button(x, y, w, h, message, button -> {
			button.active = false;
			var minecraft = Minecraft.getInstance();
			if (minecraft.screen == this) {
				if (ExitConfirmation.CONFIG.disconnectPrompt.get()) {
					minecraft.setScreen(new ConfirmDisconnectScreen(minecraft.screen));
				} else {
					WorldUtils.saveWorldThenOpenTitle();
				}
			}
		});
	}
	*///? } else fabric {

	//? <= 1.21.1 {
	/*@Shadow
    protected abstract void onDisconnect();
	*///? }

	//? < 26.1 && >= 1.21.7 {
	@Shadow
	public static void disconnectFromWorld(Minecraft par1, Component par2) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}
	//?}

	//? < 26.2 {
	@Redirect(method = "createPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button;builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button$Builder;", ordinal = 1))
	private Button.Builder exitConfirmation$createPauseMenu(Component message, Button.OnPress onPress) {
		return Button.builder(message, button -> {
			button.active = false;
			var minecraft = Minecraft.getInstance();
			//? >= 26.2 {
			/*if (minecraft.gui.screen() == this) {
				*///? } else {
				if (minecraft.screen == this) {
				 //? }
				if (ExitConfirmation.CONFIG.disconnectPrompt.get()) {
					//? >= 26.2 {
					/*minecraft.gui.setScreen(new ConfirmDisconnectScreen(minecraft.gui.screen()));
					*///? } else {
					minecraft.setScreen(new ConfirmDisconnectScreen(minecraft.screen));
					 //? }
				} else {
					//? >= 26.1 {
					/*this.minecraft
							.getReportingContext()
							.draftReportHandled(this.minecraft, this, () -> this.minecraft.disconnectFromWorld(ClientLevel.DEFAULT_QUIT_MESSAGE), true);
					*///? } else >= 1.21.7 {
					this.minecraft
							.getReportingContext()
							.draftReportHandled(this.minecraft, this, () -> disconnectFromWorld(this.minecraft, ClientLevel.DEFAULT_QUIT_MESSAGE), true);
					//? } else >= 1.20.1 {
					/*this.minecraft
							.getReportingContext()
							.draftReportHandled(this.minecraft, this, this::onDisconnect, true);
					*///? } else {
					/*this.minecraft
							.getReportingContext()
							.draftReportHandled(this.minecraft, this, () -> WorldUtils.saveWorldThenOpenTitle(), true);
					*///? }
				}
			}
		});
	}
	//? } else {
	/*@Redirect(method = "createPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button;builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button$Builder;", ordinal = 2))
	private Button.Builder exitConfirmation$createPauseMenu(Component message, Button.OnPress onPress) {
		return Button.builder(message, button -> {
			button.active = false;
			var minecraft = Minecraft.getInstance();
			//? >= 26.2 {
			/^if (minecraft.gui.screen() == this) {
			^///? } else {
			if (minecraft.screen == this) {
			//? }
				if(ExitConfirmation.CONFIG.disconnectPrompt.get()) {
					//? >= 26.2 {
					/^minecraft.gui.setScreen(new ConfirmDisconnectScreen(minecraft.gui.screen()));
					^///? } else {
					minecraft.setScreen(new ConfirmDisconnectScreen(minecraft.screen));
					//? }
				} else {
					//? >= 26.1 {
					/^this.minecraft
						.getReportingContext()
						.draftReportHandled(this.minecraft, this, () -> this.minecraft.disconnectFromWorld(ClientLevel.DEFAULT_QUIT_MESSAGE), true);
					^///? } else >= 1.21.7 {
					/^this.minecraft
							.getReportingContext()
							.draftReportHandled(this.minecraft, this, () -> disconnectFromWorld(this.minecraft, ClientLevel.DEFAULT_QUIT_MESSAGE), true);
					^///? } else >= 1.20.1 {
					this.minecraft
							.getReportingContext()
							.draftReportHandled(this.minecraft, this, this::onDisconnect, true);
					//? } else {
					/^this.minecraft
							.getReportingContext()
							.draftReportHandled(this.minecraft, this, () -> WorldUtils.saveWorldThenOpenTitle(), true);
					^///? }
				}
			}
		});
	}
	*///? }
	//? }
}

