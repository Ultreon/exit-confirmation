package com.ultreon.mods.exitconfirmation;

import com.ultreon.libs.events.v1.Event;
import com.ultreon.libs.events.v1.EventResult;

@FunctionalInterface
public interface AttemptExitEvent {
    Event<AttemptExitEvent> EVENT = Event.withResult(AttemptExitEvent.class);
    
    EventResult closing(CloseSource quitButton);
}