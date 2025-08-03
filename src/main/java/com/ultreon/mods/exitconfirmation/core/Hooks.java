package com.ultreon.mods.exitconfirmation.core;

import com.ultreon.mods.exitconfirmation.ExitConfirmation;
import com.ultreon.mods.exitconfirmation.WindowCloseEvent;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("unused")
public class Hooks {
    public static boolean allowShutdown() {
        if (ExitConfirmation.allowExit) return true;
        Thread.dumpStack();
        return !MinecraftForge.EVENT_BUS.post(new WindowCloseEvent(WindowCloseEvent.Source.QUIT_BUTTON));
    }
}
