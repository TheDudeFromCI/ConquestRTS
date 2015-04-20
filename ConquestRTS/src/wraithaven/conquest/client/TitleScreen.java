package wraithaven.conquest.client;

import java.awt.Graphics2D;
import wraith.library.WindowUtil.GUI.GuiButton;
import wraith.library.WindowUtil.GUI.GuiFrame;
import wraith.library.WindowUtil.GUI.GuiImage;
import wraith.library.WindowUtil.GameWindow.Game;
import wraith.library.WindowUtil.GameWindow.GameRenderer;

public class TitleScreen implements GameRenderer{
	private GuiFrame gui;
	private GuiButton multiplayerButton;
	private GuiButton exitButton;
	public TitleScreen(final Game game){
		gui=new GuiFrame(game.getScreenWidth(), game.getScreenHeight());
		gui.getOffset().x=game.getRenderX();
		gui.getOffset().y=game.getRenderY();
		gui.setLayout(new TitleScreenLayout());
		multiplayerButton=new GuiButton(gui, game.getGameDataFolder().getImage("Multiplayer Button.png"), true, new Runnable(){
			public void run(){
				game.getScreen().removeUserInputListener(multiplayerButton);
				game.getScreen().removeUserInputListener(exitButton);
				gui.dispose();
				game.setGameRenderer(new ServerList(game));
			}
		});
		exitButton=new GuiButton(gui, game.getGameDataFolder().getImage("Exit Button.png"), true, new Runnable(){
			public void run(){ System.exit(0); }
		});
		game.getScreen().addUserInputListener(multiplayerButton);
		game.getScreen().addUserInputListener(exitButton);
		gui.addComponent(new GuiImage(gui, game.getGameDataFolder().getImage("Title Screen.png"), true), multiplayerButton, exitButton);
		gui.update();
	}
	public void render(Graphics2D g, int x, int y, int width, int height){ g.drawImage(gui.getPane(), x, y, null); }
}