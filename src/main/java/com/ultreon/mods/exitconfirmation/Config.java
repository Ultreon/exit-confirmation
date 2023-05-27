//package com.ultreon.mods.exitconfirmation;
//
//import io.github.minecraftcursedlegacy.api.config.Configs;
//import net.fabricmc.loader.api.FabricLoader;
//import tk.valoeghese.zoesteriaconfig.api.container.WritableConfig;
//import tk.valoeghese.zoesteriaconfig.api.deserialiser.Comment;
//import tk.valoeghese.zoesteriaconfig.api.template.ConfigTemplate;
//
//import java.io.File;
//import java.io.IOException;
//
//public final class Config {
//    private static WritableConfig config;
//
//    private static File directory;
//    private static File configFile;
//
//    private Config() {
//    }
//
//    public static void initialize() throws IOException {
//        directory = new File(FabricLoader.getInstance().getConfigDir().toFile(), ExitConfirmation.MOD_ID);
//        configFile = new File(directory, "client.cfg");
//        config = Configs.loadOrCreate(ExitConfirmation.id("client"),
//                ConfigTemplate.builder()
//                        .addComment(new Comment("Show close prompt."))
//                        .addDataEntry("closePrompt", true)
//                        .addComment(new Comment("Show close prompt in-game."))
//                        .addDataEntry("closePromptInGame", true)
//                        .addComment(new Comment("Show close prompt when pressing quit button."))
//                        .addDataEntry("closePromptQuitButton", true)
//                        .addComment(new Comment("Show close prompt when pressing escape in title screen."))
//                        .addDataEntry("closePromptTitleEsc", true)
//                        .build());
//    }
//
//    public static WritableConfig getConfig() {
//        return config;
//    }
//
//    public static boolean getClosePrompt() {
//        return config.getBooleanValue("closePrompt");
//    }
//
//    public static void setClosePrompt(boolean enabled) {
//        config.putBooleanValue("closePrompt", enabled);
//    }
//
//    public static boolean getClosePromptInGame() {
//        return config.getBooleanValue("closePromptInGame");
//    }
//
//    public static void setClosePromptInGame(boolean enabled) {
//        config.putBooleanValue("closePromptInGame", enabled);
//    }
//
//    public static boolean getClosePromptQuitButton() {
//        return config.getBooleanValue("closePromptQuitButton");
//    }
//
//    public static void setClosePromptQuitButton(boolean enabled) {
//        config.putBooleanValue("closePromptQuitButton", enabled);
//    }
//
//    public static boolean getQuitOnEscInTitle() {
//        return config.getBooleanValue("closePromptTitleEsc");
//    }
//
//    public static void setQuitOnEscInTitle(boolean enabled) {
//        config.putBooleanValue("closePromptTitleEsc", enabled);
//    }
//
//    public static void save() {
//        config.writeToFile(configFile);
//    }
//
//    public static File getDirectory() {
//        return directory;
//    }
//
//    public static File getConfigFile() {
//        return configFile;
//    }
//}
