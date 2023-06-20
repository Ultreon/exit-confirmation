package com.ultreon.mods.exitconfirmation;

import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;

public interface WindowCloseEvent {
    Event<WindowCloseEvent> EVENT = EventFactory.createArrayBacked(WindowCloseEvent.class,
        (listeners) -> (source) -> {
            for (WindowCloseEvent listener : listeners) {
                ActionResult result = listener.closing(source);
 
                if(result != ActionResult.PASS) {
                    return result;
                }
            }
 
        return ActionResult.PASS;
    });
 
    ActionResult closing(Source source);

    enum Source {
        GENERIC,
        QUIT_BUTTON
    }
}