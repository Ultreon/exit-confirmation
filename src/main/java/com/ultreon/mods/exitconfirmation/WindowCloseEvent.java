package com.ultreon.mods.exitconfirmation;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.awt.*;

public interface WindowCloseEvent {
    Event<WindowCloseEvent> EVENT = EventFactory.createArrayBacked(WindowCloseEvent.class,
        (listeners) -> (window, source) -> {
            for (WindowCloseEvent listener : listeners) {
                ActionResult result = listener.closing(window, source);
 
                if(result != ActionResult.PASS) {
                    return result;
                }
            }
 
        return ActionResult.PASS;
    });
 
    ActionResult closing(Frame window, Source source);

    enum Source {
        GENERIC,
        QUIT_BUTTON
    }
}