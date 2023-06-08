package com.ultreon.mods.exitconfirmation.forge;

import com.ultreon.mods.exitconfirmation.CloseSource;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("unused")
@Cancelable
@Mod.EventBusSubscriber(modid = ExitConfirmationForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class WindowCloseEvent extends Event {
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
