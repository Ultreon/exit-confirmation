package com.ultreon.mods.exitconfirmation.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {
    @Accessor("instance")
    static Minecraft getInstance() {
        throw new AssertionError();
    }
}
