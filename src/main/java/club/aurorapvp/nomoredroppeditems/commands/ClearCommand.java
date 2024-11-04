package club.aurorapvp.nomoredroppeditems.commands;

import club.aurorapvp.nomoredroppeditems.NoMoreDroppedItems;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;

@CommandAlias("clear")
public class ClearCommand extends BaseCommand {

  @Default
  @Description("Clears your inventory")
  @SuppressWarnings("unused")
  public void onClear(Player player) {
    player.getInventory().clear();

    player.sendMessage(
        NoMoreDroppedItems.getInstance().getLang().getComponent("inventory-cleared"));
  }
}
