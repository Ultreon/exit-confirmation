package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.craft.client.gui.Bounds;
import com.ultreon.craft.client.gui.widget.Slider;
import com.ultreon.craft.client.gui.widget.Widget;
import com.ultreon.craft.text.TextObject;
import com.ultreon.mods.exitconfirmation.config.Config;
import com.ultreon.mods.exitconfirmation.config.gui.ConfigEntry;
import com.ultreon.mods.exitconfirmation.config.gui.ValueSliderButton;

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
    public Slider createButton(Config options, int x, int y, int width) {
        return new Slider(this.get(), this.min, this.max).text(TextObject.literal(String.valueOf((int) this.get()))).bounds(() -> new Bounds(x, y, width, 20));
    }

    @Override
    public void setFromWidget(Widget widget) {
        ValueSliderButton cycleButton = (ValueSliderButton) widget;
        int value = cycleButton.value().get();
        this.set(value);
    }
}
