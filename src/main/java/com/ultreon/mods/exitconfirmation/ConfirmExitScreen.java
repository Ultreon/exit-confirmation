package com.ultreon.mods.exitconfirmation;

import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.resource.language.I18n;
import org.lwjgl.opengl.GL11;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class ConfirmExitScreen extends Screen {
    private static final String DESCRIPTION_ID = "screen.exit_confirm.description";
    private static final String TITLE_ID = "screen.exit_confirm.title";
    private static final String yesButtonTextId = "gui.yes";
    private static final String noButtonTextId = "gui.no";
    private final String yesButtonText;
    private final String noButtonText;
    private final Screen background;
    private int ticksUntilEnableIn;
    public boolean passStop;

    public ConfirmExitScreen(Screen background) {
        super();
        this.yesButtonText = I18n.translate(yesButtonTextId);
        this.noButtonText = I18n.translate(noButtonTextId);

        this.background = background;
    }

    @SuppressWarnings("unchecked")
    public void init() {
        super.init();

        this.buttons.add(new Button(0, this.width / 2 - 105, this.height / 6 + 96, 100, 20, this.yesButtonText));
        this.buttons.add(new Button(1, this.width / 2 + 5, this.height / 6 + 96, 100, 20, this.noButtonText));

        setButtonDelay(10);
    }

    private void yesClick(Button btn) {
        if (this.minecraft != null) {
            btn.active = false;
            if (this.minecraft.level != null && !this.minecraft.isConnectedToServer()) {
                WorldUtils.saveWorldThenQuitGame();
                return;
            }

            this.minecraft.stop();
        }
    }

    private void noClick(Button btn) {
        if (this.minecraft != null) {
            btn.active = false;
            this.minecraft.openScreen(background);
        }
    }

    @Override
    protected void buttonClicked(Button arg) {
        if (arg.id == 0) {
            yesClick(arg);
        } else if (arg.id == 1) {
            noClick(arg);
        }
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        if (background != null) {
            GL11.glTranslatef(0f, 0f, -10f);
            background.render(Integer.MAX_VALUE, Integer.MAX_VALUE, partialTicks);
            GL11.glTranslatef(0f, 0f, 10f);
        }

        this.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);

        drawTextWithShadowCentred(minecraft.textRenderer, I18n.translate(TITLE_ID), this.width / 2, 70, 0xffffff);
        drawTextWithShadowCentred(minecraft.textRenderer, I18n.translate(DESCRIPTION_ID), this.width / 2, 90, 0xbfbfbf);
        super.render(mouseX, mouseY, partialTicks);
    }

    /**
     * Sets the number of ticks to wait before enabling the buttons.
     */
    public void setButtonDelay(int ticksUntilEnableIn) {
        this.ticksUntilEnableIn = ticksUntilEnableIn;

        for (Object obj : buttons) {
            if (obj instanceof Button) {
                Button button = (Button) obj;
                button.active = false;
            }
        }
    }

    public void tick() {
        if (--ticksUntilEnableIn <= 0) {
            ticksUntilEnableIn = 0;

            for (Object obj : buttons) {
                if (obj instanceof Button) {
                    Button button = (Button) obj;
                    button.active = true;
                }
            }
        }
    }

    public void back() {
        ExitConfirmation.minecraft.openScreen(background);
    }

    @Override
    protected void keyPressed(char c, int i) {
        // Do not call super - Done to disable closing on [ESC].
    }
}
