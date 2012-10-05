package de.qgel.skySMP;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import de.qgel.skySMP.PlayerEventListener;

@SuppressWarnings("unused")
public class skySMP extends JavaPlugin 
{
  private PlayerEventListener plugin = new PlayerEventListener(this);
  private HashMap<String, Island> playerIslands = new HashMap();
  private Stack<Island> orphaned = new Stack();
  private static int SPAWN_X = 0;
  private static int SPAWN_Z = 0;
  private Island lastIsland;
  private List<Party> partyList;
  private static int ISLANDS_Y = 200;
  private static int ISLAND_SPACING = 100;
  
  public void onDisable()
  {
    try {
      SLAPI.save(this.playerIslands, "playerIslands.bin");
      SLAPI.save(this.lastIsland, "lastIsland.bin");
      SLAPI.save(this.orphaned, "orpahnedIslands.bin");
    } catch (Exception e) {
      System.out.println("Something went wrong saving the Island data. That's really bad but there is nothing we can really do about it. Sorry");
      e.printStackTrace();
    }
    PluginDescriptionFile pdfFile = getDescription();
    System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is now Disabled!");
  }
  //public void onEnableL()L
  {
	// Register events
	plugin = new PlayerEventListener(this);
	PluginManager pm = Bukkit.getServer().getPluginManager();
	pm.registerEvents(this.plugin, this);
    
    getCommand("newIsland").setExecutor(new CreateIslandCommand(this));
    getCommand("removeIsland").setExecutor(new RemoveIslandCommand(this));
    getCommand("home").setExecutor(new tpHomeCommand(this));
    getCommand("tphome").setExecutor(new tpHomeCommand(this));
    getCommand("skyHelp").setExecutor(new skyHelpCommand(this));
    getCommand("skydev").setExecutor(new skyDevCommand(this));
    getCommand("coop").setExecutor(new partyCommand(this));
    
    PluginDescriptionFile pdfFile = getDescription();
    
    //Load the Island data from disk
    try { 
    	if(new File("lastIsland.bin").exists())
    		lastIsland = (Island)SLAPI.load("lastIsland.bin");
    	
    	if(null == lastIsland) {
        	//in case we don't have any data on disk
        	lastIsland = new Island(); 
        	lastIsland.x = 0;
        	lastIsland.z = 0;
    	}
    	
    	//playerIslands = new HashMap<String, Island>();
    	if(new File("playerIslands.bin").exists()){
        	@SuppressWarnings("unchecked")
    		HashMap<String,Island> load = (HashMap<String,Island>)SLAPI.load("playerIslands.bin");
			if(null != load) 
				playerIslands = load;
    	}
    	
    	if(new File("orphanedIslands.bin").exists()) {
    		@SuppressWarnings("unchecked")
			Stack<Island> load = (Stack<Island>)SLAPI.load("orphanedIslands.bin");
			if(null != load)
				orphaned = load;
		}
    
    } catch (Exception e) {
		System.out.println("Could not load Island data from disk.");
		e.printStackTrace();
    }
    
    makeSpawn("skyworld");
    
    System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
  }

  public boolean hasIsland(Player player) {
    return this.playerIslands.containsKey(player.getName());
  }
  public boolean hasIsland(String playername) {
    return this.playerIslands.containsKey(playername);
  }
  public boolean hasParty(Player player) {
	return this.partyList.contains(player.getName());
  }
  public boolean hasParty(String playername) {
    return this.partyList.contains(playername);
  }  

  public boolean hasOrphanedIsland() {
    return !this.orphaned.empty();
  }
  public Island getOrphanedIsland() {
    if (hasOrphanedIsland()) {
      return (Island)this.orphaned.pop();
    }

    Island spawn = new Island();
    spawn.x = SPAWN_X;
    spawn.z = SPAWN_Z;

    return spawn;
  }

  public void clearOrphanedIsland() {
    while (hasOrphanedIsland())
      this.orphaned.pop();
  }

  public Island getPlayerIsland(String playerName)
  {
    if (hasIsland(playerName)) {
      return (Island)this.playerIslands.get(playerName);
    }
    Island spawn = new Island();
    spawn.x = SPAWN_X;
    spawn.z = SPAWN_Z;
    return spawn;
  }
  public int getISLANDS_Y() {
    return ISLANDS_Y; } 
  public Island getLastIsland() { return this.lastIsland; } 
  public void setLastIsland(Island island) { this.lastIsland = island;
    try { SLAPI.save(this.lastIsland, "lastIsland.bin"); } catch (Exception localException) {
    } } 
  public int getISLAND_SPACING() { return ISLAND_SPACING; } 
  public HashMap<String, Island> getPlayers() { return this.playerIslands; }

  public void deleteIsland(String playerName, World world) {
    if (hasIsland(playerName)) {
      Island island = getPlayerIsland(playerName);
      for (int x = island.x - 50; x < island.x + 50; x++) {
        for (int y = ISLANDS_Y - 200; y < world.getMaxHeight(); y++)
          for (int z = island.z - 50; z < island.z + 50; z++) {
            Block block = world.getBlockAt(x, y, z);
            if (block.getTypeId() != 0)
              block.setTypeId(0);
          }
      }
      this.orphaned.push(island);
      this.playerIslands.remove(playerName);
    }
  }
  public void autodeleteIsland(String playerName, World world) {
	    if (hasIsland(playerName)) {
	      Island island = getPlayerIsland(playerName);
	      for (int x = island.x - 50; x < island.x + 50; x++) {
	        for (int y = ISLANDS_Y - 200; y < world.getMaxHeight(); y++)
	          for (int z = island.z - 50; z < island.z + 50; z++) {
	            Block block = world.getBlockAt(x, y, z);
	            if (block.getTypeId() != 0)
	              block.setTypeId(0);
	          }
	      }
	      this.orphaned.push(island);
	      this.playerIslands.remove(playerName);
		  System.out.println("Player \"" + playerName + "\" island removed.");
	    }
	 }
  }
  

  public void addOrphan(Island island)
  {
    this.orphaned.push(island);
  }

  public void unregisterPlayerIsland(String player)
  {
    this.playerIslands.remove(player);
  }

  public void registerPlayerIsland(String player, Island newIsland) {
    this.playerIslands.put(player, newIsland);
    try {
      SLAPI.save(this.playerIslands, "playerIslands.bin");
      SLAPI.save(this.lastIsland, "lastIsland.bin");
      SLAPI.save(this.orphaned, "orpahnedIslands.bin");
    } catch (Exception e) {
      System.out.println("Something went wrong saving the Island data. That's really bad but there is nothing we can really do about it. Sorry");
      e.printStackTrace();
    }
  }

  public void teleportHome(Player player, World homeWorld) {
    Island home = getPlayerIsland(player.getName());
    int h = ISLANDS_Y;
    while (homeWorld.getBlockTypeIdAt(home.x, h, home.z) != 0) {
      h++;
    }
    h++;
    homeWorld.loadChunk(home.x, home.z);
    player.teleport(new Location(homeWorld, home.x, h + 2, home.z));
}
  
  public void teleportGroup(String leader, Player player, World homeWorld) {
    Island home = getPlayerIsland(leader);
    int h = ISLANDS_Y;
    while (homeWorld.getBlockTypeIdAt(home.x, h, home.z) != 0) {
      h++;
    }
    h++;
    homeWorld.loadChunk(home.x, home.z);
    player.teleport(new Location(homeWorld, home.x, h + 2, home.z));
  }
	
  public List<Party> getPartyList()
  {
    return this.partyList;
  }

  public void syncPartyList(List<Party> nList)
  {
    this.partyList = nList;
  }
  private void makeSpawn(String worldname) {
	World world = this.getServer().getWorld(worldname);
	if(null == world) {
		System.out.println("No world named \""+ worldname +"\" found, no specific spawn created");
		return;
	}
	Location spawn = world.getSpawnLocation();
	System.out.println("[skyblock] making spawn in skyworld");
	//make a platform for people to spawn on
	for(int x = (int)(spawn.getX() - 1); x < spawn.getX() + 1; x++)
		for(int z = (int) (spawn.getZ() - 1); z < spawn.getZ() + 1; z++) {	
			Block block = world.getBlockAt(x,spawn.getBlockY(),z);
			block.setTypeId(7);
		}
	
}
  public boolean onCommand(CommandSender sender, Command cmd, String commandText, String[] args) {
	    if (!(sender instanceof Player)) {
	        return false;
	      }
	    if(cmd.getName().equalsIgnoreCase("vote")){
	        sender.sendMessage("");
	        sender.sendMessage("");       
	        sender.sendMessage(ChatColor.RED + "Click both links!");
	        sender.sendMessage(ChatColor.RED + "You recieve 1 grass per vote.");      
	        sender.sendMessage(ChatColor.RED + "Make sure you type your name in correctly, and that you are logged into the server!");      
	        sender.sendMessage("");
	        sender.sendMessage("");      
	        sender.sendMessage(ChatColor.DARK_AQUA + "minecraftservers: " + ChatColor.WHITE + " http://vote2.skyblock.net ");      
	        sender.sendMessage("");      
	        sender.sendMessage("");
	        sender.sendMessage("");       
	        sender.sendMessage(ChatColor.DARK_AQUA + "minestatus: " + ChatColor.WHITE + " http://vote.skyblock.net ");
	        sender.sendMessage("");      
	        sender.sendMessage("");        
	        sender.sendMessage(ChatColor.YELLOW + "You can vote every 12 hours!"); 
	        sender.sendMessage(ChatColor.YELLOW + "Don't like grass? Trade the grass in for some cobble or iron! /warp trade");        
	        sender.sendMessage("");   
	        sender.sendMessage("");     
	      }
	    if(cmd.getName().equalsIgnoreCase("rules")){
	    sender.sendMessage(ChatColor.DARK_AQUA + "Rules:");
	    sender.sendMessage(ChatColor.WHITE + "1." + ChatColor.RED + " Do not grief, steal, or bridge to other islands. You will be immediately banned, rollbacked, and have your island removed.");
	    sender.sendMessage(ChatColor.WHITE + "2." + ChatColor.RED + " Do not ask staff for items or staff positions. ");
	    sender.sendMessage(ChatColor.WHITE + "3." + ChatColor.RED + " Do not advertise other servers. ");
	    sender.sendMessage(ChatColor.WHITE + "4." + ChatColor.RED + " Do not spam, or use offensive language.");
	    sender.sendMessage(ChatColor.WHITE + "5." + ChatColor.RED + " Do not make new islands just to trade everything on them.");
	    sender.sendMessage(ChatColor.DARK_AQUA+ " Please"+ ChatColor.RED + " be respectful" + ChatColor.DARK_AQUA +" and" + ChatColor.RED +" use common sense," + ChatColor.DARK_AQUA + " failure to follow any of these rules can result in a kick or ban, depending on what the staff member dedcides.");
	  }
	    if(cmd.getName().equalsIgnoreCase("donate")){
	        sender.sendMessage("");
	        sender.sendMessage("");
	        sender.sendMessage("");
	        sender.sendMessage("");
	        sender.sendMessage(ChatColor.GOLD + " Donators recieve an orange name, donor prefix, special commands, and a kit every 3 days. They can also bypass the player slot limit!");
	        sender.sendMessage("");      
	        sender.sendMessage(ChatColor.DARK_AQUA + "To view more information about donating click here: " + ChatColor.WHITE + "http://donate.skyblock.net");
	        sender.sendMessage("");
	        sender.sendMessage("");
	        sender.sendMessage("");
	        sender.sendMessage("");      
	        sender.sendMessage("");
	}
	    if(cmd.getName().equalsIgnoreCase("challenges")){
	    sender.sendMessage(ChatColor.RED + "Challenges (1st page):");
	    sender.sendMessage(ChatColor.WHITE + "1)" + ChatColor.DARK_AQUA + " Build a Cobble Stone generator.");
	    sender.sendMessage(ChatColor.WHITE + "2)" + ChatColor.DARK_AQUA + " Build a house");
	    sender.sendMessage(ChatColor.WHITE + "3)" + ChatColor.DARK_AQUA + " Expand the island");
	    sender.sendMessage(ChatColor.WHITE + "4)" + ChatColor.DARK_AQUA + " Make a melon farm");
	    sender.sendMessage(ChatColor.WHITE + "5)" + ChatColor.DARK_AQUA + " Make a pumpkin farm");
	    sender.sendMessage(ChatColor.WHITE + "6)" + ChatColor.DARK_AQUA + " Make a sugarcane farm");
	    sender.sendMessage(ChatColor.WHITE + "7)" + ChatColor.DARK_AQUA + " Make a wheat farm");
	    sender.sendMessage(ChatColor.WHITE + "8)" + ChatColor.DARK_AQUA + " Make a giant red mushroom");
	    sender.sendMessage(ChatColor.WHITE + "9)" + ChatColor.DARK_AQUA + " Build a bed");
	    sender.sendMessage(ChatColor.WHITE + "10)" + ChatColor.DARK_AQUA + " Make 40 stone bricks");
	    sender.sendMessage(ChatColor.WHITE + "11)" + ChatColor.DARK_AQUA + " Make atleast 20 torches");
	    sender.sendMessage(ChatColor.WHITE + "12)" + ChatColor.DARK_AQUA + " Make an infinite water source");
	    sender.sendMessage(ChatColor.DARK_AQUA + "To view the 2nd page of challenges type" + ChatColor.WHITE + " /challenges2");
	  }
	    if(cmd.getName().equalsIgnoreCase("challenges2")){
	        sender.sendMessage(ChatColor.RED + "Challenges (2nd page):");
	        sender.sendMessage(ChatColor.WHITE + "13)" + ChatColor.DARK_AQUA + " Build a furnace");
	        sender.sendMessage(ChatColor.WHITE + "14)" + ChatColor.DARK_AQUA + " Build a mob spawner");
	        sender.sendMessage(ChatColor.WHITE + "15)" + ChatColor.DARK_AQUA + " Make 10 cactus green dye");
	        sender.sendMessage(ChatColor.WHITE + "16)" + ChatColor.DARK_AQUA + " Make 10 mushroom stew");
	        sender.sendMessage(ChatColor.WHITE + "17)" + ChatColor.DARK_AQUA + " Make 10 Jack 'o' lanterns");
	        sender.sendMessage(ChatColor.WHITE + "18)" + ChatColor.DARK_AQUA + " Build 10 bookcases");
	        sender.sendMessage(ChatColor.WHITE + "19)" + ChatColor.DARK_AQUA + " Make 10 bread");
	        sender.sendMessage(ChatColor.WHITE + "20)" + ChatColor.DARK_AQUA + " Collect 10 ender-pearls");
	        sender.sendMessage(ChatColor.WHITE + "21)" + ChatColor.DARK_AQUA + " Cook 10 fish");
	        sender.sendMessage(ChatColor.WHITE + "22)" + ChatColor.DARK_AQUA + " Craft 10 black, gray, light gray, lime green, red, yellow, pink, green, and orange wool");
	        sender.sendMessage(ChatColor.WHITE + "23)" + ChatColor.DARK_AQUA + " Craft 10 snow golems");
	        sender.sendMessage(ChatColor.WHITE + "24)" + ChatColor.DARK_AQUA + " Craft 20 paintings");
	        sender.sendMessage(ChatColor.WHITE + "25)" + ChatColor.DARK_AQUA + " Make 64 bonemeal");
	      }
		return true;
	}
	}



