package dev.ultreon.mods.exitconfirm.config.entries;

import dev.ultreon.mods.exitconfirm.config.gui.ConfigEntry;

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
