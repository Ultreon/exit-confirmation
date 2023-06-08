package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.mods.exitconfirmation.config.Config;
import com.ultreon.mods.exitconfirmation.config.gui.ConfigEntry;
import com.ultreon.mods.exitconfirmation.config.gui.ValueSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public class FloatEntry extends ConfigEntry<Float> {
    private final float min;
    private final float max;

    public FloatEntry(String key, float value, float min, float max) {
        super(key, value);
        this.min = min;
        this.max = max;
    }

    @Override
    protected Float read(String text) {
        return Float.parseFloat(text);
    }

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }

    @Override
    public AbstractWidget createButton(Config options, int x, int y, int width) {
        return new ValueSliderButton(x, y, width, 20, Component.literal(String.valueOf((float) this.get())), this.get(), this.min, this.max) {
            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal(String.valueOf((float) this.getValue())));
            }
        };
    }

    @Override
    public void setFromWidget(AbstractWidget widget) {
        ValueSliderButton sliderButton = (ValueSliderButton) widget;
        float value = (float) sliderButton.getValue();
        this.set(value);
    }
}
