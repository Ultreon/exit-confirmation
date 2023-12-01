package com.ultreon.mods.exitconfirmation.config.gui;

import com.ultreon.craft.client.gui.Bounds;
import com.ultreon.craft.client.gui.GuiBuilder;
import com.ultreon.craft.client.gui.Position;
import com.ultreon.craft.client.gui.Renderer;
import com.ultreon.craft.client.gui.screens.Screen;
import com.ultreon.craft.client.gui.widget.SelectionList;
import com.ultreon.craft.client.gui.widget.TextButton;
import com.ultreon.craft.text.TextObject;
import com.ultreon.craft.util.Color;
import com.ultreon.mods.exitconfirmation.config.Config;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ConfigScreen extends Screen {
    private SelectionList<ConfigEntry<?>> list;
    private TextButton doneButton;
    private TextButton cancelButton;

    public ConfigScreen(Screen parent) {
        super(TextObject.translation("exit_confirm.screen.config"), parent);
    }

    @Override
    public void build(GuiBuilder builder) {
        this.list = new SelectionList<ConfigEntry<?>>(28)
                .entries(Arrays.asList(Config.values()))
                .bounds(() -> new Bounds(0, 32, this.getWidth(), this.getHeight() - 64));
        this.add(this.list);

        this.doneButton = TextButton.of(TextObject.translation("ultracraft.ui.ok"), 150, 20).callback(button -> {
            Config.save();
            assert this.client != null;
            this.back();
        }).position(() -> new Position(this.getWidth() / 2 + 5, this.getHeight() - 6 - 20));
        this.add(this.doneButton);

        this.cancelButton = TextButton.of(TextObject.translation("ultracraft.ui.cancel"), 150, 20)
                .callback(button -> {
                    Config.load();
                    this.back();
                })
                .position(() -> new Position(this.getWidth() / 2 - 155, this.getHeight() - 6 - 20));
        this.add(this.cancelButton);
    }

    @Override
    public void renderWidget(@NotNull Renderer renderer, int mouseX, int mouseY, @IntRange(from = 0L) float deltaTime) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTime);

        renderer.textCenter(this.getTitle(), this.getWidth() / 2, 16 - this.font.lineHeight / 2, Color.WHITE);
    }

    public SelectionList<ConfigEntry<?>> getList() {
        return this.list;
    }

    public TextButton getDoneButton() {
        return this.doneButton;
    }
}
