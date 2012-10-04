package de.qgel.skySMP;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class tpHomeCommand
  implements CommandExecutor
{
  private final skySMP plugin;
  private List<Party> partyList;

  public tpHomeCommand(skySMP plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] split)
  {
    if (!(sender instanceof Player)) {
      return false;
    }
    Player player = (Player)sender;
    this.partyList = this.plugin.getPartyList();
    if (hasParty(sender.getName()) >= 0)
    {
      if (player.getWorld().getEnvironment().getId() == 0) {
        this.plugin.teleportGroup(((Party)this.partyList.get(hasParty(sender.getName()))).getLeader(), (Player)sender, this.plugin.getServer().getWorld("skyworld"));
        return true;
      }

      player.sendMessage("Can't tphome in the nether, sorry");
      return true;
    }
    if (this.plugin.hasIsland(sender.getName())) {
      if (player.getWorld().getEnvironment().getId() == 0) {
  		Island home = plugin.getPlayerIsland(player.getName());
        this.plugin.teleportHome((Player)sender, this.plugin.getServer().getWorld("skyworld"));
        return true;
      }
      player.sendMessage("Can't tphome in the nether, sorry");
      return true;
    }

    sender.sendMessage("Error.  You do not have an island.");
    return true;
  }
    
  private int hasParty(String playerName)
  {
    try
    {
      if (Bukkit.getPlayer(playerName).isOnline())
      {
        for (int i = 0; i < this.partyList.size(); i++)
          if (((Party)this.partyList.get(i)).hasMember(playerName))
            return i;
        return -1;
      }
      return -2; } catch (Exception e) {
    }
    return -2;
  }
}