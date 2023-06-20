package com.ultreon.mods.exitconfirmation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ModEvents {
    private boolean escPress;

    @SubscribeEvent
    public synchronized void onKeyInput(GuiScreenEvent.KeyboardInputEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();

        boolean state = Keyboard.getEventKeyState();
        int key = Keyboard.getEventKey();
        if (state) {
            if (key == Keyboard.KEY_ESCAPE && Config.CLOSE_PROMPT.getBoolean() && Config.QUIT_ON_ESC_IN_TITLE.getBoolean()) {
                if (!this.escPress) {
                    this.escPress = true;
                    if (mc.currentScreen instanceof GuiMainMenu) {
                        mc.displayGuiScreen(new ConfirmExitScreen(mc.currentScreen));
                    }
                }
            }
        } else {
            if (key == 256) {
                this.escPress = false;
            }
        }
    }

    @SubscribeEvent
    public void onWindowClose(WindowCloseEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (event.getSource() == WindowCloseEvent.Source.GENERIC) {
            if (mc.level == null && mc.screen == null) {
                event.setCanceled(true);
                return;
            }

            if (mc.screen instanceof LevelLoadingScreen) {
                event.setCanceled(true);
                return;
            }

            if (Config.CLOSE_PROMPT.getBoolean()) {
                if (mc.theWorld != null && !Config.CLOSE_PROMPT_IN_GAME.getBoolean()) {
                    return;
                }
                event.setCanceled(true);
                if (!(mc.currentScreen instanceof ConfirmExitScreen)) {
                    mc.displayGuiScreen(new ConfirmExitScreen(mc.currentScreen));
                }
            }
        } else if (event.getSource() == WindowCloseEvent.Source.QUIT_BUTTON) {
            if (Config.CLOSE_PROMPT.getBoolean() && Config.CLOSE_PROMPT_QUIT_BUTTON.getBoolean() && !(mc.currentScreen instanceof ConfirmExitScreen)) {
                mc.displayGuiScreen(new ConfirmExitScreen(mc.currentScreen));
            }
        }
    }

    @SubscribeEvent
    public static void onTitleScreenInit(GuiScreenEvent.InitGuiEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen screen = event.gui;
        if (screen instanceof GuiMainMenu) {
            GuiMainMenu mainMenu = (GuiMainMenu) screen;
            setupGLFWCallback(mc);
            overrideQuitButton(mc, mainMenu);
        }
    }

    private static void overrideQuitButton(Minecraft mc, GuiMainMenu mainMenu) {
        ReflectionHelper.findField(mainMenu, "")
        List<? extends GuiEventListener> buttons = mainMenu.children();
        if (buttons.size() >= 2) {
            if (buttons.get(buttons.size() - 2) instanceof Button widget) {
                widget.onPress = (button) -> {
                    boolean flag = MinecraftForge.EVENT_BUS.post(new WindowCloseEvent(WindowCloseEvent.Source.QUIT_BUTTON));
                    if (!flag) {
                        mc.stop();
                    }
                };
            }
        }
    }

    @SuppressWarnings("resource")
    private static void setupGLFWCallback(Minecraft mc) {
        if (!callbackSetup) {
            long handle = mc.getWindow().getWindow();
            GLFW.glfwSetWindowCloseCallback(handle, window -> {
                boolean flag = MinecraftForge.EVENT_BUS.post(new WindowCloseEvent(WindowCloseEvent.Source.GENERIC));
                if (flag) {
                    GLFW.glfwSetWindowShouldClose(window, false);
                }
            });
            callbackSetup = true;
        }
    }
}
