package club.aurorapvp;

import club.aurorapvp.config.Config;
import club.aurorapvp.listeners.EventListener;
import java.io.File;
import java.io.IOException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class NoMoreDroppedItems extends JavaPlugin implements Listener {
  public static Plugin INSTANCE;
  public static File DATA_FOLDER;

  @Override
  public void onEnable() {
    INSTANCE = this;

    Config.init();

    getServer().getPluginManager().registerEvents(new EventListener(), this);
  }
}
