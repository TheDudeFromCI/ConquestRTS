package me.ci;

import java.io.File;
import javax.imageio.ImageIO;
import wraith.library.WorldManagement.TileGrid.MapView;
import wraith.library.WorldManagement.TileGrid.Chipset;
import wraith.library.WorldManagement.TileGrid.Map;

public class Loader{
	public static void main(String[] args){
		GameWindow window = new GameWindow();
		Chipset chipset = null;
		try{
			chipset=new Chipset(ImageIO.read(new File("C:/Users/Phealoon/Desktop/chipset.png")), 16);
			chipset.generateTileMaterials();
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
		WorldPop test = new WorldPop(chipset);
		Map map = new Map(20, 16, 15, test);
		map.setCameraScale(40);
		MapView view = new MapView(map);
		window.setPanel(view.getPanel());
	}
}