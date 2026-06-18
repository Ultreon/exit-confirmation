package dev.ultreon.mods.exitconfirm.config.entries;

import dev.ultreon.mods.exitconfirm.config.gui.ConfigEntry;

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

}
