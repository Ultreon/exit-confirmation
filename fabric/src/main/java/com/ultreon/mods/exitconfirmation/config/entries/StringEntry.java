package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.mods.exitconfirmation.config.ConfigEntry;

public class StringEntry extends ConfigEntry<String> {
    public StringEntry(String key, String value) {
        super(key, value);
    }

    @Override
    protected String read(String text) {
        return text;
    }

}
