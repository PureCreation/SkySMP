package de.qgel.skySMP.dev;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

import org.bukkit.plugin.java.JavaPlugin;

public class SkySMP extends JavaPlugin{
	  private Stack<Island> orphaned = new Stack();
	  private static int SPAWN_X = 0;
	  private static int SPAWN_Z = 0;
	  private Island lastIsland;
	  private static int ISLANDS_Y = 200;
	  private static int ISLAND_SPACING = 100;
	  private File islands;
	public void onDisable()
	  {
		
	  }
	  public void onEnable()
	  {
		  if(!islands.exists()){
			  try{
				  islands.createNewFile();
			  }
			  catch(IOException e){
				  e.printStackTrace();
			  }
		  }
	  }
	  public int getISLANDS_Y() {
		    return ISLANDS_Y; } 
		  public Island getLastIsland() { return this.lastIsland; } 
		  public int getISLAND_SPACING() { return ISLAND_SPACING; }
}
