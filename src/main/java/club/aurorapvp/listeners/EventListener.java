package club.aurorapvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class EventListener implements Listener {
  @EventHandler
  public void onItemSpawn(ItemSpawnEvent event) {
    event.getEntity().remove();
  }
}