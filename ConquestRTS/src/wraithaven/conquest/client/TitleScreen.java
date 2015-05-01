package wraithaven.conquest.client;

import java.awt.Graphics2D;
import wraith.library.WindowUtil.GUI.GuiFrame;
import wraith.library.WindowUtil.GUI.GuiImage;
import wraith.library.WindowUtil.GameWindow.Game;
import wraith.library.WindowUtil.GameWindow.GameRenderer;

public class TitleScreen implements GameRenderer{
	private GuiFrame gui;
	private CircleButton singlePlayerButton;
	private CircleButton multiplayerButton;
	private CircleButton settingsButton;
	private CircleButton toolboxButton;
	private CircleButton exitGameButton;
	public TitleScreen(final Game game){
		gui=new GuiFrame(game.getScreenWidth(), game.getScreenHeight());
		gui.getOffset().x=game.getRenderX();
		gui.getOffset().y=game.getRenderY();
		gui.setLayout(new TitleScreenLayout());
		singlePlayerButton=new CircleButton(gui, game.getFolder().getImage("Single Player Button.png"), true, new Runnable(){
			public void run(){
				//TODO
			}
		});
		multiplayerButton=new CircleButton(gui, game.getFolder().getImage("Multiplayer Button.png"), true, new Runnable(){
			public void run(){
				game.getScreen().removeUserInputListener(singlePlayerButton);
				game.getScreen().removeUserInputListener(multiplayerButton);
				game.getScreen().removeUserInputListener(settingsButton);
				game.getScreen().removeUserInputListener(toolboxButton);
				game.getScreen().removeUserInputListener(exitGameButton);
				gui.dispose();
				game.setGameRenderer(new ServerList(game));
			}
		});
		settingsButton=new CircleButton(gui, game.getFolder().getImage("Settings Button.png"), true, new Runnable(){
			public void run(){
				//TODO
			}
		});
		toolboxButton=new CircleButton(gui, game.getFolder().getImage("Toolbox Button.png"), true, new Runnable(){
			public void run(){
				//TOOD
			}
		});
		exitGameButton=new CircleButton(gui, game.getFolder().getImage("Exit Game Button.png"), true, new Runnable(){
			public void run(){ System.exit(0); }
		});
		game.getScreen().addUserInputListener(singlePlayerButton);
		game.getScreen().addUserInputListener(multiplayerButton);
		game.getScreen().addUserInputListener(settingsButton);
		game.getScreen().addUserInputListener(toolboxButton);
		game.getScreen().addUserInputListener(exitGameButton);
		gui.addComponent(new GuiImage(gui, game.getFolder().getImage("Title Screen.png"), true), singlePlayerButton, multiplayerButton, settingsButton, toolboxButton, exitGameButton);
		gui.update();
	}
	public void render(Graphics2D g, int x, int y, int width, int height){ g.drawImage(gui.getPane(), x, y, null); }
}