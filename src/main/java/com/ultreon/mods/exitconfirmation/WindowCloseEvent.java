package com.ultreon.mods.exitconfirmation;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

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

    public static boolean isInitialized() {
        return initialized;
    }
}
