package de.qgel.skySMP.dev;

import java.io.File;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Island{
	public int x;
	public int z;
	private File islands;
	private final List<Player> players;
	private final Player owner;
	private final List<Block> blocks;
	public Island(final List<Player> players, final Player owner, final List<Block> blocks){
		this.players = players;
		this.owner = owner;
		this.blocks = blocks;
	}
	public int getPlayerAmount(Island island){
		return players.size();
	}
	public Player getOwner(Island island){
		return owner;
	}
	public List<Player> getPlayers(Island island){
		return players;
	}
	public List<Block> getBlocks(){
		return blocks;
	}
	public void save(){
		
	}
}
