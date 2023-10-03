package com.ultreon.mods.exitconfirmation.mixin;

import com.ultreon.bubbles.render.gui.GuiComponent;
import com.ultreon.bubbles.render.gui.widget.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Container.class)
public interface ContainerAccessor {
    @Accessor
    List<GuiComponent> getChildren();
}
