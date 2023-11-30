package com.ultreon.mods.exitconfirmation;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.text2speech.Narrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.IBidiRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.settings.NarratorStatus;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ExitConfirmation.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ConfirmExitScreen extends Screen {
    private static final ITextComponent DESCRIPTION = new TranslationTextComponent("screen.exit_confirm.description");
    private static final ITextComponent TITLE = new TranslationTextComponent("screen.exit_confirm.title");
    private final IBidiRenderer label = IBidiRenderer.EMPTY;
    private final ITextComponent yesButtonText;
    private final ITextComponent noButtonText;
    private Button button;
    private int ticksUntilEnable;

    public ConfirmExitScreen() {
        super(TITLE);
        this.yesButtonText = DialogTexts.GUI_YES;
        this.noButtonText = DialogTexts.GUI_NO;
    }

    protected void init() {
        super.init();

        NarratorStatus narratorStatus = Objects.requireNonNull(this.minecraft).options.narratorStatus;

        if (narratorStatus == NarratorStatus.SYSTEM || narratorStatus == NarratorStatus.ALL) {
            Narrator.getNarrator().say("Are you sure you want to exit Minecraft?", true);
        }

        this.children.clear();

        this.button = this.addButton(new Button(this.width / 2 - 105, this.height / 6 + 96, 100, 20, this.yesButtonText, (btn) -> {
            if (this.minecraft != null) {
                btn.active = false;
                if (this.minecraft.level != null && this.minecraft.isLocalServer()) {
                    WorldUtils.saveWorldThenQuitGame();
                    return;
                }

                ExitConfirmation.didAccept = true;

                this.minecraft.stop();
            }
        }));
        this.addButton(new Button(this.width / 2 + 5, this.height / 6 + 96, 100, 20, this.noButtonText, (btn) -> {
            if (this.minecraft != null) {
                btn.active = false;
                this.minecraft.popGuiLayer();
            }
        }));

        setButtonDelay(10);
    }

    public void render(@NotNull MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        matrices.translate(0f, 0f, 400f);
        this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);
        MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.BackgroundDrawnEvent(this, matrices));

        drawCenteredString(matrices, this.font, this.title, this.width / 2, 70, 0xffffff);
        drawCenteredString(matrices, this.font, DESCRIPTION, this.width / 2, 90, 0xbfbfbf);
        this.label.renderCentered(matrices, this.width / 2, 90);
        super.render(matrices, mouseX, mouseY, partialTicks);
    }

    /**
     * Sets the number of ticks to wait before enabling the buttons.
     */
    public void setButtonDelay(int ticksUntilEnableIn) {
        this.button.active = false;
        this.ticksUntilEnable = ticksUntilEnableIn;
    }

    public void tick() {
        if (this.ticksUntilEnable-- <= 0) {
            this.button.active = true;
            this.ticksUntilEnable = 0;
        }
    }

    public void back() {
        Minecraft.getInstance().popGuiLayer();
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void onClose() {
        back();
    }
}
