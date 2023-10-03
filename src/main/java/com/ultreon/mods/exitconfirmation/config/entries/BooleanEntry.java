package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.mods.exitconfirmation.config.gui.ConfigEntry;

public class BooleanEntry extends ConfigEntry<Boolean> {
    public BooleanEntry(String key, boolean value) {
        super(key, value);
    }

    @Override
    protected Boolean read(String text) {
        return Boolean.parseBoolean(text);
    }

}
