package club.aurorapvp.NoMoreDroppedItems;

import club.aurorapvp.NoMoreDroppedItems.config.Config;
import club.aurorapvp.NoMoreDroppedItems.flags.Flags;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class NoMoreDroppedItems extends JavaPlugin implements Listener {

  public static NoMoreDroppedItems INSTANCE;

  @Override
  public void onLoad() {
    long startTime = System.currentTimeMillis();

    INSTANCE = this;

    // Setup configs
    Config.init();

    Flags.init();

    getLogger()
        .info(
            "AuroraCombat loaded in "
                + Math.subtractExact(System.currentTimeMillis(), startTime)
                + "ms");
  }
}
