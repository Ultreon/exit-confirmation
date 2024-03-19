package com.ultreon.mods.exitconfirmation.mixin.forge;

import com.ultreon.mods.exitconfirmation.ConfirmDisconnectScreen;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends Screen {
    @Shadow
    protected abstract void onDisconnect();

    protected PauseScreenMixin(Component component) {
        super(component);
    }

    @Redirect(method = "createPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button;builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button$Builder;", ordinal = 2))
    private Button.Builder exitConfirmation$createPauseMenu(Component message, Button.OnPress onPress) {
        return Button.builder(message, button -> {
            button.active = false;
            var minecraft = Minecraft.getInstance();
            if (minecraft.screen == this) {
                if(ExitConfirmation.CONFIG.disconnectPrompt.get()) {
                    minecraft.setScreen(new ConfirmDisconnectScreen(minecraft.screen));
                } else {
                    minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::onDisconnect, true);
                }
            }
        });
    }
}

