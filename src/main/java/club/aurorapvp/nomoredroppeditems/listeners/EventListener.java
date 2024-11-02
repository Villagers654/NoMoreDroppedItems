package club.aurorapvp.nomoredroppeditems.listeners;

import club.aurorapvp.nomoredroppeditems.NoMoreDroppedItems;
import club.aurorapvp.nomoredroppeditems.config.Config;
import club.aurorapvp.nomoredroppeditems.flags.Flags;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.util.*;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class EventListener implements Listener {

  List<String> excludedItems = (List<String>) Config.get().getList("excluded-items");

  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent event) {
    Item item = event.getItemDrop();

    if (shouldPreventDrop(Flags.PLAYER_DROPS_ENABLED, item.getItemStack(), item.getLocation())) {
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

  @EventHandler
  public void onBlockDrop(BlockDropItemEvent event) {
    Location loc = event.getBlock().getLocation();
    Collection<Item> items = event.getItems();

    for (Item item : items) {
      if (shouldPreventDrop(Flags.BLOCK_DROPS_ENABLED, item, loc)) {
        item.remove();
      }
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onEntityExplode(EntityExplodeEvent event) {
    World world = event.getLocation().getWorld();
    Location explosionCenter = event.getLocation();

    handleExplosion(world, explosionCenter, event.blockList());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onBlockExplode(BlockExplodeEvent event) {
    World world = event.getBlock().getWorld();
    Location explosionCenter = event.getBlock().getLocation();

    handleExplosion(world, explosionCenter, event.blockList());
  }

  private void handleExplosion(World world, Location center, List<Block> blocks) {
    if (!checkFlag(Flags.EXPLOSION_DROPS_ENABLED, center)) {
      return;
    }

    double maxDistance =
        blocks.stream().mapToDouble(block -> block.getLocation().distance(center)).max().orElse(0)
            + 1.0;

    new BukkitRunnable() {
      @Override
      public void run() {
        Collection<Entity> nearbyEntities =
            world.getNearbyEntities(
                center,
                maxDistance,
                maxDistance,
                maxDistance,
                entity ->
                    entity instanceof Item && entity.getLocation().distance(center) <= maxDistance);

        List<Item> itemsWithinSphere =
            nearbyEntities.stream().map(entity -> (Item) entity).toList();

        Map<String, List<Item>> itemsByType =
            itemsWithinSphere.stream()
                .collect(Collectors.groupingBy(item -> item.getItemStack().getType().name()));

        itemsByType.forEach(
            (itemName, items) -> {
              if (!excludedItems.contains(itemName)) {
                items.forEach(Entity::remove);
              }
            });
      }
    }.runTaskLater(NoMoreDroppedItems.INSTANCE, 1);
  }

  private boolean shouldPreventDrop(StateFlag flag, Item drop, Location loc) {
    return shouldPreventDrop(flag, drop.getItemStack(), loc);
  }

  private boolean shouldPreventDrop(StateFlag flag, ItemStack drop, Location loc) {
    if (excludedItems.contains(drop.getType().name())) {
      return false;
    }

    return checkFlag(flag, loc);
  }

  private boolean checkFlag(StateFlag flag, Location loc) {
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
