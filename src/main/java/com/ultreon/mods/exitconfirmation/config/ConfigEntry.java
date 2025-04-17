package com.ultreon.mods.exitconfirmation.config;

import net.minecraft.util.Language;
import org.jetbrains.annotations.NotNull;

public abstract class ConfigEntry<T> {
    private final String key;
    private T value;
    private String comment;

    public ConfigEntry(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public T get() {
        return this.value;
    }

    public void set(@NotNull T value) {
        this.value = value;
    }

    public ConfigEntry<T> comment(String comment) {
        this.comment = comment;
        return this;
    }

    protected abstract T read(String text);

    public void readAndSet(String text) {
        try {
            this.value = this.read(text);
        } catch (Exception ignored) {

        }
    }

    public String getComment() {
        return this.comment;
    }

    public String getKey() {
        return this.key;
    }

    public String write() {
        return this.value.toString();
    }

    public String getDescription() {
        return Language.getInstance().translate("exit_confirm.config." + this.key);
    }
}
