package club.aurorapvp.NoMoreDroppedItems.flags;

import club.aurorapvp.NoMoreDroppedItems.NoMoreDroppedItems;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import java.util.logging.Level;

public class Flags {

  public static StateFlag DROPS_ENABLED;
  public static StateFlag DEATH_DROPS_ENABLED;

  public static void init() {
    FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

    try {
      StateFlag flag = new StateFlag("drops-enabled", true);
      registry.register(flag);
      DROPS_ENABLED = flag;
    } catch (FlagConflictException e) {
      NoMoreDroppedItems.INSTANCE.getLogger()
          .log(Level.SEVERE, "Unable to register Drops Enabled flag", e);
    }

    try {
      StateFlag flag = new StateFlag("death-drops-enabled", true);
      registry.register(flag);
      DEATH_DROPS_ENABLED = flag;
    } catch (FlagConflictException e) {
      NoMoreDroppedItems.INSTANCE.getLogger()
          .log(Level.SEVERE, "Unable to register Death Drops Enabled flag", e);
    }
  }
}
