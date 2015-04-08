package me.ci;

import wraith.library.WorldManagement.TileGrid.Chipset;
import wraith.library.WorldManagement.TileGrid.Tile;
import wraith.library.WorldManagement.TileGrid.WorldPopulator;

public class WorldPop implements WorldPopulator{
	private Chipset chipset;
	public WorldPop(Chipset chipset){ this.chipset=chipset; }
	public void generate(Tile[][][] tiles){ for(int x = 0; x<tiles.length; x++)for(int z = 0; z<tiles[x][0].length; z++)tiles[x][0][z]=new Tile(chipset.getTileMaterial(1, 10), x, 0, z); }
}