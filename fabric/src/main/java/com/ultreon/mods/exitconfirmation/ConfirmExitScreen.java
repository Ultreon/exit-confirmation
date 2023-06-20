package com.ultreon.mods.exitconfirmation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@Environment(EnvType.CLIENT)
public class ConfirmExitScreen extends Screen {
    private final String description = I18n.translate("screen.exit_confirm.description");
    private final String title = I18n.translate("screen.exit_confirm.title");
    private Screen previousScreen;
    private int ticksUntilEnableIn;

    public ConfirmExitScreen(Screen previousScreen) {
        super();
    }

    @Override
    public void init() {
        super.init();

        this.buttons.clear();

        ButtonWidget yesButton;
        this.buttons.add(yesButton = new ButtonWidget(0, this.width / 2 - 105, this.height / 6 + 96, 100, 20, I18n.translate("gui.yes")));
        this.buttons.add(new ButtonWidget(1, this.width / 2 + 5, this.height / 6 + 96, 100, 20, I18n.translate("gui.no")));

        yesButton.active = false;

        this.setButtonDelay(ExitConfirmation.CONFIG.confirmDelay.get());
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if (button.id == 0) {
            if (this.client != null) {
                button.active = false;
                ExitConfirmation.allowExit = true;
                if (this.client.world != null && this.client.isIntegratedServerRunning()) {
                    WorldUtils.saveWorldThenQuitGame();
                    return;
                }

                this.client.scheduleStop();
            }
        } else if (button.id == 1) {
            if (this.client != null) {
                button.active = false;
                this.client.setScreen(this.previousScreen);
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();

        this.drawCenteredString(this.textRenderer, this.title, this.width / 2, 70, 0xffffff);
        this.drawCenteredString(this.textRenderer, this.description, this.width / 2, 90, 0xbfbfbf);

        super.render(mouseX, mouseY, partialTicks);

    }

    /**
     * Sets the number of ticks to wait before enabling the buttons.
     */
    public void setButtonDelay(int ticksUntilEnable) {
        this.ticksUntilEnableIn = ticksUntilEnable;
    }

    public void tick() {
        if (--this.ticksUntilEnableIn == 0) {
            for (ButtonWidget guiButton : this.buttons) {
                guiButton.active = true;
            }
        }
    }

    public void back() {
        this.client.setScreen(this.previousScreen);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void keyPressed(char id, int code) {

    }
}
