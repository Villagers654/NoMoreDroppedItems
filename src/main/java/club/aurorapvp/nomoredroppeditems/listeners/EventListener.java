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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class EventListener implements Listener {

  List<String> excludedItems = (List<String>) Config.get().getList("excluded-items");

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

  @EventHandler
  public void onBlockDrop(BlockDropItemEvent event) {
    Location loc = event.getBlock().getLocation();
    Collection<Item> items = event.getItems();

    if (shouldPreventDrop(Flags.BLOCK_DROPS_ENABLED, items, loc)) {
      for (Item item : items) {
        item.remove();
      }
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onBlockExplode(BlockExplodeEvent event) {
    Set<String> excludedItemsSet = new HashSet<>(excludedItems);

    World world = event.getBlock().getWorld();

    new BukkitRunnable() {
      @Override
      public void run() {
        for (Block block : event.blockList()) {
          Location loc = block.getLocation();

          Collection<Entity> existingDrops =
              world.getNearbyEntities(loc, 1, 1, 1, entity -> entity instanceof Item);

          existingDrops.stream()
              .filter(entity -> entity instanceof Item)
              .map(entity -> (Item) entity)
              .map(Item::getItemStack)
              .map(ItemStack::getType)
              .map(Enum::name)
              .filter(itemName -> !excludedItemsSet.contains(itemName))
              .forEach(
                  itemName ->
                      existingDrops.stream()
                          .filter(
                              entity ->
                                  ((Item) entity).getItemStack().getType().name().equals(itemName))
                          .forEach(Entity::remove));
        }
      }
    }.runTaskLater(NoMoreDroppedItems.INSTANCE, 1);
  }

  private <T> boolean shouldPreventDrop(StateFlag flag, Collection<T> drops, Location loc) {
    for (Object entry : excludedItems) {
      for (T item : drops) {
        String itemName;
        if (item instanceof Item) {
          itemName = ((Item) item).getType().name();
        } else if (item instanceof ItemStack) {
          itemName = ((ItemStack) item).getType().name();
        } else {
          continue;
        }

        if (itemName.equals(entry)) {
          return false;
        }
      }
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
