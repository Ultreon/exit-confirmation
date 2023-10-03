package com.ultreon.mods.exitconfirmation;

import com.ultreon.bubbles.Axis2D;
import com.ultreon.bubbles.init.Fonts;
import com.ultreon.bubbles.render.Color;
import com.ultreon.bubbles.render.Renderer;
import com.ultreon.bubbles.render.gui.screen.Screen;
import com.ultreon.bubbles.render.gui.widget.Button;
import com.ultreon.libs.text.v1.TextObject;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public abstract class ConfirmScreen extends Screen {
    private final TextObject title;
    private final TextObject description;
    protected final TextObject yesButtonText;
    protected final TextObject noButtonText;
    protected final Screen background;
    protected Button yesButton;
    private int activateDelay;
    private Button noButton;

    protected ConfirmScreen(Screen background, TextObject title, TextObject description) {
        super(title);
        this.yesButtonText = ModTranslations.YES;
        this.noButtonText = ModTranslations.NO;

        this.title = title;
        this.description = description;

        this.background = background;
    }

    public abstract void yesButtonCallback();

    public void init() {
        this.clearWidgets();

        this.yesButton = this.add(Button.builder().text(this.yesButtonText).command(this::yesButtonCallback)
                .bounds(this.width / 2 - 105, this.height / 6 + 96, 100, 60).build());

        this.noButton = this.add(Button.builder().text(this.noButtonText).command(() -> {
            if (this.game != null) {
                this.noButton.enabled = false;
                this.game.showScreen(this.background);
            }
        }).bounds(this.width / 2 + 5, this.height / 6 + 96, 100, 60).build());

        this.yesButton.enabled = false;

        this.setButtonDelay(ExitConfirmation.CONFIG.confirmDelay.get());
    }

    @Override
    public void render(@NotNull Renderer renderer, int mouseX, int mouseY, float deltaTime) {
        if (ExitConfirmation.CONFIG.dirtBackground.get()) {
            this.renderBackground(renderer);
        } else if (ExitConfirmation.CONFIG.forceTransparentBackground.get()) {
            if (this.background != null) {
//                renderer.getMatrixStack().push();
                renderer.translate(0f, 0f, -10f);
                this.background.render(renderer, Integer.MAX_VALUE, Integer.MAX_VALUE, deltaTime);
//                renderer.getMatrixStack().pop();
            }

            renderer.fillGradient(0, 0, this.getWidth(), this.getHeight(), Color.BLACK.withAlpha(0x70), Color.BLACK.withAlpha(0x80), Axis2D.VERTICAL);
        } else {
            this.renderBackground(renderer);
        }

        renderer.drawTextCenter(Fonts.SANS_BOLD_40.get(), this.title, this.width / 2f, 70, Color.WHITE);
        renderer.drawTextCenter(this.font, this.description, this.width / 2f, 110, Color.grayscale(0xbf));

        this.renderChildren(renderer, mouseX, mouseY, deltaTime);
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
        this.yesButton.enabled = true;
    }

    public void back() {
        this.game.showScreen(this.background);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean close(Screen to) {
        if (to != this.background) {
            this.back();
            return true;
        }
        return false;
    }
}
