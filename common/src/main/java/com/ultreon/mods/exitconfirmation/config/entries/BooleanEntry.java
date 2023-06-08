package com.ultreon.mods.exitconfirmation.config.entries;

import com.ultreon.mods.exitconfirmation.config.Config;
import com.ultreon.mods.exitconfirmation.config.gui.ConfigEntry;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.CommonComponents;

public class BooleanEntry extends ConfigEntry<Boolean> {
    public BooleanEntry(String key, boolean value) {
        super(key, value);
    }

    @Override
    protected Boolean read(String text) {
        return Boolean.parseBoolean(text);
    }

    @Override
    public AbstractWidget createButton(Config options, int x, int y, int width) {
        return new CycleButton.Builder<>(o -> (boolean)o ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF).withValues(true, false).displayOnlyValue().withInitialValue(this.get()).create(x, y, width, 20, null);
    }

    @Override
    public void setFromWidget(AbstractWidget widget) {
        CycleButton<?> cycleButton = (CycleButton<?>) widget;
        boolean value = (boolean) cycleButton.getValue();
        this.set(value);
    }
}
