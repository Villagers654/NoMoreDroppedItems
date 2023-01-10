package club.aurorapvp.config;

import club.aurorapvp.NoMoreDroppedItems;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
  private static final HashMap<String, Object> DEFAULTS = new HashMap<>();
  private static final File FILE = new File(NoMoreDroppedItems.DATA_FOLDER, "config.yml");
  private static YamlConfiguration config;

  public static void generateDefaults() throws IOException {
    DEFAULTS.put("excluded-items", new ArrayList<String>().add("example_block"));

    for (String path : DEFAULTS.keySet()) {
      if (!get().contains(path) || get().getString(path) == null) {
        get().set(path, DEFAULTS.get(path));
        get().save(FILE);
      }
    }
  }

  public static YamlConfiguration get() {
    return config;
  }

  public static void reload() throws IOException {
    if (!FILE.exists()) {
      FILE.getParentFile().mkdirs();

      FILE.createNewFile();
    }
    config = YamlConfiguration.loadConfiguration(FILE);
    NoMoreDroppedItems.PLUGIN.getLogger().info("Config reloaded!");
  }
}
