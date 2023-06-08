package com.ultreon.mods.exitconfirmation.fabric;

import com.mojang.blaze3d.platform.Window;
import com.ultreon.mods.exitconfirmation.ActionResult;
import com.ultreon.mods.exitconfirmation.CloseSource;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

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
 
    ActionResult closing(Window window, CloseSource source);
}