package de.qgel.skySMP;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class partyCommand
  implements CommandExecutor
{
  private final skySMP plugin;
  private List<Party> partyList;
  private Island centerIsland;
  private Party tempParty;
  private String tempLeader;
  private List<String> groupList;
  private List<Invite> inviteList;
  String tPlayer;

  public partyCommand(skySMP plugin)
  {
    this.plugin = plugin;
    this.centerIsland = new Island();
    this.centerIsland.x = 0;
    this.centerIsland.z = 0;
    this.groupList = new ArrayList<String>();
    this.partyList = new ArrayList<Party>();
    this.inviteList = new ArrayList<Invite>();
    this.inviteList.add(new Invite("NoInviter", "NoInvited"));
    try {
      if (new File("partylist.bin").exists())
      {
        @SuppressWarnings("unchecked")
		List<Party> tempPartyList = (ArrayList<Party>)SLAPI.load("partylist.bin");
        this.partyList = tempPartyList;
      }
      if (this.partyList == null)
      {
        this.partyList.add(new Party("NoLeader", "NoMember", this.centerIsland));
      }
    }
    catch (Exception e) {
      System.out.println("Something went wrong with the party data");
      e.printStackTrace();
    }
    plugin.syncPartyList(this.partyList);
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] split)
  {
    if (!(sender instanceof Player)) {
      return false;
    }
    this.plugin.syncPartyList(this.partyList);
    Player player = (Player)sender;
    if (hasParty(player.getName()) >= 0)
      this.tempParty = ((Party)this.partyList.get(hasParty(sender.getName())));
    if (hasParty(player.getName()) >= 0)
      this.tempLeader = ((Party)this.partyList.get(hasParty(sender.getName()))).getLeader();
    if (split.length == 0) {
      sender.sendMessage(ChatColor.WHITE + "/coop invite <playername>" + ChatColor.YELLOW + " to invite a player.");
      if (hasParty(player.getName()) >= 0)
      {
        player.sendMessage(ChatColor.WHITE + "/coop leave" + ChatColor.YELLOW + " leave your current group and return to spawn");
        if (this.tempParty.getLeader().equalsIgnoreCase(sender.getName()))
        {
          player.sendMessage(ChatColor.WHITE + "/coop kick <playername>" + ChatColor.YELLOW + " remove a member from your group");
          if (this.tempParty.getSize() < this.tempParty.getMax())
          {
            player.sendMessage(ChatColor.GREEN + "You can invite " + (this.tempParty.getMax() - this.tempParty.getSize()) + " more players.");
          }
          else player.sendMessage(ChatColor.RED + "You can't invite any more players.");
        }
        this.groupList = this.tempParty.getMembers();
        player.sendMessage(ChatColor.YELLOW + "Listing your group members:");
        player.sendMessage(ChatColor.GREEN + this.tempParty.getLeader() + " " + ChatColor.WHITE + this.groupList);
      }
      else if (wasInvited(sender.getName()) >= 0)
      {
        sender.sendMessage(((Invite)this.inviteList.get(wasInvited(sender.getName()))).getInviting() + " has invited you to a group.");
        sender.sendMessage(ChatColor.WHITE + "/coop [accept/reject]" + ChatColor.YELLOW + " to accept or reject the invite.");
      }
      return true;
    }if (split.length == 1) {
      if (split[0].equals("invite")) {
        player.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.WHITE + " /coop invite <playername>" + ChatColor.YELLOW + " to invite a player.");
        if (hasParty(player.getName()) < 0)
        {
          return true;
        }
        if (((Party)this.partyList.get(hasParty(sender.getName()))).getLeader().equalsIgnoreCase(sender.getName()))
        {
          if (hasParty(sender.getName()) >= 0)
          {
            if (this.tempParty.getSize() < this.tempParty.getMax())
            {
              sender.sendMessage(ChatColor.GREEN + "You can invite " + (this.tempParty.getMax() - this.tempParty.getSize()) + " more players.");
            }
            else sender.sendMessage(ChatColor.RED + "You can't invite any more players."); 
          }
          else
            sender.sendMessage(ChatColor.GREEN + "You can invite 3 players.");
          return true;
        }

        player.sendMessage(ChatColor.RED + "Only the group leader can invite!");
        return true;
      }
      if (split[0].equals("accept")) {	
	    String playerName = player.getName();    	  
		if (hasParty(playerName) < 0 && wasInvited(playerName) >= 0) { // check to see if the player is NOT in a party and has been invited by someone
			if (hasParty(inviteList.get(wasInvited(playerName)).getInviting()) == -1) { // check to see if the player who invited has a party, if not then create a new one
				/* add a new party to the party list with the inviter as the leader, and the inviter's island as the party's island */     	  
        	  
		        List<Party> partyList = plugin.getPartyList();

				int playerInvited = wasInvited(playerName);
				if (playerInvited < 0) {
					player.sendMessage("You weren't invited");
					return true;
				}

				String partyLeader = inviteList.get(playerInvited).getInviting();
				if (partyLeader == null) {
					player.sendMessage("Error finding co-op leader");
					return true;
				}

				Island leaderIsland = plugin.getPlayerIsland(partyLeader);
				if (leaderIsland == null) {
					player.sendMessage("Error finding co-op leader's island");
					return true;
				}		        						        
		  
				partyList.add(new Party(partyLeader, playerName, leaderIsland));

				player.sendMessage(ChatColor.GREEN + "Request accepted!"); // send a message to the invited player
				if (Bukkit.getPlayer(inviteList.get(wasInvited(player.getName())).getInviting()) != null)
					/* send a message to the inviter if he is online (he should be, but this is just a fail-safe to avoid any null pointer exceptions) */
					Bukkit.getPlayer(inviteList.get(wasInvited(player.getName())).getInviting()).sendMessage(ChatColor.GREEN + player.getName() + " has joined your island!");
			} else { // if the inviter already has a party, add the invited person to it
				if (plugin.getPartyList().get(hasParty(inviteList.get(wasInvited(player.getName())).getInviting())).addMember(player.getName())) { // try to add the person into the group, if
																																					// this fails it will return false
					player.sendMessage(ChatColor.GREEN + "Request accepted!");
					if (Bukkit.getPlayer(inviteList.get(wasInvited(player.getName())).getInviting()) != null)
						/* send a message to the inviter if he is online (he should be, but this is just a fail-safe to avoid any null pointer exceptions) */
						Bukkit.getPlayer(inviteList.get(wasInvited(player.getName())).getInviting()).sendMessage(ChatColor.GREEN + player.getName() + " has joined your island!");
				} else { // if there was a problem adding the person, let them know
					player.sendMessage(ChatColor.RED + "You couldn't join the co-op, maybe it's full.");
					return true;
				}
			}
			
		  plugin.teleportGroup(inviteList.get(wasInvited(player.getName())).getInviting(), player, plugin.getServer().getWorld("skyworld"));
          player.getInventory().clear();
          if (this.plugin.hasIsland(sender.getName()))
            this.plugin.deleteIsland(sender.getName(), this.plugin.getServer().getWorld("skyworld"));        
		  inviteList.remove(wasInvited(player.getName())); // remove the invite from the invite list          
          this.plugin.syncPartyList(this.partyList);
          try {
            SLAPI.save(this.partyList, "partylist.bin");
          } catch (Exception e) {
            System.out.println("Couldn't add to group!");
            e.printStackTrace();
          }
          return true;
        }
		int playerInvited = wasInvited(player.getName());
		if (playerInvited < 0) {		
        player.sendMessage(ChatColor.RED + "You haven't been invited.");
        return true;
      }  
		String partyLeader = inviteList.get(playerInvited).getInviting();
		String partyLeaderIsland;
		if (partyLeader == null) {
			player.sendMessage("Error finding co-op leader island");
			return true;
		} else {
			partyLeaderIsland = partyLeader + "Island";
		}
      }
    

      if (split[0].equals("reject")) {
        if (wasInvited(sender.getName()) >= 0)
          this.inviteList.remove(wasInvited(sender.getName()));       
        else
          player.sendMessage(ChatColor.RED + "You haven't been invited.");
        return true;
        
	  }if (split[0].equalsIgnoreCase("partydebug")) { // display a list of all the parties
		if (player.isOp()) {
			player.sendMessage(ChatColor.RED + "Checking Parties.");
			partyDebug(player, "none");
		} else
			player.sendMessage(ChatColor.RED + "You can't access that command!");
		return true;     
			
      }if (split[0].equalsIgnoreCase("invitedebug")) {
        if (player.isOp())
        {
          player.sendMessage(ChatColor.RED + "Checking Invites.");
          inviteDebug(player);
        } else {
          player.sendMessage(ChatColor.RED + "You can't access that command!");
        }return true;        
        
      }if (split[0].equals("leave"))
        if (hasParty(sender.getName()) >= 0)
        {
          this.tempParty = ((Party)this.partyList.get(hasParty(sender.getName())));
          if (((Party)this.partyList.get(hasParty(sender.getName()))).removeMember(sender.getName()) == 0)
          {
            try {
              this.groupList = this.tempParty.getMembers();
              Iterator<String> ent = this.groupList.iterator();           
              
                player.getInventory().clear();
           	    player.teleport(Bukkit.getServer().getWorld("skyworld").getSpawnLocation());      
                if (this.plugin.hasIsland(sender.getName()))
                this.plugin.deleteIsland(sender.getName(), this.plugin.getServer().getWorld("skyworld"));
                {
                }

              this.partyList.remove(hasParty(sender.getName()));
              this.plugin.syncPartyList(this.partyList);
              SLAPI.save(this.partyList, "partylist.bin");
            } catch (Exception e) {
              System.out.println("Couldn't teleport");
              e.printStackTrace();
            }
            return true;
          }

          try
          {
            player.getInventory().clear();        	  
   	    	player.teleport(Bukkit.getServer().getWorld("skyworld").getSpawnLocation());
            SLAPI.save(this.partyList, "partylist.bin");
          }
          catch (Exception e) {
            System.out.println("Couldn't teleport");
            e.printStackTrace();
          }
        }
        else
        {
          player.sendMessage(ChatColor.RED + "You can't use that command right now.");
          return true;
        }
    }    
    
    else if (split.length == 2) {
      if (split[0].equalsIgnoreCase("invite"))
      {
        if (hasParty(split[1]) < -1)
        {
          player.sendMessage(ChatColor.RED + "That player is offline or doesn't exist.");
          return true;
        }
        if (!this.plugin.hasIsland(player))
        {
          player.sendMessage(ChatColor.RED + "You must have an island to create a co-op.");
          return true;
        }
        if (player.getName().equalsIgnoreCase(split[1]))
        {
          player.sendMessage(ChatColor.RED + "You can't invite yourself, silly goose!");
          return true;
        }
        if (hasParty(sender.getName()) >= 0)
        {
          if (((Party)this.partyList.get(hasParty(sender.getName()))).getLeader().equalsIgnoreCase(sender.getName()))
          {
            if (hasParty(split[1]) == -1)
            {
              if (this.tempParty.getSize() < this.tempParty.getMax())
              {
                if (hasInvited(player.getName()) > 0)	
                {
                  this.inviteList.remove(hasInvited(player.getName()));
                  player.sendMessage(ChatColor.YELLOW + "Removing your previous invite.");
                }
                this.inviteList.add(new Invite(split[1], player.getName()));
                Bukkit.getPlayer(split[1]).sendMessage(player.getName() + " has invited you to join a co-op!");
                Bukkit.getPlayer(split[1]).sendMessage(ChatColor.WHITE + "/coop [accept/reject]" + ChatColor.YELLOW + " to accept or reject the invite.");
                Bukkit.getPlayer(split[1]).sendMessage(ChatColor.RED + "WARNING: You will lose your current island!");
              } else {
                sender.sendMessage(ChatColor.RED + "You can't invite any more players.");
              }
            } else sender.sendMessage(ChatColor.RED + "You can't invite that player.");
            return true;
          }
        } else {
          if (hasParty(sender.getName()) == -1)
          {
            if (hasParty(split[1]) == -1)
            {
              if (hasInvited(player.getName()) > 0)	  
              {
                this.inviteList.remove(hasInvited(player.getName()));
                player.sendMessage(ChatColor.YELLOW + "Removing your previous invite.");
              }
              this.inviteList.add(new Invite(split[1], player.getName()));
              Bukkit.getPlayer(split[1]).sendMessage(player.getName() + " has invited you to join a co-op!");
              Bukkit.getPlayer(split[1]).sendMessage(ChatColor.WHITE + "/coop [accept/reject]" + ChatColor.YELLOW + " to accept or reject the invite.");
            } else {
              sender.sendMessage(ChatColor.RED + "You can't invite that player.");
            }return true;
          }

          player.sendMessage(ChatColor.RED + "Only the group leader can invite!");
          return true;
        }
  	} else if (split[0].equalsIgnoreCase("kick"))
		{  // if the player uses /party remove [player] *** split[1] is the invited player
  		if (hasParty(player.getName()) >= 0)
  		{ // check to see if the player is in a party
  			if (tempLeader.equalsIgnoreCase(player.getName()))
		   		{ // check to see if the player is the party leader
  				if (tempParty.hasMember(split[1]))
			   		{ // check to see if the removed player is in the removing player's party
  					if (player.getName().equalsIgnoreCase(split[1]))
  					{ // check to see if the player is trying to remove himself
  						player.sendMessage(ChatColor.RED + "Use /coop leave to leave the group.");
  						return true;
  					}
			}
            if (((Party)this.partyList.get(hasParty(sender.getName()))).removeMember(split[1]) > 1)
            {
              try
              {  
				if (Bukkit.getPlayer(split[1]) != null)
					{ // check to see if the player is online, if they are then remove their stuff and teleport them
						Bukkit.getPlayer(split[1]).getInventory().clear();
						Bukkit.getPlayer(split[1]).teleport(Bukkit.getServer().getWorld("skyworld").getSpawnLocation());
				}
		        if (Bukkit.getPlayer(tempLeader) != null)
				    Bukkit.getPlayer(tempLeader).sendMessage(ChatColor.RED + player.getName() +" has been removed from the group.");
                this.plugin.syncPartyList(this.partyList);
                SLAPI.save(this.partyList, "partylist.bin");
              }
              catch (Exception e) {
                System.out.println("Couldn't teleport");
                e.printStackTrace();
              }
            }
          }
          return true;
        }

        player.sendMessage(ChatColor.RED + "Only the group leader can kick!");
        return true;
      }

    }

    return true;
  }
    

  private int hasParty(String playerName)
  {
    try {
      if (Bukkit.getPlayer(playerName).isOnline())
      {
        for (int i = 0; i < this.partyList.size(); i++)
        {
          if (((Party)this.partyList.get(i)).hasMember(playerName))
            return i;
        }
        return -1;
      }
      return -2; } catch (Exception e) {
    }
    return -2;
  }

  private int wasInvited(String playerName)
  {
    for (int i = 0; i < this.inviteList.size(); i++)
      if (((Invite)this.inviteList.get(i)).getInvited().equalsIgnoreCase(playerName))
        return i;
    return -1;
  }

  private int hasInvited(String playerName)
  {
    for (int i = 0; i < this.inviteList.size(); i++)
      if (((Invite)this.inviteList.get(i)).getInviting().equalsIgnoreCase(playerName))
        return i;
    return -1;
  }

  private void inviteDebug(Player player)
  {
    for (int i = 0; i < this.inviteList.size(); i++)
    {
      player.sendMessage("Inviting: " + ((Invite)this.inviteList.get(i)).getInviting() + " Invited: " + ((Invite)this.inviteList.get(i)).getInvited());
    }
  }

	private void partyDebug(Player player, String name) { // display the entire list or the party of a given player
		if (name.equalsIgnoreCase("none")) { // if 'none' is given as the player name, show the entire list (large lists will be clipped)
			for (int i = 0; i < plugin.getPartyList().size(); i++) {
				player.sendMessage("Leader: " + plugin.getPartyList().get(i).getLeader() + " Members: " + plugin.getPartyList().get(i).getMembers());
			}
		} else { // display the party information for the given player
			if (hasParty(name.toLowerCase()) >= 0)
				player.sendMessage("Leader: " + plugin.getPartyList().get(hasParty(name.toLowerCase())).getLeader() + " Members: "
						+ plugin.getPartyList().get(hasParty(name.toLowerCase())).getMembers());
			else
				player.sendMessage("Invalid player, or Error");
		}
	}
  
  public List<Party> getPartyList()
  {
    return this.partyList;
  }
}