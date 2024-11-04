package club.aurorapvp.nomoredroppeditems.events.listeners;

import club.aurorapvp.nomoredroppeditems.modules.InventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class ItemPickupListener implements Listener {
  @EventHandler
  public void onPickupItem(EntityPickupItemEvent event) {
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }

    if (InventoryManager.isInventoryDisabled(player)) {
      event.setCancelled(true);
    }
  }
}
