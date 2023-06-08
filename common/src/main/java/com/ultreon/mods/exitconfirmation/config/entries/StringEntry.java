package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.mods.exitconfirmation.config.Config;
import com.ultreon.mods.exitconfirmation.config.gui.ConfigEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;

public class StringEntry extends ConfigEntry<String> {
    public StringEntry(String key, String value) {
        super(key, value);
    }

    @Override
    protected String read(String text) {
        return text;
    }

    @Override
    public AbstractWidget createButton(Config options, int x, int y, int width) {
        EditBox editBox = new EditBox(Minecraft.getInstance().font, x, y, width, 20, this.getDescription());
        editBox.setValue(this.get());
        return editBox;
    }

    @Override
    public void setFromWidget(AbstractWidget widget) {
        EditBox cycleButton = (EditBox) widget;
        String value = cycleButton.getValue();
        this.set(value);
    }
}
