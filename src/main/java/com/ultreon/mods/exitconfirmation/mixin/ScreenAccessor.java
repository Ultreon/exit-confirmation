package com.ultreon.mods.exitconfirmation.mixin;

import net.minecraft.client.gui.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Screen.class)
public interface ScreenAccessor {
    @SuppressWarnings("rawtypes")
    @Accessor("buttons")
    List getButtons();
}
