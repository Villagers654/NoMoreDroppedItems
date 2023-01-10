package club.aurorapvp.listeners;

import club.aurorapvp.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {
  @EventHandler
  public void onItemSpawn(ItemSpawnEvent event) {
    if (event.getEntity().getType() == EntityType.DROPPED_ITEM) {
      for (Object entry : Config.get().getList("excluded-items")) {
        ItemStack item = Bukkit.getItemFactory().createItemStack((String) entry);

        if (event.getEntity().getItemStack().getType() == item.getType()) {
          return;
        }
      }
      event.getEntity().remove();
    }
  }
}