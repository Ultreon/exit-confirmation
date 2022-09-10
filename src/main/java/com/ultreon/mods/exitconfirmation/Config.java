package com.ultreon.mods.exitconfirmation;

import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.api.fml.event.config.ModConfigEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public final class Config {
    public static final ForgeConfigSpec.BooleanValue closePrompt;
    public static final ForgeConfigSpec.BooleanValue closePromptInGame;
    public static final ForgeConfigSpec.BooleanValue closePromptQuitButton;
    public static final ForgeConfigSpec.BooleanValue quitOnEscInTitle;

    private static final ForgeConfigSpec clientSpec;

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    static {
        // Updates
        closePrompt = builder
                .comment("Show close prompt.")
                .define("closePrompt", true);
        closePromptInGame = builder
                .comment("Show close prompt in-game.")
                .define("closePromptInGame", true);
        closePromptQuitButton = builder
                .comment("Show close prompt when pressing quit button.")
                .define("closePromptQuitButton", true);
        quitOnEscInTitle = builder
                .comment("Show close prompt when pressing escape in title screen.")
                .define("closePromptQuitButton", true);
        clientSpec = builder.build();
    }

    private Config() {

    }

    public static void initialize() {
        ModLoadingContext.registerConfig(ExitConfirmation.MOD_ID, ModConfig.Type.CLIENT, clientSpec);
        ModConfigEvent.LOADING.register(Config::load);
        ModConfigEvent.RELOADING.register(Config::reload);
    }

    public static void sync() {

    }

    public static void load(ModConfig event) {
        sync();
    }

    public static void reload(ModConfig event) {
        sync();
    }

    public static void save() {
        clientSpec.save();
    }
}
