package club.aurorapvp.nomoredroppeditems.flags;

import club.aurorapvp.nomoredroppeditems.NoMoreDroppedItems;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import java.util.logging.Level;

public class Flags {

  public static StateFlag PLAYER_DROPS_ENABLED;
  public static StateFlag BLOCK_DROPS_ENABLED;
  public static StateFlag DEATH_DROPS_ENABLED;

  public static void init() {
    FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

    try {
      StateFlag flag = new StateFlag("item-drops", true);
      registry.register(flag);
      PLAYER_DROPS_ENABLED = flag;
    } catch (FlagConflictException e) {
      NoMoreDroppedItems.INSTANCE.getLogger()
          .log(Level.SEVERE, "Unable to register Player Drops Enabled flag", e);
    }

    try {
      StateFlag flag = new StateFlag("block-drops", true);
      registry.register(flag);
      BLOCK_DROPS_ENABLED = flag;
    } catch (FlagConflictException e) {
      NoMoreDroppedItems.INSTANCE.getLogger()
              .log(Level.SEVERE, "Unable to register Block Drops Enabled flag", e);
    }

    try {
      StateFlag flag = new StateFlag("death-drops", true);
      registry.register(flag);
      DEATH_DROPS_ENABLED = flag;
    } catch (FlagConflictException e) {
      NoMoreDroppedItems.INSTANCE.getLogger()
          .log(Level.SEVERE, "Unable to register Death Drops Enabled flag", e);
    }
  }
}
