package com.ultreon.mods.exitconfirmation;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("unused")
@Cancelable
@SideOnly(Side.CLIENT)
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
