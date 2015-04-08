package me.ci;

import wraith.library.WorldManagement.TileGrid.Map;
import Test.WorldPopTest;

public class Loader{
	public static void main(String[] args){
		GameWindow window = new GameWindow();
		WorldPopTest test = new WorldPopTest();
		Map map = new Map(15, 2, 15, test);
		map.setCameraScale(32);
		MapView view = new MapView(map);
		window.setPanel(view.getPanel());
	}
}