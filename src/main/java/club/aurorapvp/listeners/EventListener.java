package club.aurorapvp.listeners;

import club.aurorapvp.config.Config;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {
  @EventHandler
  public void onItemSpawn(PlayerDropItemEvent event) {
    if (event.getItemDrop().getType() == EntityType.DROPPED_ITEM) {
      for (Object entry : Objects.requireNonNull(Config.get().getList("excluded-items"))) {
        ItemStack item = Bukkit.getItemFactory().createItemStack((String) entry);

        if (event.getItemDrop().getItemStack().getType() == item.getType()) {
          return;
        }
      }
      event.setCancelled(true);
    }
  }
}