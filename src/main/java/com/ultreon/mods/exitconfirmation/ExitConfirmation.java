package com.ultreon.mods.exitconfirmation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;

@Mod(ExitConfirmation.MOD_ID)
public class ExitConfirmation {

    public static final String MOD_ID = "exit_confirm";
    private static boolean callbackSetup = false;
    private boolean escPress = false;

    // Directly reference a log4j logger.
    @SuppressWarnings("unused")
    static final Logger LOGGER = LogManager.getLogger();

    public ExitConfirmation() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        Config.initialize();
    }

    @SubscribeEvent
    public synchronized void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();

        if (event.getAction() == GLFW.GLFW_PRESS) {
            if (event.getKey() == 256 && Config.closePrompt.get() && Config.quitOnEscInTitle.get()) {
                if (!escPress) {
                    escPress = true;
                    if (mc.screen instanceof TitleScreen) {
                        mc.pushGuiLayer(new ConfirmExitScreen());
                    }
                }
            }
        }
        if (event.getAction() == GLFW.GLFW_RELEASE) {
            if (event.getKey() == 256) {
                escPress = false;
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

            if (Config.closePrompt.get()) {
                if (mc.level != null && !Config.closePromptInGame.get()) {
                    return;
                }
                event.setCanceled(true);
                if (!(mc.screen instanceof ConfirmExitScreen)) {
                    mc.pushGuiLayer(new ConfirmExitScreen());
                }
            }
        } else if (event.getSource() == WindowCloseEvent.Source.QUIT_BUTTON) {
            if (Config.closePrompt.get() && Config.closePromptQuitButton.get() && !(mc.screen instanceof ConfirmExitScreen)) {
                mc.pushGuiLayer(new ConfirmExitScreen());
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onTitleScreenInit(ScreenEvent.Init.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Screen screen = event.getScreen();
        if (screen instanceof TitleScreen titleScreen) {
            setupGLFWCallback(mc);
            overrideQuitButton(mc, titleScreen);
        }
    }

    private void overrideQuitButton(Minecraft mc, TitleScreen titleScreen) {
        List<? extends GuiEventListener> buttons = titleScreen.children();
        Optional<Button> quitButton = buttons.stream().filter(listener -> listener instanceof Button button && button.getMessage().equals(Component.translatable("menu.quit"))).map(listener -> (Button) listener).findFirst();
        quitButton.ifPresent(widget -> widget.onPress = (button) -> {
            boolean flag = MinecraftForge.EVENT_BUS.post(new WindowCloseEvent(WindowCloseEvent.Source.QUIT_BUTTON));
            if (!flag) {
                mc.stop();
            }
        });
    }

    @SuppressWarnings("resource")
    private void setupGLFWCallback(Minecraft mc) {
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
