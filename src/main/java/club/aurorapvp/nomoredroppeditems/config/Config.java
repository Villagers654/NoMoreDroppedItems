package club.aurorapvp.nomoredroppeditems.config;

import club.aurorapvp.nomoredroppeditems.NoMoreDroppedItems;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
  private static final File FILE =
      new File(NoMoreDroppedItems.INSTANCE.getDataFolder(), "config.yml");
  private static YamlConfiguration config;

  public static void init() {
    reload();
    generateDefaults();
  }

  public static void generateDefaults() {
    final HashMap<String, Object> DEFAULTS = new HashMap<>();

    DEFAULTS.put("excluded-items", List.of("AIR", "BEDROCK"));

    for (String path : DEFAULTS.keySet()) {
      if (!get().isSet(path) || get().getString(path) == null) {
        get().set(path, DEFAULTS.get(path));
      }
    }

    try {
      get().save(FILE);
    } catch (IOException e) {
      NoMoreDroppedItems.INSTANCE.getLogger().severe("Failed to save config file");
    }
  }

  public static YamlConfiguration get() {
    return config;
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static void reload() {
    if (!FILE.exists()) {
      try {
        FILE.getParentFile().mkdirs();
        FILE.createNewFile();
      } catch (IOException e) {
        NoMoreDroppedItems.INSTANCE.getLogger().severe("Failed to generate config file");
      }
    }
    config = YamlConfiguration.loadConfiguration(FILE);
    NoMoreDroppedItems.INSTANCE.getLogger().info("Config reloaded!");
  }
}
