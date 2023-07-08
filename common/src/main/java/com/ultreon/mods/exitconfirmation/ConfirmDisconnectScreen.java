package com.ultreon.mods.exitconfirmation;

import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.text2speech.Narrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.NarratorStatus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class ConfirmDisconnectScreen extends ConfirmScreen {
    private static final Component DESCRIPTION = Component.translatable("screen.disconnect_confirm.description");
    private static final Component TITLE = Component.translatable("screen.disconnect_confirm.title");

    public ConfirmDisconnectScreen(Screen background) {
        super(background, TITLE, DESCRIPTION);
    }

    @Override
    public void yesButtonCallback(Button btn) {
        if (this.minecraft != null) {
            btn.active = false;
            this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, WorldUtils::saveWorldThenOpenTitle, true);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
