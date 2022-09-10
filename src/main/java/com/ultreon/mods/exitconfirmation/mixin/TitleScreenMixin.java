package com.ultreon.mods.exitconfirmation.mixin;

import com.ultreon.mods.exitconfirmation.Config;
import com.ultreon.mods.exitconfirmation.ConfirmExitScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    private boolean exitConfirmation$escPress;

    protected TitleScreenMixin(Component component) {
        super(component);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && this.shouldCloseOnEsc() && Config.closePrompt.get() && Config.quitOnEscInTitle.get()) {
            if (!exitConfirmation$escPress) {
                exitConfirmation$escPress = true;
                var minecraft = Minecraft.getInstance();
                if (minecraft.screen == this) {
                    minecraft.setScreen(new ConfirmExitScreen(minecraft.screen));
                    return true;
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        var minecraft = Minecraft.getInstance();
        if (keyCode == 256 && exitConfirmation$escPress && Config.closePrompt.get() && Config.quitOnEscInTitle.get() && minecraft.screen == this) {
            exitConfirmation$escPress = false;
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}
