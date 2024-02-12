package club.aurorapvp.nomoredroppeditems;

import club.aurorapvp.nomoredroppeditems.commands.ClearCommand;
import club.aurorapvp.nomoredroppeditems.config.Config;
import club.aurorapvp.nomoredroppeditems.flags.Flags;
import club.aurorapvp.nomoredroppeditems.listeners.EventListener;
import co.aikar.commands.PaperCommandManager;
import org.bukkit.Bukkit;
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
            "NoMoreDroppedItems loaded in "
                + Math.subtractExact(System.currentTimeMillis(), startTime)
                + "ms");
  }

  @Override
  public void onEnable() {
    long startTime = System.currentTimeMillis();

    Bukkit.getPluginManager().registerEvents(new EventListener(), this);

    new PaperCommandManager(this).registerCommand(new ClearCommand());

    getLogger()
        .info(
            "NoMoreDroppedItems enabled in "
                + Math.subtractExact(System.currentTimeMillis(), startTime)
                + "ms");
  }
}
