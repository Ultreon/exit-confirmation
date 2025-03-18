package dev.ultreon.mods.exitconfirmation.mixin;

import dev.ultreon.mods.exitconfirmation.ExitConfirmation;
import dev.ultreon.mods.exitconfirmation.ExitSource;
import dev.ultreon.mods.exitconfirmation.GameExitEvent;
import dev.ultreon.mods.xinexlib.event.system.EventSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component component) {
        super(component);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && ExitConfirmation.CONFIG.closePrompt.get() && ExitConfirmation.CONFIG.quitOnEscInTitle.get()) {
            var minecraft = Minecraft.getInstance();
            if (minecraft.screen == this) {
                EventSystem.MAIN.publish(new GameExitEvent(ExitSource.keyboardInScreen(this, keyCode), minecraft));
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
