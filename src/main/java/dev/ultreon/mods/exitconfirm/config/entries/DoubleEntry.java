package dev.ultreon.mods.exitconfirm.config.entries;

import dev.ultreon.mods.exitconfirm.config.gui.ConfigEntry;

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

}
