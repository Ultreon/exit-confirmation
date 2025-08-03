package com.ultreon.mods.exitconfirmation.core;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.intellij.lang.annotations.Language;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.7.10")
public class Coremod implements IFMLLoadingPlugin {
    @Override
    public @Language("jvm-class-name") String[] getASMTransformerClass() {
        return new String[]{
                "com.ultreon.mods.exitconfirmation.core.ShutdownTransformer",
                "com.ultreon.mods.exitconfirmation.core.MinecraftInitTransformer"
        };
    }

    @Override
    public @Language("jvm-class-name") String getModContainerClass() {
        return "com.ultreon.mods.exitconfirmation.core.ExitConfirmModContainer";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
