package dev.ultreon.mods.exitconfirmation.config.entries;

import dev.ultreon.mods.exitconfirmation.config.gui.ConfigEntry;

public class StringEntry extends ConfigEntry<String> {
    public StringEntry(String key, String value) {
        super(key, value);
    }

    @Override
    protected String read(String text) {
        return text;
    }

}
