package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.mods.exitconfirmation.config.ConfigEntry;

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

}
