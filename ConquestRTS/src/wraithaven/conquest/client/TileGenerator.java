package wraithaven.conquest.client;

import java.io.File;
import javax.imageio.ImageIO;
import wraith.library.WorldManagement.TileGrid.Chipset;
import wraith.library.WorldManagement.TileGrid.Tile;
import wraith.library.WorldManagement.TileGrid.WorldPopulator;

public class TileGenerator implements WorldPopulator{
	private Chipset chipset;
	public TileGenerator(){
		try{
			chipset=new Chipset(ImageIO.read(new File("C:/Users/Phealoon/Desktop/Conquest Folder/chipset.png")), 16);
			chipset.generateTileMaterials();
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
	}
	public void generate(Tile[][][] tiles){
		int x, y, z;
		for(x=0; x<tiles.length; x++){
			for(y=0; y<tiles[x].length; y++){
				for(z=0; z<tiles[x][y].length; z++){
					if(y==0)tiles[x][y][z]=new Tile(chipset.getTileMaterial(4, 10), x, y, z);
				}
			}
		}
	}
}