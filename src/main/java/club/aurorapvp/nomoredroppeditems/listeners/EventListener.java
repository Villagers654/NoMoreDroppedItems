package club.aurorapvp.nomoredroppeditems.listeners;

import club.aurorapvp.nomoredroppeditems.NoMoreDroppedItems;
import club.aurorapvp.nomoredroppeditems.config.Config;
import club.aurorapvp.nomoredroppeditems.flags.Flags;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class EventListener implements Listener {

  private static final Set<ItemStack> ITEMS_TO_REMOVE = ConcurrentHashMap.newKeySet();

  @EventHandler
  public void onBlockBroken(BlockBreakEvent event) {
    Block block = event.getBlock();

    if (shouldPreventDrop(Flags.BLOCK_DROPS_ENABLED, block.getDrops(), block.getLocation())) {
      event.setDropItems(false);
    }
  }

  @EventHandler
  public void onBlockDestroyed(BlockDestroyEvent event) {
    Block block = event.getBlock();

    if (shouldPreventDrop(Flags.BLOCK_DROPS_ENABLED, block.getDrops(), block.getLocation())) {
      event.setWillDrop(false);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onBlockExplode(BlockExplodeEvent event) {
    for (Block block : event.blockList()) {
      ITEMS_TO_REMOVE.addAll(block.getDrops());

      new BukkitRunnable() {
        @Override
        public void run() {
          ITEMS_TO_REMOVE.removeAll(block.getDrops());
        }
      }.runTaskLaterAsynchronously(NoMoreDroppedItems.INSTANCE, 1);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onItemSpawn(ItemSpawnEvent event) {
    ItemStack items = event.getEntity().getItemStack();

    if (ITEMS_TO_REMOVE.contains(items)
        && shouldPreventDrop(Flags.BLOCK_DROPS_ENABLED, Set.of(items), event.getLocation())) {
      event.getEntity().remove();
    }
  }

  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent event) {
    Item item = event.getItemDrop();

    if (shouldPreventDrop(
        Flags.PLAYER_DROPS_ENABLED, Set.of(item.getItemStack()), item.getLocation())) {
      item.remove();
    }
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getPlayer();
    List<ItemStack> itemStacks = event.getDrops();

    Set<Object> excludedItems =
        new HashSet<>(Objects.requireNonNull(Config.get().getList("excluded-items")));

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

  private boolean shouldPreventDrop(StateFlag flag, Collection<ItemStack> drops, Location loc) {
    for (Object entry : Objects.requireNonNull(Config.get().getList("excluded-items"))) {
      for (ItemStack item : drops) {
        if (item.getType().name().equals(entry)) {
          return false;
        }
      }
    }

    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    RegionQuery query = container.createQuery();
    ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(loc));

    for (ProtectedRegion region : set.getRegions()) {
      if (Objects.equals(region.getFlag(flag), State.DENY)) {
        return true;
      }
    }

    return false;
  }
}
