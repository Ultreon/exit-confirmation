package com.ultreon.mods.exitconfirmation.config.gui;

import com.google.common.base.Preconditions;
import com.ultreon.mods.exitconfirmation.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public abstract class ConfigEntry<T> {
    private final String key;
    private T value;
    private String comment;

    public ConfigEntry(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public T get() {
        return this.value;
    }

    public void set(@NotNull T value) {
        Preconditions.checkNotNull(value, "Entry value shouldn't be null.");
        this.value = value;
    }

    public ConfigEntry<T> comment(String comment) {
        this.comment = comment;
        return this;
    }

    protected abstract T read(String text);

    public void readAndSet(String text) {
        try {
            this.value = this.read(text);
        } catch (Exception ignored) {

        }
    }

    public String getComment() {
        return this.comment;
    }

    public String getKey() {
        return this.key;
    }

    public String write() {
        return this.value.toString();
    }

    public MutableComponent getDescription() {
        return Component.translatable("exit_confirm.config." + this.key);
    }

    public AbstractWidget createButton(Config options, int x, int y, int width) {
        return new AbstractWidget(x, y, width, 20, this.getDescription()) {
            @Override
            public void renderWidget(GuiGraphics gfx, int i, int j, float f) {
                gfx.drawCenteredString(Minecraft.getInstance().font, ConfigEntry.this.getDescription().append(": N/A"), this.getX() + this.width / 2, this.getY() + (this.height / 2 - 5), 0xffffffff);
            }

            @Override
            protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

            }
        };
    }

    public abstract void setFromWidget(AbstractWidget widget);
}
