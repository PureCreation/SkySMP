package de.qgel.skySMP;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@SuppressWarnings("unused")
  public class CreateIslandCommand implements CommandExecutor {
	    private final skySMP plugin;
	    private List<Party> partyList;

	    public CreateIslandCommand(skySMP plugin) {
	        this.plugin = plugin;
	    }

	    @Override
	    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
	        if (!(sender instanceof Player)) {
	            return false;
	        }
	        Player player = (Player) sender;
	        
    this.partyList = this.plugin.getPartyList();
    if (hasParty(player.getName()) >= 0) {
      player.sendMessage(ChatColor.RED + "You cannot use this command while in a co-op.");
      player.sendMessage(ChatColor.RED + "Use /coop leave to start your own island.");
      return true;
    }

    if (player.getWorld().getEnvironment().getId() != 0) {
      player.sendMessage("Can only do that in the normal world, sorry");
      return true;
    }

    if (this.plugin.hasIsland(player)) {
      if (split.length == 0) {
        Island location = this.plugin.getPlayerIsland(player.getName());
        player.sendMessage("You already have an island at " + location.x + " / " + location.z + " If you want a new one, use \"/newIsland replace\" instead.");
        return true;
      }if (split[0].equals("replace")) {      
        this.plugin.deleteIsland(player.getName(), this.plugin.getServer().getWorld("skyworld"));
		
		//remove Items that drop on the island due to removal
		//List<Entity> Entities = player.getNearbyEntities(15,15,15);
		//Iterator<Entity> ent = Entities.iterator();
		//while (ent.hasNext())
			//ent.next().remove();

        return createIsland(player, this.plugin.getServer().getWorld("skyworld"));
      }if (split[0].equals("new")) {
        if (!player.isOp())
          return false;
        return createIsland(player, this.plugin.getServer().getWorld("skyworld"));
      }if (split[0].equals("test"))
      {
        return player.isOp();
      }

    }
    else
    {
      return createIsland(player, this.plugin.getServer().getWorld("skyworld"));
    }
    return false;
  }

  private boolean createIsland(Player player, World cworld) {	  
    Island last = this.plugin.getLastIsland();
    try
    {
      Island next;
      if (this.plugin.hasOrphanedIsland()) {
        next = this.plugin.getOrphanedIsland();
      } else {
        next = nextIslandLocation(last);
        this.plugin.setLastIsland(next);
        while (this.plugin.getPlayers().containsValue(next))
        {
          next = nextIslandLocation(next);
          this.plugin.setLastIsland(next);
        }
      }

      generateIslandBlocks(next.x-1, next.z-4, player, this.plugin.getServer().getWorld("skyworld"));
      this.plugin.registerPlayerIsland(player.getName(), next);
      this.plugin.teleportHome(player, this.plugin.getServer().getWorld("skyworld"));
      player.getInventory().clear();      
	  //remove Items that drop on the island due to removal
	  List<Entity> Entities = player.getNearbyEntities(15,15,15);
	  Iterator<Entity> ent = Entities.iterator();
	  while (ent.hasNext())
	      ent.next().remove();
      //if (!player.isOp())
	  //{
	        //this.plugin.addPerk(player, "");
	      //}
      //player.performCommand("");
      //player.performCommand("");
      //if (!player.isOp())
      //{
        //this.plugin.removePerk(player, "");
      //}

    } catch (Exception ex) {
      player.sendMessage("Could not create your Island. Pleace contact a server moderator.");
      this.plugin.setLastIsland(last);
      ex.printStackTrace();
      return false;
    }
    return true;
  } 
  public void generateIslandBlocks(int x, int z, Player player, World world) {
    int y = this.plugin.getISLANDS_Y();
    /** 6x6 Land Mass Middle And Bottom **/
    for(int x_operate = x; x_operate < x+3; x_operate++){
            for(int y_operate = y; y_operate < y+2; y_operate++){
                    for(int z_operate = z; z_operate < z+6; z_operate++){
                            Block blockToChange = player.getWorld().getBlockAt(x_operate,y_operate,z_operate);
                            blockToChange.setTypeId(3);               
                    }
            }
    }
    /* 6x6 Land Mass Middle And Bottom */
    /** 6x6 Land Mass Top **/
    for(int x_operate = x; x_operate < x+3; x_operate++){
            for(int y_operate = y+2; y_operate < y+3; y_operate++){
                    for(int z_operate = z; z_operate < z+6; z_operate++){
                            Block blockToChange = player.getWorld().getBlockAt(x_operate,y_operate,z_operate);
                            blockToChange.setTypeId(2);               
                    }
            }
    }
    /* 6x6 Land Mass Top */
   /** 3x3 Land Mass Middle And Bottom **/
    for(int x_operate = x+3; x_operate < x+6; x_operate++){
            for(int y_operate = y; y_operate < y+2; y_operate++){
                    for(int z_operate = z+3; z_operate < z+6; z_operate++){
                            Block blockToChange = player.getWorld().getBlockAt(x_operate,y_operate,z_operate);
                            blockToChange.setTypeId(3);    
                    }
            }
    }
    /* 3x3 Land Mass Middle And Bottom */
    /** 3x3 Land Mass Top **/
    for(int x_operate = x+3; x_operate < x+6; x_operate++){
            for(int y_operate = y+2; y_operate < y+3; y_operate++){
                    for(int z_operate = z+3; z_operate < z+6; z_operate++){
                            Block blockToChange = player.getWorld().getBlockAt(x_operate,y_operate,z_operate);
                            blockToChange.setTypeId(2);    
                    }
            }
    }
    /* 3x3 Land Mass Top */
    /** Logs **/
     for(int y_operate = y+3; y_operate < y+9; y_operate++){
             Block blockToChange = player.getWorld().getBlockAt(x+5,y_operate,z+5);
             blockToChange.setTypeId(17); 
     }
     /* Logs */
    /** Chest **/
    Block blockToChange = player.getWorld().getBlockAt(x+1,y+3,z+0);
    blockToChange.setTypeId(54);
    Chest chest = (Chest) blockToChange.getState();
    Inventory inventory = chest.getInventory();
    ItemStack item = new ItemStack(287,12); //String
    inventory.addItem(item);
    item = new ItemStack(327,1); //Bucket lava
    inventory.addItem(item);
    item = new ItemStack(352,1); //Bone
    inventory.addItem(item);
    item = new ItemStack(338,1); //Sugar Cane
    inventory.addItem(item);
    item = new ItemStack(40,1); //Mushroom red
    inventory.addItem(item);
    item = new ItemStack(79,2); //Ice
    inventory.addItem(item);
    item = new ItemStack(361,1); //pumpkin seeds
    inventory.addItem(item);
    item = new ItemStack(39,1); //mushroom brown
    inventory.addItem(item);
    item = new ItemStack(360,1); //melon slice
    inventory.addItem(item);
    item = new ItemStack(81,1); //cactus
    inventory.addItem(item);
    /* Chest */
    /** Bedrock **/
    blockToChange = player.getWorld().getBlockAt(x+1,y,z+4);
    blockToChange.setTypeId(7);
    /* Bedrock */
    /** Leaves **/
    blockToChange = player.getWorld().getBlockAt(x+4,y+6,z+3);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+5,y+6,z+3);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+6,y+6,z+3);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+3,y+7,z+3);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+4,y+7,z+3);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+5,y+7,z+3);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+6,y+7,z+3);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+7,y+7,z+3);
    blockToChange.setTypeId(18);
    
    blockToChange = player.getWorld().getBlockAt(x+3,y+6,z+4);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+4,y+6,z+4);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+5,y+6,z+4);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+6,y+6,z+4);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+7,y+6,z+4);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+3,y+7,z+4);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+4,y+7,z+4);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+5,y+7,z+4);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+6,y+7,z+4);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+7,y+7,z+4);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+4,y+8,z+4);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+5,y+8,z+4);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+6,y+8,z+4);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+5,y+9,z+4);
    blockToChange.setTypeId(18);
    
    blockToChange = player.getWorld().getBlockAt(x+3,y+6,z+5);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+4,y+6,z+5);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+6,y+6,z+5);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+7,y+6,z+5);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+3,y+7,z+5);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+4,y+7,z+5);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+6,y+7,z+5);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+7,y+7,z+5);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+4,y+8,z+5);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+6,y+8,z+5);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+4,y+9,z+5);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+5,y+9,z+5);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+6,y+9,z+5);
    blockToChange.setTypeId(18);
    
    blockToChange = player.getWorld().getBlockAt(x+3,y+6,z+6);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+4,y+6,z+6);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+5,y+6,z+6);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+6,y+6,z+6);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+7,y+6,z+6);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+3,y+7,z+6);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+4,y+7,z+6);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+5,y+7,z+6);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+6,y+7,z+6);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+7,y+7,z+6);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+5,y+8,z+6);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+6,y+8,z+6);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+5,y+9,z+6);
    blockToChange.setTypeId(18);
    
    blockToChange = player.getWorld().getBlockAt(x+4,y+6,z+7);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+5,y+6,z+7);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+6,y+6,z+7);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+3,y+7,z+7);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+4,y+7,z+7);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+5,y+7,z+7);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+6,y+7,z+7);
    blockToChange.setTypeId(18);
    blockToChange = player.getWorld().getBlockAt(x+7,y+7,z+7);
    blockToChange.setTypeId(18);
    /* Leaves */
    /** Sand **/
    blockToChange = player.getWorld().getBlockAt(x+1,y+1,z+1);
    blockToChange.setTypeId(12);
    blockToChange = player.getWorld().getBlockAt(x+1,y+1,z+4);
    blockToChange.setTypeId(12);
    blockToChange = player.getWorld().getBlockAt(x+4,y+1,z+4);
    blockToChange.setTypeId(12);
    /* Sand */
}

  private Island nextIslandLocation(Island lastIsland)
  {
    int x = lastIsland.x;
    int z = lastIsland.z;
    Island nextPos = new Island();
    nextPos.x = x;
    nextPos.z = z;
    if (x < z)
    {
      if (-1 * x < z)
      {
        nextPos.x += this.plugin.getISLAND_SPACING();
        return nextPos;
      }
      nextPos.z += this.plugin.getISLAND_SPACING();
      return nextPos;
    }
    if (x > z)
    {
      if (-1 * x >= z)
      {
        nextPos.x -= this.plugin.getISLAND_SPACING();
        return nextPos;
      }
      nextPos.z -= this.plugin.getISLAND_SPACING();
      return nextPos;
    }
    if (x <= 0)
    {
      nextPos.z += this.plugin.getISLAND_SPACING();
      return nextPos;
    }
    nextPos.z -= this.plugin.getISLAND_SPACING();
    return nextPos;
  }

  private int hasParty(String playerName)
  {
    try {
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