package dev.ultreon.mods.exitconfirm.config.entries;

import dev.ultreon.mods.exitconfirm.config.gui.ConfigEntry;

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

}
