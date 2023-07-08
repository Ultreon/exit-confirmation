package com.ultreon.mods.exitconfirmation;

import com.mojang.text2speech.Narrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.NarratorStatus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public abstract class ConfirmScreen extends Screen {
    private final Component title;
    private final Component description;
    private final MultiLineLabel label = MultiLineLabel.EMPTY;
    protected final Component yesButtonText;
    protected final Component noButtonText;
    protected final Screen background;
    protected Button yesButton;
    private int activateDelay;

    protected ConfirmScreen(Screen background, Component title, Component description) {
        super(title);
        this.yesButtonText = CommonComponents.GUI_YES;
        this.noButtonText = CommonComponents.GUI_NO;

        this.title = title;
        this.description = description;

        this.background = background;
    }

    public abstract void yesButtonCallback(Button btn);

    protected void init() {
        if (ExitConfirmation.CONFIG.allowNarrator.get()) {
            NarratorStatus narratorStatus = Objects.requireNonNull(this.minecraft).options.narrator().get();

            if (narratorStatus == NarratorStatus.SYSTEM || narratorStatus == NarratorStatus.ALL) {
                Narrator.getNarrator().say(this.description.getString(), true);
            }
        }

        this.clearWidgets();

        this.yesButton = this.addRenderableWidget(Button.builder(this.yesButtonText, this::yesButtonCallback)
                .bounds(this.width / 2 - 105, this.height / 6 + 96, 100, 20).build());

        this.addRenderableWidget(Button.builder(this.noButtonText, (btn) -> {
            if (this.minecraft != null) {
                btn.active = false;
                this.minecraft.setScreen(this.background);
            }
        }).bounds(this.width / 2 + 5, this.height / 6 + 96, 100, 20).build());

        this.yesButton.active = false;

        this.setButtonDelay(ExitConfirmation.CONFIG.confirmDelay.get());
    }

    @Override
    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        if (ExitConfirmation.CONFIG.dirtBackground.get()) {
            this.renderDirtBackground(gfx);
        } else if (ExitConfirmation.CONFIG.forceTransparentBackground.get()) {
            if (this.background != null) {
                gfx.pose().pushPose();
                gfx.pose().translate(0f, 0f, -10f);
                this.background.render(gfx, Integer.MAX_VALUE, Integer.MAX_VALUE, partialTicks);
                gfx.pose().popPose();
            }

            gfx.pose().pushPose();
            gfx.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            this.renderBackground(gfx);
        }

        gfx.drawCenteredString(this.font, this.title, this.width / 2, 70, 0xffffff);
        gfx.drawCenteredString(this.font, this.description, this.width / 2, 90, 0xbfbfbf);
        this.label.renderCentered(gfx, this.width / 2, 90);
        super.render(gfx, mouseX, mouseY, partialTicks);
        gfx.pose().popPose();
    }

    /**
     * Sets the number of ticks to wait before enabling the buttons.
     */
    public void setButtonDelay(int activateDelay) {
        this.activateDelay = activateDelay;
    }

    public void tick() {
        if (--this.activateDelay <= 0) {
            this.activateDelay = 0;
            this.activate();
        }
    }

    public void activate() {
        this.yesButton.active = true;
    }

    public void back() {
        Minecraft.getInstance().setScreen(this.background);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void onClose() {
        this.back();
    }
}
