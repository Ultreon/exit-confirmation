package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.mods.exitconfirmation.config.ConfigEntry;

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
