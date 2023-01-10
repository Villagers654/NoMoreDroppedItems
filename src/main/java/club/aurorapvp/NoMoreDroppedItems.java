package club.aurorapvp;

import club.aurorapvp.config.Config;
import club.aurorapvp.listeners.EventListener;
import java.io.File;
import java.io.IOException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class NoMoreDroppedItems extends JavaPlugin implements Listener {
  public static Plugin PLUGIN;
  public static File DATA_FOLDER;

  @Override
  public void onEnable() {
    PLUGIN = this;
    DATA_FOLDER = this.getDataFolder();

    getServer().getPluginManager().registerEvents(new EventListener(), this);
    try {
      Config.generateDefaults();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
