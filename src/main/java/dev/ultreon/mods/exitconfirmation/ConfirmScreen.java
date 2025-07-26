package dev.ultreon.mods.exitconfirmation;

import dev.ultreon.quantum.client.gui.Renderer;
import dev.ultreon.quantum.client.gui.Screen;
import dev.ultreon.quantum.client.gui.widget.TextButton;
import dev.ultreon.quantum.text.TextObject;
import dev.ultreon.quantum.util.RgbColor;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public abstract class ConfirmScreen extends Screen {
    private final TextObject title;
    private final TextObject description;
    private TextObject label = TextObject.empty();
    protected final TextObject yesButtonText;
    protected final TextObject noButtonText;
    protected final Screen background;
    protected TextButton yesButton;
    protected TextButton noButton;
    private int activateDelay;

    protected ConfirmScreen(Screen background, TextObject title, TextObject description) {
        super(title);
        this.yesButtonText = TextObject.translation("quantum.ui.yes");
        this.noButtonText = TextObject.translation("quantum.ui.no");

        this.title = title;
        this.description = description;

        this.background = background;
    }

    public abstract void yesButtonCallback(TextButton btn);

    @Override
    protected void init() {
        this.yesButton = this.add(TextButton.of(this.yesButtonText, 100, 20)
                .withCallback(this::yesButtonCallback));

        this.noButton = this.add(TextButton.of(this.noButtonText, 100, 20)
                .withCallback(this::noButtonCallback));

        this.setButtonDelay(ExitConfig.confirmDelay);
    }

    @Override
    public void resized(int width, int height) {
        super.resized(width, height);

        this.yesButton.setPos(this.size.width / 2 - 105, this.size.height / 6 + 96);
        this.noButton.setPos(this.size.width / 2 + 5, this.size.height / 6 + 96);
    }

    @Override
    public void renderWidget(@NotNull Renderer renderer, float deltaTime) {
        renderer.pushMatrix();
        if (ExitConfig.solidBackground) {
            this.renderSolidBackground(renderer);
        } else if (ExitConfig.forceTransparentBackground) {
            if (this.background != null) {
                renderer.pushMatrix();
                renderer.translate(0f, 0f, -1000f);
                this.background.render(renderer, deltaTime);
                renderer.popMatrix();
            }

            this.renderTransparentBackground(renderer);
        } else {
            this.renderBackground(renderer);
        }
        renderer.popMatrix();

        renderer.textCenter(this.title, this.getWidth() / 2, 70, RgbColor.rgb(0xffffff));
        renderer.textCenter(this.description, this.getWidth() / 2, 90, RgbColor.rgb(0xbfbfbf));
        renderer.textCenter(this.label, this.getWidth() / 2, 90);
        super.render(renderer, deltaTime);
    }

    /**
     * Sets the number of ticks to wait before enabling the buttons.
     */
    public void setButtonDelay(int activateDelay) {
        this.activateDelay = activateDelay;
    }

    @Override
    public void tick() {
        if (--this.activateDelay <= 0) {
            this.activateDelay = 0;
            this.activate();
        }
    }

    public void activate() {
        this.yesButton.enabled = true;
    }

    public boolean canCloseWithEsc() {
        return false;
    }

    private void noButtonCallback(TextButton btn) {
        if (this.client != null) {
            btn.enabled = false;
            this.client.showScreen(this.background);
        }
    }
}
