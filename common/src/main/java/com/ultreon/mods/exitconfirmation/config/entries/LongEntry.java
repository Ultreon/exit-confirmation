package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.mods.exitconfirmation.config.Config;
import com.ultreon.mods.exitconfirmation.config.gui.ConfigEntry;
import com.ultreon.mods.exitconfirmation.config.gui.ValueSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public class LongEntry extends ConfigEntry<Long> {
    private final long min;
    private final long max;

    public LongEntry(String key, long value, long min, long max) {
        super(key, value);
        this.min = min;
        this.max = max;
    }

    @Override
    protected Long read(String text) {
        return Long.parseLong(text);
    }

    public long getMin() {
        return this.min;
    }

    public long getMax() {
        return this.max;
    }

    @Override
    public AbstractWidget createButton(Config options, int x, int y, int width) {
        return new ValueSliderButton(x, y, width, 20, Component.literal(String.valueOf((long) this.get())), this.get(), this.min, this.max) {
            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal(String.valueOf((long) this.getValue())));
            }
        };
    }

    @Override
    public void setFromWidget(AbstractWidget widget) {
        ValueSliderButton cycleButton = (ValueSliderButton) widget;
        long value = (long) cycleButton.getValue();
        this.set(value);
    }
}
