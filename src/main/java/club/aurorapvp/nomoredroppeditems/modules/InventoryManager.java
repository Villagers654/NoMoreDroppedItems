package club.aurorapvp.nomoredroppeditems.modules;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class InventoryManager {
  public static Set<Player> INVENTORY_DISABLED = new HashSet<>();

  public static boolean isInventoryDisabled(Player player) {
    return INVENTORY_DISABLED.contains(player);
  }

  public static boolean changeInventoryState(Player player) {
    boolean isInventoryDisabled = isInventoryDisabled(player);

    setInventoryDisabled(player, !isInventoryDisabled);

    return !isInventoryDisabled;
  }

  public static void setInventoryDisabled(Player player) {
    INVENTORY_DISABLED.add(player);
  }

  public static void setInventoryEnabled(Player player) {
    INVENTORY_DISABLED.remove(player);
  }

  public static void setInventoryDisabled(Player player, boolean disable) {
    if (disable) {
      INVENTORY_DISABLED.add(player);
    } else {
      INVENTORY_DISABLED.remove(player);
    }
  }
}
