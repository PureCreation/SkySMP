package de.qgel.skySMP;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class skyHelpCommand
  implements CommandExecutor
{
  private final skySMP plugin;
  private HashMap<String, Island> playerIslands;

  public skyHelpCommand(skySMP plugin)
  {
    this.plugin = plugin;
    this.playerIslands = new HashMap<String, Island>();
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] split)
  {
    if (!(sender instanceof Player)) {
      return false;
    }
    if (split.length == 0) {
      sender.sendMessage("");
      sender.sendMessage(ChatColor.RED + "Commands:");
      sender.sendMessage(ChatColor.DARK_AQUA + "/newisland [replace]: Gives you a new island or replaces your old one");
      sender.sendMessage(ChatColor.DARK_AQUA + "/home: Teleports you to your island.");
      sender.sendMessage(ChatColor.DARK_AQUA + "/coop: Join a group of people (max 4 people)");
      sender.sendMessage(ChatColor.DARK_AQUA + "/rules: View the server rules");
      sender.sendMessage(ChatColor.DARK_AQUA + "/vote: Vote for the server");
      sender.sendMessage(ChatColor.DARK_AQUA + "/donate: Donate to help keep the server online");
      sender.sendMessage(ChatColor.DARK_AQUA + "/challenges: Challenges of SkyBlock page 1");
      sender.sendMessage(ChatColor.DARK_AQUA + "/challenges2: Challenges of SkyBlock page 2");
      sender.sendMessage(ChatColor.DARK_AQUA + "/help: Even more commands!");
      sender.sendMessage("");
    }
    else if ((split.length == 1) && 
      (sender.isOp()))
    {
      if (split[0].equals("playerlist"))
      {
        this.playerIslands = this.plugin.getPlayers();
        sender.sendMessage(this.playerIslands.toString());
      }

    }

    return true;
  }
}