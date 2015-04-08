package me.ci;

import java.io.File;
import javax.imageio.ImageIO;
import wraith.library.WorldManagement.TileGrid.Chipset;
import wraith.library.WorldManagement.TileGrid.Tile;
import wraith.library.WorldManagement.TileGrid.WorldPopulator;

public class WorldPop implements WorldPopulator{
	private Chipset chipset;
	public WorldPop(){
		try{
			chipset=new Chipset(ImageIO.read(new File("C:/Users/Phealoon/Desktop/chipset.png")), 16);
			chipset.generateTileMaterials();
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
	}
	public void generate(Tile[][][] tiles){
		for(int x = 0; x<tiles.length; x++){
			for(int y = 0; y<tiles[x].length; y++){
				for(int z = 0; z<tiles[x][y].length; z++){
					if(y==0)tiles[x][y][z]=new Tile(chipset.getTileMaterial(1, 10), x, y, z);
				}
			}
		}
	}
}