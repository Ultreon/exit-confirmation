package dev.ultreon.mods.exitconfirm.platform.fabric;

//? fabric {

import dev.ultreon.mods.exitconfirm.ExitConfirmation;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ExitConfirmation.onInitializeClient();
	}

}
//?}
