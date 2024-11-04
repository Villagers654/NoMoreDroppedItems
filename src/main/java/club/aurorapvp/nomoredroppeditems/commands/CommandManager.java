package club.aurorapvp.nomoredroppeditems.commands;

import club.aurorapvp.nomoredroppeditems.NoMoreDroppedItems;
import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.PaperCommandManager;

public class CommandManager {

  public static PaperCommandManager MANAGER = new PaperCommandManager(NoMoreDroppedItems.getInstance());

  public static void init() {
    MANAGER.registerCommand(new ClearCommand());
    MANAGER.registerCommand(new AutoInventoryCommand());

    CommandCompletions<BukkitCommandCompletionContext> commandCompletions =
        MANAGER.getCommandCompletions();
  }
}
