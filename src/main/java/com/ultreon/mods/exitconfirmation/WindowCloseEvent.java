package com.ultreon.mods.exitconfirmation;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SuppressWarnings("unused")
@Cancelable
@Mod.EventBusSubscriber(modid = ExitConfirmation.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class WindowCloseEvent extends Event {
    private static boolean initialized;
    private final Source source;

    public WindowCloseEvent(Source source) {
        this.source = source;
    }

    public Source getSource() {
        return source;
    }

    public enum Source {
        QUIT_BUTTON,
        GENERIC
    }

    @SubscribeEvent
    public static void onOptionsScreenInit(ScreenEvent.InitScreenEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Screen screen = event.getScreen();
        if (screen instanceof TitleScreen titleScreen) {
            if (!initialized) {
                long handle = mc.getWindow().getWindow();
                GLFW.glfwSetWindowCloseCallback(handle, window -> {
                    boolean flag = MinecraftForge.EVENT_BUS.post(new WindowCloseEvent(Source.GENERIC));
                    if (flag) {
                        GLFW.glfwSetWindowShouldClose(window, false);
                    }
                });
            }
            List<? extends GuiEventListener> buttons = titleScreen.children();
            if (buttons.size() >= 2) {
                if (buttons.get(buttons.size() - 2) instanceof Button widget) {
                    widget.onPress = (button) -> {
                        boolean flag = MinecraftForge.EVENT_BUS.post(new WindowCloseEvent(Source.QUIT_BUTTON));
                        if (!flag) {
                            mc.stop();
                        }
                    };
                    initialized = true;
                }
            }
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
