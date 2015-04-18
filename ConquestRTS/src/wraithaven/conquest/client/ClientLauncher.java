package wraithaven.conquest.client;

import wraith.library.WindowUtil.GameScreen;

public class ClientLauncher{
	public static GameThread gameThread;
	public static void main(String[] args){
		try{
//			GeneratorProperties genProp = new GeneratorProperties();
//			genProp.heightMapSeed=1000;
//			genProp.heightMapSmoothing=30;
//			genProp.heightMapDetail=2;
//			genProp.heightMapInterpolation=new LinearInterpolation();
//			genProp.layerCount=7;
//			genProp.mapHeightScaler=new MapHeightScaler(1);
//			genProp.mapXSize=200;
//			genProp.mapZSize=200;
//			Chipset chipset = new Chipset(ImageIO.read(new File("C:/Users/Phealoon/Desktop/Conquest Folder/Terrain.png")), 32, 4);
//			chipset.generateTileMaterials();
			gameThread=new GameThread();
//			RenderingPanel renderingPanel = new RenderingPanel(chipset, genProp);
			TitleScreen titleScreen = new TitleScreen();
			GameScreen game = new GameScreen("Wraithaven's Conquest", titleScreen, titleScreen);
			game.setRenderSize(4, 3, true);
//			renderingPanel.setDimensions(game.getRenderSize().width, game.getRenderSize().height);
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
	}
}