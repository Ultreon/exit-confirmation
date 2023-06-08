package com.ultreon.mods.exitconfirmation.config.gui;

import com.ultreon.mods.exitconfirmation.config.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {
    private final Screen back;
    private ConfigList list;
    private Button doneButton;
    private Button cancelButton;

    public ConfigScreen(Screen back) {
        super(Component.translatable("screen.exit_confirm.config"));
        this.back = back;
    }

    @Override
    protected void init() {
        this.clearWidgets();
        super.init();

        this.list = new ConfigList(this.minecraft, this.width, this.height, 32, this.height - 32);
        this.list.addEntries(Config.values());
        this.addRenderableWidget(this.list);

        this.doneButton = new Button.Builder(CommonComponents.GUI_DONE, button -> {
            this.list.save();
            assert this.minecraft != null;
            this.minecraft.setScreen(this.back);
        }).bounds(this.width / 2 + 5, this.height - 6 - 20, 150, 20).build();
        this.addRenderableWidget(this.doneButton);

        this.cancelButton = new Button.Builder(CommonComponents.GUI_CANCEL, button -> {
            assert this.minecraft != null;
            this.minecraft.setScreen(this.back);
        }).bounds(this.width / 2 - 155, this.height - 6 - 20, 150, 20).build();
        this.addRenderableWidget(this.cancelButton);
    }

    @Override
    public void render(GuiGraphics gfx, int i, int j, float f) {
        super.render(gfx, i, j, f);

        gfx.drawCenteredString(this.font, this.getTitle(), this.width / 2, 16 - this.font.lineHeight / 2, 0xffffffff);
    }

    public Screen getBack() {
        return this.back;
    }

    public ConfigList getList() {
        return this.list;
    }

    public Button getDoneButton() {
        return this.doneButton;
    }
}
