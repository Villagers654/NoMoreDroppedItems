package club.aurorapvp.nomoredroppeditems.commands;

import club.aurorapvp.nomoredroppeditems.NoMoreDroppedItems;
import club.aurorapvp.nomoredroppeditems.modules.InventoryManager;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;

public class AutoInventoryCommand extends BaseCommand {
  @Default
  @Description("Enables or disables pickups")
  @CommandPermission("auroraduels.command.autoinventory")
  @SuppressWarnings("unused")
  public void onCommand(Player player) {
    if (InventoryManager.changeInventoryState(player)) {
      player.sendMessage(
          NoMoreDroppedItems.getInstance().getLang().getComponent("inventory-disabled"));
    } else {
      player.sendMessage(
              NoMoreDroppedItems.getInstance().getLang().getComponent("inventory-enabled"));
    }
  }
}
