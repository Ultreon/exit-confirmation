package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.mods.exitconfirmation.config.gui.ConfigEntry;

import java.util.UUID;

public class UUIDEntry extends ConfigEntry<UUID> {
    public UUIDEntry(String key, UUID value) {
        super(key, value);
    }

    @Override
    protected UUID read(String text) {
        return UUID.fromString(text);
    }

}
