package com.ultreon.mods.exitconfirmation;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@SideOnly(Side.CLIENT)
public class ConfirmExitScreen extends GuiScreen {
    private final String description = I18n.format("screen.exit_confirm.description");
    private final String title = I18n.format("screen.exit_confirm.title");
    private GuiScreen previousScreen;
    private int ticksUntilEnableIn;

    public ConfirmExitScreen(GuiScreen previousScreen) {
        super();
    }

    @Override
    public void initGui() {
        super.initGui();

        this.buttonList.clear();

        GuiButton yesButton;
        this.buttonList.add(yesButton = new GuiButton(0, this.width / 2 - 105, this.height / 6 + 96, 100, 20, I18n.format("misc.yes")));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height / 6 + 96, 100, 20, I18n.format("misc.no")));

        yesButton.enabled = false;

        this.setButtonDelay(10);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            if (this.mc != null) {
                button.enabled = false;
                if (this.mc.theWorld != null && this.mc.isIntegratedServerRunning()) {
                    WorldUtils.saveWorldThenQuitGame();
                    return;
                }

                this.mc.shutdown();
            }
        } else if (button.id == 1) {
            if (this.mc != null) {
                button.enabled = false;
                this.mc.displayGuiScreen(this.previousScreen);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 70, 0xffffff);
        this.drawCenteredString(this.fontRendererObj, this.description, this.width / 2, 90, 0xbfbfbf);

        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    /**
     * Sets the number of ticks to wait before enabling the buttons.
     */
    public void setButtonDelay(int ticksUntilEnable) {
        this.ticksUntilEnableIn = ticksUntilEnable;
    }

    public void tick() {
        if (--this.ticksUntilEnableIn == 0) {
            for (GuiButton guiButton : this.buttonList) {
                guiButton.enabled = true;
            }
        }
    }

    public void back() {
        this.mc.displayGuiScreen(this.previousScreen);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        // Don't allow closing the GUI
    }
}
