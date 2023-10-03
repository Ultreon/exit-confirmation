package com.ultreon.mods.exitconfirmation.config;

import com.ultreon.mods.exitconfirmation.OrderedHashMap;
import com.ultreon.mods.exitconfirmation.config.entries.*;
import com.ultreon.mods.exitconfirmation.config.gui.ConfigEntry;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.UUID;

public class Config {
    public static final File FILE = new File(getConfigDir().toFile(), "exit-confirmation.txt");

    private static final OrderedHashMap<String, ConfigEntry<?>> ENTRIES = new OrderedHashMap<>();
    public final ConfigEntry<Boolean> closePrompt;
    public final ConfigEntry<Boolean> closePromptInGame;
    public final ConfigEntry<Boolean> quitOnEscInTitle;
    public final ConfigEntry<Boolean> dirtBackground;
    public final ConfigEntry<Boolean> forceTransparentBackground;
    public final ConfigEntry<Integer> confirmDelay;
    public final ConfigEntry<Boolean> saveAndExitPrompt;

    public Config() {
        this.closePrompt = this.add("prompt.enable", true, "Show close prompt.");
        this.closePromptInGame = this.add("prompt.inGame", true, "Show the close prompt when in-game.");
        this.quitOnEscInTitle = this.add("prompt.escInTitle", true, "Show the close prompt when pressing escape in the title screen.");
        this.dirtBackground = this.add("screen.forceDirtBackground", false, "Make the background of the close prompt always dirt.");
        this.forceTransparentBackground = this.add("screen.forceTransparentBackground", true, "Make the background of the close prompt always transparent.");
        this.confirmDelay = this.add("screen.confirmDelay", 40, 5, 100, "Confirmation delay in ticks.");
        this.saveAndExitPrompt = this.add("prompt.saveAndExit", false, "Show save and exit prompt when quitting from world or server.");
    }

    private ConfigEntry<Boolean> add(String key, boolean defaultValue, String comment) {
        ConfigEntry<Boolean> entry = new BooleanEntry(key, defaultValue).comment(comment);
        ENTRIES.put(key, entry);

        return entry;
    }

    private ConfigEntry<Integer> add(String key, int defaultValue, int min, int max, String comment) {
        ConfigEntry<Integer> entry = new IntEntry(key, defaultValue, min, max).comment(comment);
        ENTRIES.put(key, entry);

        return entry;
    }

    private ConfigEntry<Long> add(String key, long defaultValue, long min, long max, String comment) {
        ConfigEntry<Long> entry = new LongEntry(key, defaultValue, min, max).comment(comment);
        ENTRIES.put(key, entry);

        return entry;
    }

    private ConfigEntry<Float> add(String key, float defaultValue, float min, float max, String comment) {
        ConfigEntry<Float> entry = new FloatEntry(key, defaultValue, min, max).comment(comment);
        ENTRIES.put(key, entry);

        return entry;
    }

    private ConfigEntry<Double> add(String key, double defaultValue, double min, double max, String comment) {
        ConfigEntry<Double> entry = new DoubleEntry(key, defaultValue, min, max).comment(comment);
        ENTRIES.put(key, entry);

        return entry;
    }

    private ConfigEntry<String> add(String key, String defaultValue, String comment) {
        ConfigEntry<String> entry = new StringEntry(key, defaultValue).comment(comment);
        ENTRIES.put(key, entry);

        return entry;
    }

    private ConfigEntry<UUID> add(String key, UUID defaultValue, String comment) {
        ConfigEntry<UUID> entry = new UUIDEntry(key, defaultValue).comment(comment);
        ENTRIES.put(key, entry);

        return entry;
    }

    public static void load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String s;
            while ((s = reader.readLine()) != null) {
                if (s.startsWith("#")) {
                    continue;
                }
                String[] entryArr = s.split("=", 2);
                if (entryArr.length <= 1) {
                    continue;
                }

                ConfigEntry<?> entry = ENTRIES.get(entryArr[0]);
                entry.readAndSet(entryArr[1]);
            }
        } catch (FileNotFoundException ignored) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            for (ConfigEntry<?> e : ENTRIES.values()) {
                String key = e.getKey();
                String value = e.write();

                String comment = e.getComment();
                if (comment != null && !comment.isBlank()) {
                    writer.write("# ");
                    writer.write(comment.trim().replaceAll("\r\n", " ").replaceAll("\r", " ").replaceAll("\n", " "));
                    writer.newLine();
                }
                writer.write(key);
                writer.write("=");
                writer.write(value);
                writer.newLine();
            }
        } catch (FileNotFoundException ignored) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static ConfigEntry<?>[] values() {
        return ENTRIES.values().toArray(new ConfigEntry[0]);
    }
}
