package club.aurorapvp.nomoredroppeditems.events;

import club.aurorapvp.nomoredroppeditems.NoMoreDroppedItems;
import club.aurorapvp.nomoredroppeditems.events.listeners.ItemDropListener;
import org.bukkit.Bukkit;

public class EventManager {

  public static void init() {
    Bukkit.getPluginManager().registerEvents(new ItemDropListener(), NoMoreDroppedItems.getInstance());
  }
}
