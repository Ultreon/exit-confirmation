package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.mods.exitconfirmation.config.Config;
import com.ultreon.mods.exitconfirmation.config.gui.ConfigEntry;
import com.ultreon.mods.exitconfirmation.config.gui.ValueSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public class IntEntry extends ConfigEntry<Integer> {
    private final int min;
    private final int max;

    public IntEntry(String key, int value, int min, int max) {
        super(key, value);
        this.min = min;
        this.max = max;
    }

    @Override
    protected Integer read(String text) {
        return Integer.parseInt(text);
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }

    @Override
    public AbstractWidget createButton(Config options, int x, int y, int width) {
        return new ValueSliderButton(x, y, width, 20, Component.literal(String.valueOf((int) this.get())), this.get(), this.min, this.max) {
            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal(String.valueOf((int) this.getValue())));
            }
        };
    }

    @Override
    public void setFromWidget(AbstractWidget widget) {
        ValueSliderButton cycleButton = (ValueSliderButton) widget;
        int value = (int) cycleButton.getValue();
        this.set(value);
    }
}
