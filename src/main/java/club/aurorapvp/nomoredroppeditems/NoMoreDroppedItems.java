package club.aurorapvp.nomoredroppeditems;

import club.aurorapvp.nomoredroppeditems.commands.CommandManager;
import club.aurorapvp.nomoredroppeditems.config.Config;
import club.aurorapvp.nomoredroppeditems.config.Lang;
import club.aurorapvp.nomoredroppeditems.events.EventManager;
import club.aurorapvp.nomoredroppeditems.flags.Flags;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class NoMoreDroppedItems extends JavaPlugin implements Listener {

  private static NoMoreDroppedItems INSTANCE;
  private static Config config;
  private static Lang lang;

  public static NoMoreDroppedItems getInstance() {
    return INSTANCE;
  }

  public @NotNull YamlConfiguration getConfig() {
    return config.getYaml();
  }

  public Lang getLang() {
    return lang;
  }

  public void reloadConfig() {
    config.reload();
  }

  @Override
  public void onLoad() {
    long startTime = System.currentTimeMillis();

    INSTANCE = this;

    Flags.init();

    getLogger()
        .info(
            "NoMoreDroppedItems loaded in "
                + Math.subtractExact(System.currentTimeMillis(), startTime)
                + "ms");
  }

  @Override
  public void onEnable() {
    long startTime = System.currentTimeMillis();

    // Initialize configs
    config = new Config();
    lang = new Lang();

    // Initialize classes
    CommandManager.init();
    EventManager.init();

    getLogger()
        .info(
            "NoMoreDroppedItems enabled in "
                + Math.subtractExact(System.currentTimeMillis(), startTime)
                + "ms");
  }

  @Override
  public void onDisable() {
    long startTime = System.currentTimeMillis();

    getLogger().info("NoMoreDroppedItems disabled in " + (System.currentTimeMillis() - startTime) + "ms");
  }
}
