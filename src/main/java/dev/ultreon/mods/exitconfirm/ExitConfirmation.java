package dev.ultreon.mods.exitconfirm;

import dev.ultreon.mods.exitconfirm.config.Config;
import dev.ultreon.mods.exitconfirm.platform.Platform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? < 1.19.2 {
//? }
//? fabric {
import dev.ultreon.mods.exitconfirm.platform.fabric.FabricPlatform;
//?} neoforge {
/*import dev.ultreon.mods.exitconfirm.platform.neoforge.NeoforgePlatform;
 *///?} forge {
/*import dev.ultreon.mods.exitconfirm.platform.forge.ForgePlatform;
 *///?}

@SuppressWarnings("LoggingSimilarMessage")
public class ExitConfirmation {
	public static final Config CONFIG = new Config();
	public static final String MOD_ID = /*$ mod_id*/ "exitconfirm";
	public static final String MOD_VERSION = /*$ mod_version*/ "4.0.0";
	public static final String MOD_FRIENDLY_NAME = /*$ mod_name*/ "Exit Confirmation";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Platform PLATFORM = createPlatformInstance();
	private static boolean shouldClose;
	private static boolean callbackSetUp;

	public static void onInitialize() {
		LOGGER.info("Initializing {} on {}", MOD_ID, ExitConfirmation.xplat().loader());
		LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
	}

	public static void onInitializeClient() {
		Config.load();
		Config.save();

		LOGGER.info("Initializing {} Client on {}", MOD_ID, ExitConfirmation.xplat().loader());
		LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
	}

	@ApiStatus.Internal
	public static void onTitleScreenInit(Minecraft client, Screen screen) {
		// Only if it's the title screen.
		if (screen instanceof TitleScreen titleScreen) {
			// Set everything up.
			setupGLFWCallback(client);
		}
	}

	/**
	 * Sets up the {@link GLFW#glfwSetWindowCloseCallback(long, GLFWWindowCloseCallbackI)}  window close callback using GLFW}.
	 *
	 * @param client the minecraft client.
	 * @see GLFW#glfwSetWindowCloseCallback(long, GLFWWindowCloseCallbackI)
	 */
	@SuppressWarnings("resource")
	private static void setupGLFWCallback(Minecraft client) {
		if (!callbackSetUp) {
			// Intercepting close button / ALT+F4 (on Windows and Ubuntu)
			//? < 26.1 {
			long handle = client.getWindow().getWindow();
			//? } else {
			/*long handle = client.getWindow().handle();
			*///? }

			// Set the callback.
			GLFW.glfwSetWindowCloseCallback(handle, window -> onCloseCallback(client, window));
			callbackSetUp = true;
		}
	}

	public static void onCloseCallback(Minecraft client, long window) {
		GLFW.glfwSetWindowShouldClose(window, false);
		Minecraft instance = Minecraft.getInstance();
		//? < 26.2 {
		if (instance.screen instanceof ConfirmExitScreen) {
			return;
		}

		instance.setScreen(new ConfirmExitScreen(instance.screen));
		//? } else {
		/*if (instance.gui.screen() instanceof ConfirmExitScreen) {
			return;
		}

		instance.gui.setScreen(new ConfirmExitScreen(instance.gui.screen()));
		*///? }
	}
	static Platform xplat() {
		return PLATFORM;
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		return new FabricPlatform();
		//?} neoforge {
		/*return new NeoforgePlatform();
		 *///?} forge {
		/*return new ForgePlatform();
		 *///?}
	}

	private static ResourceLocation id(String path) {
		//? > 1.20.4 {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
		 //?} <= 1.20.4 {
		/*return new ResourceLocation(MOD_ID, path);
		*///?}
	}

	private static ResourceLocation id(String namespace, String path) {
		//? > 1.20.4 {
		return ResourceLocation.fromNamespaceAndPath(namespace, path);
		 //?} <= 1.20.4 {
		/*return new ResourceLocation(namespace, path);
		*///?}
	}

	public static void markShouldClose() {
		shouldClose = true;
	}

	public static boolean shouldClose() {
		return shouldClose;
	}
}
