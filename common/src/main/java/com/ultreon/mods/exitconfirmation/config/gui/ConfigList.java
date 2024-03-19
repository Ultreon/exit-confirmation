package com.ultreon.mods.exitconfirmation.config.gui;

import com.ultreon.mods.exitconfirmation.config.Config;
import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigList extends ObjectSelectionList<ConfigList.ListEntry> {
    private final List<ListEntry> entries = new ArrayList<>();

    public ConfigList(Minecraft minecraft, int width, int height, int y) {
        super(minecraft, width, height, y, 28);
        this.centerListVertically = false;
    }

    public void addEntries(ConfigEntry<?>[] options) {
        for (ConfigEntry<?> option : options) {
            ListEntry of = ListEntry.of(this, ExitConfirmation.CONFIG, this.getRowWidth(), option);
            this.entries.add(of);
            this.addEntry(of);
        }
    }

    @Override
    protected void clearEntries() {
        super.clearEntries();
        this.entries.clear();
    }

    public int getRowWidth() {
        return this.width - 4;
    }

    protected int getScrollbarPosition() {
        return this.width - 5;
    }

    public void save() {
        for (ListEntry entry : this.entries) {
            entry.configEntry.setFromWidget(entry.widget);
        }
        Config.save();
    }

    protected static class ListEntry extends ObjectSelectionList.Entry<ListEntry> {
        private final ConfigList list;
        final ConfigEntry<?> configEntry;
        final AbstractWidget widget;

        private ListEntry(ConfigList list, ConfigEntry<?> configEntry, AbstractWidget widget) {
            this.list = list;
            this.configEntry = configEntry;
            this.widget = widget;
        }

        public static ListEntry of(ConfigList list, Config config, int rowWidth, ConfigEntry<?> rightOption) {
            return new ListEntry(list, rightOption, rightOption.createButton(config, rowWidth - 160, 0, 150));
        }

        public void render(GuiGraphics gfx, int index, int y, int x, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean selected, float partialTicks) {
            if (this.list.isMouseOver(mouseX, mouseY) && this.isMouseOver(mouseX, mouseY)) {
                gfx.fill(x - 4, y, x + rowWidth, y + rowHeight, 0x40ffffff);
            }

            Minecraft mc = Minecraft.getInstance();
            gfx.drawString(mc.font, this.configEntry.getDescription(), 2 + x, y + rowHeight / 2 - mc.font.lineHeight / 2, 0xffffffff, true);

            this.widget.setY(y + 2);
            this.widget.render(gfx, mouseX, mouseY, partialTicks);
        }

        @NotNull
        public List<? extends GuiEventListener> children() {
            return Collections.singletonList(this.widget);
        }

        @NotNull
        public List<? extends NarratableEntry> narratables() {
            return Collections.singletonList(this.widget);
        }

        @Override
        public Component getNarration() {
            return this.configEntry.getDescription();
        }
    }
}
