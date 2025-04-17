package com.ultreon.mods.exitconfirmation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class WindowCloseEvent {
    public static final Event<WindowClosing> EVENT = EventFactory.createArrayBacked(WindowClosing.class, callbacks -> source -> {
        for (WindowClosing callback : callbacks) {
            ActionResult closing = callback.closing(source);
            if (closing == ActionResult.CANCEL) {
                return ActionResult.CANCEL;
            } else if (closing == ActionResult.INTERRUPT) {
                return ActionResult.PASS;
            }
        }

        return ActionResult.PASS;
    });

    @FunctionalInterface
    public interface WindowClosing {
        ActionResult closing(Source source);
    }

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
