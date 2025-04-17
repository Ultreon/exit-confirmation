package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.craft.client.gui.widget.TextEntry;
import com.ultreon.craft.client.gui.widget.Widget;
import com.ultreon.mods.exitconfirmation.config.Config;
import com.ultreon.mods.exitconfirmation.config.gui.ConfigEntry;

public class StringEntry extends ConfigEntry<String> {
    public StringEntry(String key, String value) {
        super(key, value);
    }

    @Override
    protected String read(String text) {
        return text;
    }

    @Override
    public Widget createButton(Config options, int x, int y, int width) {
        TextEntry editBox = new TextEntry(width, 20)
                .hint(this.getDescription());
        editBox.value(this.get());
        return editBox;
    }

    @Override
    public void setFromWidget(Widget widget) {
        TextEntry cycleButton = (TextEntry) widget;
        String value = cycleButton.getValue();
        this.set(value);
    }
}
