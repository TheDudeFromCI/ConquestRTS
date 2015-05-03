package wraithaven.conquest.client;

import java.awt.Graphics2D;
import wraithaven.conquest.client.GameWorld.WindowUtil.RepeatingTask;
import wraithaven.conquest.client.GameWorld.WindowUtil.Game;
import wraithaven.conquest.client.GameWorld.WindowUtil.GuiImage;
import wraithaven.conquest.client.GameWorld.WindowUtil.GuiFrame;
import wraithaven.conquest.client.GameWorld.WindowUtil.GameRenderer;
import wraithaven.conquest.client.GameWorld.WindowUtil.CircleButton;
import wraithaven.conquest.client.BuildingCreator.BuildingCreator;

public class TitleScreen implements GameRenderer{
	private GuiFrame gui;
	private CircleButton singlePlayerButton;
	private CircleButton multiplayerButton;
	private CircleButton settingsButton;
	private CircleButton toolboxButton;
	private CircleButton exitGameButton;
	private GuiImage singlePlayerWords;
	private GuiImage multiplayerWords;
	private GuiImage settingsWords;
	private GuiImage toolboxWords;
	private GuiImage exitGameWords;
	public TitleScreen(final Game game){
		gui=new GuiFrame(game.getScreenWidth(), game.getScreenHeight());
		gui.getOffset().x=game.getRenderX();
		gui.getOffset().y=game.getRenderY();
		gui.setLayout(new TitleScreenLayout());
		singlePlayerButton=new CircleButton(gui, game.getFolder().getImage("Single Player Button.png"), game.getFolder().getImage("Single Player Button Down.png"), new Runnable(){
			public void run(){
				//TODO
			}
		});
		multiplayerButton=new CircleButton(gui, game.getFolder().getImage("Multiplayer Button.png"), game.getFolder().getImage("Multiplayer Button Down.png"), new Runnable(){
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
		settingsButton=new CircleButton(gui, game.getFolder().getImage("Settings Button.png"), game.getFolder().getImage("Settings Button Down.png"), new Runnable(){
			public void run(){
				//TODO
			}
		});
		toolboxButton=new CircleButton(gui, game.getFolder().getImage("Toolbox Button.png"), game.getFolder().getImage("Toolbox Button Down.png"), new Runnable(){
			public void run(){
				gui.dispose();
				ClientLauncher.game.getScreen().dispose();
				new BuildingCreator().launch();
			}
		});
		exitGameButton=new CircleButton(gui, game.getFolder().getImage("Exit Game Button.png"), game.getFolder().getImage("Exit Game Button Down.png"), new Runnable(){
			public void run(){ System.exit(0); }
		});
		singlePlayerWords=new GuiImage(gui, game.getFolder().getImage("Single Player Words.png"), true);
		multiplayerWords=new GuiImage(gui, game.getFolder().getImage("Multiplayer Words.png"), true);
		settingsWords=new GuiImage(gui, game.getFolder().getImage("Settings Words.png"), true);
		toolboxWords=new GuiImage(gui, game.getFolder().getImage("Toolbox Words.png"), true);
		exitGameWords=new GuiImage(gui, game.getFolder().getImage("Exit Game Words.png"), true);
		game.getScreen().addUserInputListener(singlePlayerButton);
		game.getScreen().addUserInputListener(multiplayerButton);
		game.getScreen().addUserInputListener(settingsButton);
		game.getScreen().addUserInputListener(toolboxButton);
		game.getScreen().addUserInputListener(exitGameButton);
		gui.addComponent(new GuiImage(gui, game.getFolder().getImage("Title Screen.png"), true), singlePlayerButton, multiplayerButton, settingsButton, toolboxButton, exitGameButton, singlePlayerWords, multiplayerWords, settingsWords, toolboxWords, exitGameWords);
		new RepeatingTask(game.getGameThread(), 1, 1){
			public void run(){
				if(gui.isDisposed())stop();
				else gui.update();
			}
			public int getPriority(){ return 0; }
		};
		gui.update();
	}
	public void render(Graphics2D g, int x, int y, int width, int height){ g.drawImage(gui.getPane(), x, y, null); }
}