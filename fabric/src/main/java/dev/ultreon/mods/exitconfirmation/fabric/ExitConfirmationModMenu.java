package dev.ultreon.mods.exitconfirmation.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.ultreon.mods.exitconfirmation.config.gui.ConfigScreen;


public class ExitConfirmationModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigScreen::new;
    }
}
