package wraithaven.conquest.client;

import java.io.File;
import javax.imageio.ImageIO;
import wraith.library.RandomGeneration.LinearInterpolation;
import wraith.library.WindowUtil.GameScreen;
import wraith.library.WorldManagement.TileGrid.Chipset;

public class ClientLauncher{
	public static GameThread gameThread;
	public static void main(String[] args){
		try{
			GeneratorProperties genProp = new GeneratorProperties();
			genProp.heightMapSeed=1000;
			genProp.heightMapSmoothing=30;
			genProp.heightMapDetail=2;
			genProp.heightMapInterpolation=new LinearInterpolation();
			genProp.layerCount=7;
			genProp.mapHeightScaler=new MapHeightScaler(1);
			genProp.mapXSize=200;
			genProp.mapZSize=200;
			Chipset chipset = new Chipset(ImageIO.read(new File("C:/Users/Phealoon/Desktop/Conquest Folder/Terrain.png")), 32, 4);
			chipset.generateTileMaterials();
			gameThread=new GameThread();
			RenderingPanel renderingPanel = new RenderingPanel(chipset, genProp);
			GameScreen game = new GameScreen("Wraithaven's Conquest", renderingPanel, renderingPanel);
			game.setRenderSize(4, 3, true);
			renderingPanel.setDimensions(game.getRenderSize().width, game.getRenderSize().height);
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
	}
}