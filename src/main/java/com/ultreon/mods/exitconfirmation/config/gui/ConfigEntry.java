package com.ultreon.mods.exitconfirmation.config.gui;

import com.google.common.base.Preconditions;
import com.ultreon.craft.client.gui.Bounds;
import com.ultreon.craft.client.gui.Position;
import com.ultreon.craft.client.gui.Renderer;
import com.ultreon.craft.client.gui.widget.Widget;
import com.ultreon.craft.text.TextObject;
import com.ultreon.mods.exitconfirmation.config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

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

    public TextObject getDescription() {
        return TextObject.translation("exit_confirm.config." + this.key);
    }

    public Widget createButton(Config options, int x, int y, int width) {
        return new Widget(x, y) {
            {
                this.width(width);
            }

            @Override
            public Widget position(Supplier<Position> position) {
                return this;
            }

            @Override
            public Widget bounds(Supplier<Bounds> position) {
                return this;
            }

            @Override
            public void renderWidget(Renderer gfx, int mouseX, int mouseY, float deltaTime) {
                gfx.textCenter(ConfigEntry.this.getDescription().copy().append(": N/A"), this.getX() + this.getWidth() / 2, this.getY() + (this.getHeight() / 2 - 5), 0xffffffff);
            }
        };
    }

    public abstract void setFromWidget(Widget widget);
}
