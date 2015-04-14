package wraithaven.conquest.client;

import wraith.library.WindowUtil.GameScreen;

public class ClientLauncher{
	public static void main(String[] args){
		RenderingPanel renderingPanel = new RenderingPanel();
		GameScreen game = new GameScreen("Wraithaven's Conquest", renderingPanel, renderingPanel);
		game.setRenderSize(4, 3, true);
		renderingPanel.setDimensions(game.getRenderSize().width, game.getRenderSize().height);
	}
}