package de.qgel.skySMP;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class skyDevCommand implements CommandExecutor {
    private final skySMP plugin; 
    
	public skyDevCommand(skySMP plugin) {
        this.plugin = plugin;
    }
        
    
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player) || !(sender.isOp()) ){
            return false;
        }      
		sender.sendMessage(plugin.getLastIsland().x + " / " +plugin.getLastIsland().z);
        Player player = (Player) sender;
if (split.length == 0) {
	if (sender.isOp())
	{
		player.sendMessage("[skydev usage]");
	}else
		player.sendMessage("You don't have permission to use this command.");
	}else if (split.length == 1) {
	}else if (split[0].equals("offline") && player.isOp())
	{
		OfflinePlayer[] oplayers;
    	long offlineTime;
    	oplayers = Bukkit.getServer().getOfflinePlayers();
    	for (int i = 0; i < oplayers.length; i++)
    	{
    		offlineTime = oplayers[i].getLastPlayed();
    		offlineTime = (System.currentTimeMillis() - offlineTime)/3600000;
    		if (offlineTime > 2232) 
    		{
				plugin.autodeleteIsland(oplayers[i].getName(), Bukkit.getWorld("skyworld"));
				//Bukkit.broadcastMessage("Deleting: " + oplayers[i].getName() + "'s island.  They were offline for " + offlineTime + " hours." );
			}
    	}
		}
	return true;
}
}


		

