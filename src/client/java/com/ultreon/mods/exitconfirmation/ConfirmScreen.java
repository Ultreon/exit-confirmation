package com.ultreon.mods.exitconfirmation;

import dev.ultreon.quantum.client.gui.screens.Screen;
import dev.ultreon.quantum.client.gui.widget.button.ButtonType;
import dev.ultreon.quantum.client.gui.widget.button.TextButton;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public abstract class ConfirmScreen extends Screen {
    private final String title;
    private final String description;
    private String label = "";
    protected final String yesButtonText;
    protected final String noButtonText;
    protected final Screen background;
    protected TextButton yesButton;
    private int activateDelay;

    protected ConfirmScreen(Screen background, String title, String description) {
        super(background);
        this.yesButtonText = "Yes";
        this.noButtonText = "No";

        this.title = title;
        this.description = description;

        this.background = background;
    }

    public abstract void yesButtonCallback(TextButton btn);

    @Override
    public void setup() {
        this.yesButton = this.add(new TextButton(this, ButtonType.Normal)
                .callback(this::yesButtonCallback)
                .position(() -> new Position(this.getWidth() / 2 - 105, this.getHeight() / 6 + 96)));

        this.add(TextButton.of(this.noButtonText, 100, 20)
                .callback((btn) -> {
                    if (this.client != null) {
                        btn.enabled = false;
                        this.client.showScreen(this.background);
                    }
                }).position(() -> new Position(this.getWidth() / 2 + 5, this.getHeight() / 6 + 96)));

//        this.yesButton.enabled = false;

        this.setButtonDelay(ExitConfirmation.CONFIG.confirmDelay.get());
    }

    @Override
    public void renderWidget(@NotNull Renderer renderer, int mouseX, int mouseY, float deltaTime) {
        renderer.pushMatrix();
        if (ExitConfirmation.CONFIG.solidBackgroumd.get()) {
            this.renderSolidBackground(renderer);
        } else if (ExitConfirmation.CONFIG.forceTransparentBackground.get()) {
            if (this.background != null) {
                renderer.pushMatrix();
                renderer.translate(0f, 0f, -1000f);
                this.background.render(renderer, Integer.MAX_VALUE, Integer.MAX_VALUE, deltaTime);
                renderer.popMatrix();
            }

            this.renderTransparentBackground(renderer);
        } else {
            this.renderBackground(renderer);
        }
        renderer.popMatrix();

        renderer.textCenter(this.title, this.getWidth() / 2, 70, Color.rgb(0xffffff));
        renderer.textCenter(this.description, this.getWidth() / 2, 90, Color.rgb(0xbfbfbf));
        renderer.textCenter(this.label, this.getWidth() / 2, 90);
        super.renderWidget(renderer, mouseX, mouseY, deltaTime);
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
}
