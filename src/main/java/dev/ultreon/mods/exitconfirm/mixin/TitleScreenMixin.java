package dev.ultreon.mods.exitconfirm.mixin;

import dev.ultreon.mods.exitconfirm.ExitConfirmation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? >= 26.1 {
/*import net.minecraft.client.input.KeyEvent;
*///? }

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component component) {
        super(component);
    }

	@Inject(method = "init", at = @At("TAIL"))
	private void exitConfirmation$init(CallbackInfo ci) {
		ExitConfirmation.onTitleScreenInit(Minecraft.getInstance(), this);
	}

	//? < 26.1 {
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.quitOnEscInTitle.get()) {
            var minecraft = Minecraft.getInstance();
            if (minecraft.screen == this) {
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
	//? } else {
	/*@Override
	public boolean keyPressed(KeyEvent event) {
        if (event.key() == 256 && ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.quitOnEscInTitle.get()) {
            var minecraft = Minecraft.getInstance();
			//? >= 26.2 {
            /^if (minecraft.gui.screen() == this) {
                return true;
            }
			^///? } else {
			if (minecraft.screen == this) {
				return true;
			}
			//? }
        }
		return super.keyPressed(event);
    }
	*///? }
}
