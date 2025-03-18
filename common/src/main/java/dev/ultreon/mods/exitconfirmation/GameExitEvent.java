package dev.ultreon.mods.exitconfirmation;

import dev.ultreon.mods.xinexlib.client.event.ClientEvent;
import dev.ultreon.mods.xinexlib.event.system.Cancelable;
import net.minecraft.client.Minecraft;

public class GameExitEvent implements ClientEvent, Cancelable {
    private final ExitSource source;
    private final Minecraft client;
    private boolean canceled = false;

    public GameExitEvent(ExitSource source, Minecraft client) {
        this.source = source;
        this.client = client;
    }

    public ExitSource getSource() {
        return this.source;
    }

    @Override
    public boolean isCanceled() {
        return this.canceled;
    }

    public boolean canBeCanceled() {
        return true;
    }

    @Override
    public void cancel() {
        this.canceled = true;
    }

    @Override
    public Minecraft getClient() {
        return this.client;
    }
}
