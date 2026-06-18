package dev.ultreon.mods.exitconfirm.config.entries;

import dev.ultreon.mods.exitconfirm.config.gui.ConfigEntry;

public class BooleanEntry extends ConfigEntry<Boolean> {
    public BooleanEntry(String key, boolean value) {
        super(key, value);
    }

    @Override
    protected Boolean read(String text) {
        return Boolean.parseBoolean(text);
    }

}
