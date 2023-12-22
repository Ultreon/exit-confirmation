package com.ultreon.mods.exitconfirmation.neoforge;

import com.ultreon.mods.exitconfirmation.CloseSource;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class WindowCloseEvent extends Event implements ICancellableEvent {
    private static boolean initialized;
    private final CloseSource source;

    public WindowCloseEvent(CloseSource source) {
        this.source = source;
    }

    public CloseSource getSource() {
        return this.source;
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
