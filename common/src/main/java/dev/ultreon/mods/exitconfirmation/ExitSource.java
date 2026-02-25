package dev.ultreon.mods.exitconfirmation;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;

public class ExitSource {
    private static GenericExitSource GENERIC = new GenericExitSource();

    public static WindowExitSource window(Window window) {
        return new WindowExitSource(window);
    }

    public static ButtonWidgetExitSource buttonWidget(Screen screen, Button button) {
        return new ButtonWidgetExitSource(screen, button);
    }

    public static ExitSource keyboard(int keyCode) {
        return new KeyboardExitSource(keyCode);
    }

    public static KeyboardInScreenExitSource keyboardInScreen(Screen screen, int keyCode) {
        return new KeyboardInScreenExitSource(screen, keyCode);
    }

    public static ConfirmExitScreenExitSource confirmScreen(ConfirmExitScreen screen) {
        return new ConfirmExitScreenExitSource(screen);
    }

    public static GenericExitSource generic() {
        return GENERIC;
    }

    public static class WindowExitSource extends ExitSource {
        private final Window window;

        protected WindowExitSource(Window window) {
            this.window = window;
        }

        public Window getWindow() {
            return this.window;
        }
    }

    public static class ButtonWidgetExitSource extends ExitSource {
        private final Screen screen;
        private final Button button;

        protected ButtonWidgetExitSource(Screen screen, Button button) {
            this.screen = screen;
            this.button = button;
        }

        public Screen getScreen() {
            return this.screen;
        }

        public Button getButton() {
            return this.button;
        }
    }

    public static class KeyboardExitSource extends ExitSource implements ClientKeyboardEvent {
        private final int keyCode;

        protected KeyboardExitSource(int keyCode) {
            this.keyCode = keyCode;
        }

        @Override
        public int getKeyCode() {
            return this.keyCode;
        }
    }

    public static class KeyboardInScreenExitSource extends KeyboardExitSource {
        private final Screen screen;

        protected KeyboardInScreenExitSource(Screen screen, int keyCode) {
            super(keyCode);
            this.screen = screen;
        }

        public Screen getScreen() {
            return this.screen;
        }
    }

    public static class GenericExitSource extends ExitSource {

    }

    public static class ConfirmExitScreenExitSource extends ExitSource {
        private final ConfirmExitScreen screen;

        protected ConfirmExitScreenExitSource(ConfirmExitScreen screen) {
            this.screen = screen;
        }

        public ConfirmExitScreen getScreen() {
            return this.screen;
        }
    }
}
