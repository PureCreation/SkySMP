package de.qgel.skySMP.dev;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
  public class CreateIslandCommand implements CommandExecutor{
	    private final SkySMP plugin;
	    private List<Block> blocks;

	    public CreateIslandCommand(SkySMP plugin) {
	        this.plugin = plugin;
	    }

	    @Override
	    public boolean onCommand(CommandSender sender, Command command, String label, String[] split){
	        public void generateIslandBlocks(int x, int z, Player player, World world){
	            int y = this.plugin.getISLANDS_Y();
	            /** 6x6 Land Mass Middle And Bottom **/
	            for(int x_operate = x; x_operate < x+3; x_operate++){
	                    for(int y_operate = y; y_operate < y+2; y_operate++){
	                            for(int z_operate = z; z_operate < z+6; z_operate++){
	                                    Block blockToChange = player.getWorld().getBlockAt(x_operate,y_operate,z_operate);
	                                    blockToChange.setTypeId(3); 
	                                    blocks.add(blockToChange);
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
	                                    blocks.add(blockToChange);
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
	                                    blocks.add(blockToChange);
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
	                                    blocks.add(blockToChange);
	                            }
	                    }
	            }
	            /* 3x3 Land Mass Top */
	            /** Logs **/
	             for(int y_operate = y+3; y_operate < y+9; y_operate++){
	                     Block blockToChange = player.getWorld().getBlockAt(x+5,y_operate,z+5);
	                     blockToChange.setTypeId(17); 
	                     blocks.add(blockToChange);
	             }
	             /* Logs */
	            /** Chest **/
	            Block blockToChange = player.getWorld().getBlockAt(x+1,y+3,z+0);
	            blockToChange.setTypeId(54);
	            blocks.add(blockToChange);
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
}