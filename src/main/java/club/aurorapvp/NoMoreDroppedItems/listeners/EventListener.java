package club.aurorapvp.NoMoreDroppedItems.listeners;

import club.aurorapvp.NoMoreDroppedItems.config.Config;
import club.aurorapvp.NoMoreDroppedItems.flags.Flags;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {
  @EventHandler
  public void onItemSpawn(PlayerDropItemEvent event) {
    Item item = event.getItemDrop();

    for (Object entry : Objects.requireNonNull(Config.get().getList("excluded-items"))) {
      if (item.getItemStack().getType().name().equals(entry)) {
        return;
      }
    }

    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    RegionQuery query = container.createQuery();
    ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(item.getLocation()));

    for (ProtectedRegion region : set.getRegions()) {
      if (Objects.equals(region.getFlag(Flags.DROPS_ENABLED), State.DENY)) {
        event.getItemDrop().remove();
      }
    }

    item.remove();
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getPlayer();
    List<ItemStack> itemStacks = event.getDrops();

    Set<Object> excludedItems = new HashSet<>(Objects.requireNonNull(Config.get().getList("excluded-items")));

    itemStacks.removeIf(itemStack -> excludedItems.contains(itemStack.getType().name()));

    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    RegionQuery query = container.createQuery();
    ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));

    for (ProtectedRegion region : set.getRegions()) {
      if (Objects.equals(region.getFlag(Flags.DEATH_DROPS_ENABLED), State.DENY)) {
        itemStacks.clear();
      }
    }
  }
}