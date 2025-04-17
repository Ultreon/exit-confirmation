package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.craft.client.gui.widget.CycleButton;
import com.ultreon.craft.client.gui.widget.Widget;
import com.ultreon.craft.text.TextObject;
import com.ultreon.mods.exitconfirmation.config.Config;
import com.ultreon.mods.exitconfirmation.config.gui.ConfigEntry;

public class BooleanEntry extends ConfigEntry<Boolean> {
    public BooleanEntry(String key, boolean value) {
        super(key, value);
    }

    @Override
    protected Boolean read(String text) {
        return Boolean.parseBoolean(text);
    }

    @Override
    public CycleButton<State> createButton(Config options, int x, int y, int width) {
        return new CycleButton<>(x, y, width, TextObject.literal("Value"));
    }

    @Override
    public void setFromWidget(Widget widget) {
        CycleButton<?> cycleButton = (CycleButton<?>) widget;
        State state = (State) cycleButton.getValue();
        this.set(state.value);
    }

    @SuppressWarnings("unused")
    public enum State {
        TRUE(true), FALSE(false);

        public final boolean value;

        State(boolean value) {
            this.value = value;
        }
    }
}
