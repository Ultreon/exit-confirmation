package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.mods.exitconfirmation.config.Config;
import com.ultreon.mods.exitconfirmation.config.gui.ConfigEntry;
import com.ultreon.mods.exitconfirmation.config.gui.ValueSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public class DoubleEntry extends ConfigEntry<Double> {
    private final double min;
    private final double max;

    public DoubleEntry(String key, double value, double min, double max) {
        super(key, value);
        this.min = min;
        this.max = max;
    }

    @Override
    protected Double read(String text) {
        return Double.parseDouble(text);
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    @Override
    public AbstractWidget createButton(Config options, int x, int y, int width) {
        return new ValueSliderButton(x, y, width, 20, Component.literal(String.valueOf(this.get())), this.get(), this.min, this.max) {
            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal(String.valueOf(this.getValue())));
            }
        };
    }

    @Override
    public void setFromWidget(AbstractWidget widget) {
        ValueSliderButton sliderButton = (ValueSliderButton) widget;
        double value = sliderButton.getValue();
        this.set(value);
    }
}
