package dev.ultreon.mods.exitconfirm.platform.fabric;

//? fabric {

import dev.ultreon.mods.exitconfirm.ExitConfirmation;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		ExitConfirmation.onInitialize();
	}
}
//?}
