package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.craft.client.gui.Bounds;
import com.ultreon.craft.client.gui.widget.Slider;
import com.ultreon.craft.client.gui.widget.Widget;
import com.ultreon.craft.text.TextObject;
import com.ultreon.mods.exitconfirmation.config.Config;
import com.ultreon.mods.exitconfirmation.config.gui.ConfigEntry;
import com.ultreon.mods.exitconfirmation.config.gui.ValueSliderButton;

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
    public Slider createButton(Config options, int x, int y, int width) {
        return new Slider(this.get().intValue(), (int) this.min, (int) this.max).text(TextObject.literal(String.valueOf((this.get())))).bounds(() -> new Bounds(x, y, width, 20));
    }

    @Override
    public void setFromWidget(Widget widget) {
        ValueSliderButton cycleButton = (ValueSliderButton) widget;
        int value = cycleButton.value().get();
        this.set((long) value);
    }
}
