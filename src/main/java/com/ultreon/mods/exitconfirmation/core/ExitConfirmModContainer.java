package com.ultreon.mods.exitconfirmation.core;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import org.apache.logging.log4j.Level;

import java.util.Collections;

public class ExitConfirmModContainer extends DummyModContainer {
    private boolean enabled;
    private EventBus eventBus;
    private LoadController controller;

    public ExitConfirmModContainer() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "exit_confirm";
        meta.name = "Exit Confirmation";
        meta.version = "0.1.0-mc.1.7.10";
        meta.authorList = Collections.singletonList("Ultreon Studios");
        meta.description = "Shows a exit confirmation screen when trying to close Minecraft.";
        meta.url = "https://github.com/Ultreon/exit-confirmation/";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        if (this.enabled)
        {
            FMLLog.log(getModId(), Level.DEBUG, "Enabling mod %s", getModId());
            this.eventBus = bus;
            this.controller = controller;
            eventBus.register(this);
            return true;
        }
        else
        {
            return false;
        }
    }

    @Subscribe
    public void onConstruct(FMLConstructionEvent event) {

    }
}