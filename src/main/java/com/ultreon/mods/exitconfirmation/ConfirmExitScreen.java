package com.ultreon.mods.exitconfirmation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.text2speech.Narrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.NarratorStatus;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class ConfirmExitScreen extends Screen {
    private static final Component DESCRIPTION = Component.translatable("screen.exit_confirm.description");
    private static final Component TITLE = Component.translatable("screen.exit_confirm.title");
    private final MultiLineLabel label = MultiLineLabel.EMPTY;
    private final Component yesButtonText;
    private final Component noButtonText;
    private final Screen background;

    public ConfirmExitScreen(Screen background) {
        super(TITLE);
        this.yesButtonText = CommonComponents.GUI_YES;
        this.noButtonText = CommonComponents.GUI_NO;

        this.background = background;
    }

    protected void init() {
        super.init();

        NarratorStatus narratorStatus = Objects.requireNonNull(this.minecraft).options.narrator().get();

        if (narratorStatus == NarratorStatus.SYSTEM || narratorStatus == NarratorStatus.ALL) {
            Narrator.getNarrator().say("Are you sure you want to exit Minecraft?", true);
        }

        this.clearWidgets();

        this.addRenderableWidget(new Button(this.width / 2 - 105, this.height / 6 + 96, 100, 20, this.yesButtonText, (btn) -> {
            if (this.minecraft != null) {
                btn.active = false;
                if (this.minecraft.level != null && this.minecraft.isLocalServer()) {
                    WorldUtils.saveWorldThenQuitGame();
                    return;
                }

                this.minecraft.stop();
            }
        }));
        this.addRenderableWidget(new Button(this.width / 2 + 5, this.height / 6 + 96, 100, 20, this.noButtonText, (btn) -> {
            if (this.minecraft != null) {
                btn.active = false;
                this.minecraft.setScreen(background);
            }
        }));

        setButtonDelay(10);
    }

    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        if (background != null) {
            pose.pushPose();
            pose.translate(0f, 0f, -10f);
            background.render(pose, Integer.MAX_VALUE, Integer.MAX_VALUE, partialTicks);
            pose.popPose();
        }

        pose.pushPose();
        this.fillGradient(pose, 0, 0, this.width, this.height, -1072689136, -804253680);

        drawCenteredString(pose, this.font, this.title, this.width / 2, 70, 0xffffff);
        drawCenteredString(pose, this.font, DESCRIPTION, this.width / 2, 90, 0xbfbfbf);
        this.label.renderCentered(pose, this.width / 2, 90);
        super.render(pose, mouseX, mouseY, partialTicks);
        pose.popPose();
    }

    /**
     * Sets the number of ticks to wait before enabling the buttons.
     */
    public void setButtonDelay(int ticksUntilEnableIn) {

    }

    public void tick() {

    }

    public void back() {
        Minecraft.getInstance().setScreen(background);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void onClose() {
        back();
    }
}
