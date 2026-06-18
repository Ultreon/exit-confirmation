package dev.ultreon.mods.exitconfirm;

import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;

//? < 1.19.2 {
/*import net.minecraft.network.chat.TranslatableComponent;
*///? }
//? >= 1.21.1 {
import net.minecraft.client.gui.screens.GenericMessageScreen;
//? } else {
/*import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
*///? }

public final class WorldUtils {
	//? < 1.19.2 {
    /*private static final Component SAVING_LEVEL = new TranslatableComponent("menu.savingLevel");
	*///? } else {
	private static final Component SAVING_LEVEL = Component.translatable("menu.savingLevel");
	//? }

    public static void saveWorldThenOpenTitle() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            boolean local = mc.isLocalServer();
			ServerData serverData = mc.getCurrentServer();
			//? if < 1.20.4 {
			/*boolean realms = mc.isConnectedToRealms();
			*///? } else {
			boolean realms = serverData.isRealm();
			//? }
			//? >=1.21.7 {
			mc.level.disconnect(Component.translatable("disconnect.disconnected"));
			//?} else < 1.21.7 {
			/*mc.level.disconnect();
			*///?}

			//? if <1.20.4 {
			/*mc.clearLevel(new GenericDirtMessageScreen(SAVING_LEVEL));
			*///?} >= 1.20.4 && < 1.21.7 {
			/*if (local) {
				//? > 1.20.4 {
				/^mc.disconnect(new GenericMessageScreen(SAVING_LEVEL));
				^///? } else {
				mc.disconnect(new GenericDirtMessageScreen(SAVING_LEVEL));
				//? }
			} else {
				mc.disconnect();
			}
			*///?} else >= 1.21.7 && < 26.1 {
			mc.disconnect(new GenericMessageScreen(SAVING_LEVEL), local);
			//?} else >= 26.1 {
			/*if (local) {
				mc.disconnectWithSavingScreen();
			} else {
				mc.disconnectWithProgressScreen();
			}
			*///?}

            TitleScreen titleScreen = new TitleScreen();
			//? >= 26.2 {
			/*if (local) {
				mc.gui.setScreen(titleScreen);
			} else if (serverData != null && realms) {
				mc.gui.setScreen(new RealmsMainScreen(titleScreen));
			} else {
				mc.gui.setScreen(new JoinMultiplayerScreen(titleScreen));
			}
			*///? } else {
			if (local) {
				mc.setScreen(titleScreen);
			} else if (serverData != null && realms) {
				mc.setScreen(new RealmsMainScreen(titleScreen));
			} else {
				mc.setScreen(new JoinMultiplayerScreen(titleScreen));
			}
			//? }
        }
    }

    public static void saveWorldThen(Runnable runnable) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
			boolean local = mc.isLocalServer();
			ServerData serverData = mc.getCurrentServer();
			//? if < 1.20.4 {
			/*boolean realms = mc.isConnectedToRealms();
			*///? } else {
			boolean realms = serverData.isRealm();
			 //? }
			//? >=1.21.7 {
			mc.level.disconnect(Component.translatable("disconnect.disconnected"));
			 //?} else < 1.21.7 {
			/*mc.level.disconnect();
			*///?}
			//? if <1.20.4 {
			/*mc.clearLevel(new GenericDirtMessageScreen(SAVING_LEVEL));
			*///?} >= 1.20.4 && < 1.21.7 {
			/*if (local) {
				//? > 1.20.4 {
				/^mc.disconnect(new GenericMessageScreen(SAVING_LEVEL));
				 ^///? } else {
				mc.disconnect(new GenericDirtMessageScreen(SAVING_LEVEL));
				//? }
			} else {
				mc.disconnect();
			}
			*///?} else >= 1.21.7 {
			mc.disconnect(new GenericMessageScreen(SAVING_LEVEL), local);
			 //?}

            runnable.run();
        }
    }

    public static void saveWorldThenOpen(Screen screen) {
		//? >= 26.2 {
        /*saveWorldThen(() -> Minecraft.getInstance().gui.setScreen(screen));
		*///? } else {
		saveWorldThen(() -> Minecraft.getInstance().setScreen(screen));
		//? }
    }

    public static void saveWorldThenQuitGame() {
        saveWorldThen(() -> Minecraft.getInstance().stop());
    }
}
