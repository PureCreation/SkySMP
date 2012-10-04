package de.qgel.skySMP;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.EventHandler;
import de.qgel.skySMP.skySMP;

public class PlayerEventListener implements Listener {
	
	private static skySMP plugin;
    HashMap<String, Integer> players = new HashMap<String, Integer>();
    
	public PlayerEventListener(skySMP instance) {
		PlayerEventListener.plugin = instance;
	}
	
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage("");    	
    	if (!plugin.hasIsland(event.getPlayer())) {  		
    		event.getPlayer().sendMessage(ChatColor.DARK_AQUA + "Welcome to SkyBlock!");
    		event.getPlayer().sendMessage(ChatColor.GOLD + "Type /newisland to get started!");
    		event.getPlayer().sendMessage(ChatColor.GOLD + "To return to your island type /home");    		
    
    		//TP the player to the spawn location, because first join sometimes seems to get fucked up.
    		Location spawn = event.getPlayer().getWorld().getSpawnLocation();
    		event.getPlayer().getWorld().getSpawnLocation().getBlock().getChunk().load();
    		event.getPlayer().teleport(new Location(spawn.getWorld(), spawn.getX(),spawn.getBlockY() + 3, spawn.getZ()));
    }
}
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
	event.setQuitMessage("");    
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        from.setY(0);
        Location to = event.getTo();
        to.setY(0);
        Player player = event.getPlayer();   
        
        if (from.distance(to) > 1.7) {        	
            player.kickPlayer("You moved to fast!");
    	    for (Player player1 : Bukkit.getServer().getOnlinePlayers()) {
			if (player1.isOp()) {
             player1.sendMessage(event.getPlayer().getName() + ChatColor.GRAY + " has been kicked for moving to fast!");
		    }
    }
    }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {    	
    if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.hasItem() && e.getItem().getType() == Material.DIAMOND_ORE) {
    e.getPlayer().getWorld().strikeLightning(e.getClickedBlock().getLocation());
    e.setCancelled(true);
    }
    }    
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block block = event.getClickedBlock();
     
            if (block.getType() == Material.OBSIDIAN) {
                block.setType(Material.LAVA);
            }
        }
    }       
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
    	Player player = event.getPlayer();
    	if(plugin.hasIsland(player)) { 
    		if(player.getWorld().getEnvironment().getId() == 0) { //if we are  in the normal world
        		Island home = plugin.getPlayerIsland(player.getName());
        		event.setRespawnLocation(new Location(player.getWorld(),home.x,plugin.getISLANDS_Y(),home.z));
    		}

    	}
    }
    @EventHandler
    public void onKickForFlying(PlayerKickEvent event){
        if(!event.getPlayer().getServer().getAllowFlight()){ //Only execute the below code if flying is disabled.
        	if(event.getReason().equals("Flying is not enabled on this server")) //Get kick reason.
                event.setReason("Ay! Quit ye' flying mate! Arrgghh your goin' to get banned!"); //Set kick message.
                	    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
        				if (player.isOp()) {
                        player.sendMessage(event.getPlayer().getName() + ChatColor.RED + " has been kicked for flying!");
                    }
                }
            }
        }
    }


