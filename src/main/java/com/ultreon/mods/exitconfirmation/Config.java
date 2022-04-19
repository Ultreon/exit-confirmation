package com.ultreon.mods.exitconfirmation;

import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
@Mod.EventBusSubscriber(modid = ExitConfirmation.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Config {
    public static final ForgeConfigSpec.BooleanValue closePrompt;
    public static final ForgeConfigSpec.BooleanValue closePromptInGame;
    public static final ForgeConfigSpec.BooleanValue closePromptQuitButton;
    public static final ForgeConfigSpec.BooleanValue quitOnEscInTitle;

    private static final ForgeConfigSpec clientSpec;

    @Deprecated
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<Module, ForgeConfigSpec.BooleanValue> modules = new OrderedHashMap<>();

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
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientSpec);
    }

    public static void sync() {

    }

    @SubscribeEvent
    public static void sync(ModConfigEvent.Loading event) {
        sync();
    }

    @SubscribeEvent
    public static void sync(ModConfigEvent.Reloading event) {
        sync();
    }

    public static void save() {
        clientSpec.save();
    }

    public static ForgeConfigSpec.BooleanValue getModuleSpec(Module module) {
        return modules.get(module);
    }
}
