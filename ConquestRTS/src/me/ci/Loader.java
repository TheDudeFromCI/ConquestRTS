package me.ci;

import wraith.library.WorldManagement.TileGrid.Map;

public class Loader{
	public static void main(String[] args){
		GameWindow window = new GameWindow();
		WorldPop test = new WorldPop();
		Map map = new Map(20, 2, 15, test);
		map.setCameraScale(40);
		MapView view = new MapView(map);
		window.setPanel(view.getPanel());
	}
}