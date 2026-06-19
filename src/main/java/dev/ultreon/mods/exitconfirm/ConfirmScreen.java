package dev.ultreon.mods.exitconfirm;

import com.mojang.text2speech.Narrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.NarratorStatus;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//? if < 1.20.1 {
/*import com.mojang.blaze3d.vertex.PoseStack;
*///?} else >= 1.20.1 && < 26.1 {
import net.minecraft.client.gui.GuiGraphics;
//? } else >= 26.1 {
/*import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
*///? }

public abstract class ConfirmScreen extends Screen {
    private final Component title;
    private final Component description;
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
			//? >= 1.19.2 {
            NarratorStatus narratorStatus = Objects.requireNonNull(this.minecraft).options.narrator().get();
			//? } else {
			/*NarratorStatus narratorStatus = Objects.requireNonNull(this.minecraft).options.narratorStatus;
			*///? }

            if (narratorStatus == NarratorStatus.SYSTEM || narratorStatus == NarratorStatus.ALL) {
				//? <1.21.7 {
				/*Narrator.getNarrator().say(this.description.getString(), true);
				*///?} else {
				Narrator.getNarrator().say(this.description.getString(), true, 1f);
				//?}
            }
        }

        this.clearWidgets();

		//? < 1.19.4 {
        /*this.yesButton = this.addRenderableWidget(new Button(this.width / 2 - 105, this.height / 6 + 96, 100, 20, this.yesButtonText, this::yesButtonCallback));

        this.addRenderableWidget(new Button(this.width / 2 + 5, this.height / 6 + 96, 100, 20, this.noButtonText, (btn) -> {
            if (this.minecraft != null) {
                btn.active = false;
                this.minecraft.setScreen(this.background);
            }
        }));
		*///? } else {
        this.yesButton = this.addRenderableWidget(Button.builder(this.yesButtonText, this::yesButtonCallback)
                .bounds(this.width / 2 - 105, this.height / 6 + 96, 100, 20).build());

        this.addRenderableWidget(Button.builder(this.noButtonText, (btn) -> {
            if (this.minecraft != null) {
                btn.active = false;
				//? < 26.2 {
                this.minecraft.setScreen(this.background);
				//? } else {
				/*this.minecraft.gui.setScreen(this.background);
				*///? }
            }
        }).bounds(this.width / 2 + 5, this.height / 6 + 96, 100, 20).build());
		//? }

        this.yesButton.active = false;

        this.setButtonDelay(ExitConfirmation.CONFIG.confirmDelay.get());
    }

	//? < 1.20.1 {


	/*@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);

		super.render(poseStack, mouseX, mouseY, partialTicks);

		drawCenteredString(poseStack, this.font, this.title, this.width / 2, 70, 0xffffff);
		drawCenteredString(poseStack, this.font, this.description, this.width / 2, 90, 0xbfbfbf);
	}

	*///?} else >= 1.20.1 && < 1.21.1 {

	/*@Override
	public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
		//? < 1.20.4 {
		/^this.renderBackground(gfx);
		^///? } else {
		this.renderBackground(gfx, mouseX, mouseY, partialTicks);
		//? }

		super.render(gfx, mouseX, mouseY, partialTicks);

		gfx.drawCenteredString(this.font, this.title, this.width / 2, 70, 0xffffff);
		gfx.drawCenteredString(this.font, this.description, this.width / 2, 90, 0xbfbfbf);
	}

	*///?} else < 1.21.7 {

	/*@Override
	public void renderBackground(GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
		if (ExitConfirmation.CONFIG.forceEmptyBackground.get()) {
			assert this.minecraft != null;
			if (this.minecraft.level == null) {
				this.renderPanorama(gfx, partialTicks);
			}

			this.renderBlurredBackground(partialTicks);
			this.renderMenuBackground(gfx);
		} else if (ExitConfirmation.CONFIG.forceTransparentBackground.get()) {
			if (this.background != null) {
				gfx.pose().pushPose();
				gfx.pose().translate(0f, 0f, -1000f);
				this.background.render(gfx, Integer.MAX_VALUE, Integer.MAX_VALUE, partialTicks);
				gfx.pose().popPose();
			}

			this.renderBlurredBackground(partialTicks);
			this.renderMenuBackground(gfx);
		} else {
			super.renderBackground(gfx, mouseX, mouseX, partialTicks);
		}
	}

	@Override
	public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
		super.render(gfx, mouseX, mouseY, partialTicks);

		gfx.drawCenteredString(this.font, this.title, this.width / 2, 70, 0xffffff);
		gfx.drawCenteredString(this.font, this.description, this.width / 2, 90, 0xbfbfbf);
	}

	*///?} else >= 1.21.7 && < 26.1 {

	@Override
	public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
		super.render(gfx, mouseX, mouseY, partialTicks);

		gfx.drawCenteredString(this.font, this.title, this.width / 2, 70, 0xffffffff);
		gfx.drawCenteredString(this.font, this.description, this.width / 2, 90, 0xffbfbfbf);
	}

	//?} else >= 26.1 {

	/*@Override
	public void extractBackground(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float partialTicks) {
		if (ExitConfirmation.CONFIG.forceEmptyBackground.get()) {
			assert this.minecraft != null;
			if (this.minecraft.level == null) {
				this.extractPanorama(gfx, partialTicks);
			}

			this.extractBlurredBackground(gfx);
			this.extractMenuBackground(gfx);
		} else {
			super.extractBackground(gfx, mouseX, mouseX, partialTicks);
		}
	}

	@Override
	public void extractRenderState(@NotNull GuiGraphicsExtractor gfx, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(gfx, mouseX, mouseY, partialTicks);

		this.visitText(gfx.textRenderer(GuiGraphicsExtractor.HoveredTextEffects.TOOLTIP_AND_CURSOR));
	}

	private void visitText(ActiveTextCollector activeTextCollector) {
		activeTextCollector.accept(TextAlignment.CENTER, this.width / 2, 70, this.title);
		activeTextCollector.accept(TextAlignment.CENTER, this.width / 2, 90, this.description.copy().withStyle(style -> style.withColor(0xbfbfbf)));
	}

	*///?}

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
		//? >= 26.2 {
        /*Minecraft.getInstance().gui.setScreen(this.background);
		*///? } else {
		Minecraft.getInstance().setScreen(this.background);
		//? }
	}

    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void onClose() {
        this.back();
    }
}
