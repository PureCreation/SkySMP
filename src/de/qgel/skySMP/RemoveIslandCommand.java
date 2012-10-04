package de.qgel.skySMP;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveIslandCommand
  implements CommandExecutor
{
  private final skySMP plugin;

  public RemoveIslandCommand(skySMP plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
    if ((!(sender instanceof Player)) || (!sender.isOp())) {
      return false;
    }
    Player player = (Player) sender;

    if (split.length == 1) {
      String playerName = split[0];
      if (this.plugin.hasIsland(playerName)) {
        this.plugin.deleteIsland(playerName, ((Player)sender).getWorld());
        sender.sendMessage(ChatColor.DARK_AQUA + "Player \"" + playerName + "\" island removed");
        return true;
      }
      sender.sendMessage(ChatColor.RED + "Player \"" + playerName + "\" doesn't have an island registered.");
      return true;
    }

    return false;
  }
}