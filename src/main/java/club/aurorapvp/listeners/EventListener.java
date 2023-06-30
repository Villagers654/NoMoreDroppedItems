package club.aurorapvp.listeners;

import club.aurorapvp.config.Config;
import java.util.Objects;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class EventListener implements Listener {
  @EventHandler
  public void onItemSpawn(PlayerDropItemEvent event) {
    for (Object entry : Objects.requireNonNull(Config.get().getList("excluded-items"))) {
      if (event.getItemDrop().getItemStack().getType().name().equals((String) entry)) {
        return;
      }
    }

    event.getItemDrop().remove();
    event.setCancelled(true);
  }
}