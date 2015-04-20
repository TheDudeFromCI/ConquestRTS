package wraithaven.conquest.client;

import java.awt.Graphics2D;
import wraith.library.WindowUtil.GUI.GuiButton;
import wraith.library.WindowUtil.GUI.GuiFrame;
import wraith.library.WindowUtil.GUI.GuiImage;
import wraith.library.WindowUtil.GameWindow.Game;
import wraith.library.WindowUtil.GameWindow.GameRenderer;

public class ServerList implements GameRenderer{
	private GuiFrame gui;
	private GuiButton backButton;
	public ServerList(final Game game){
		gui=new GuiFrame(game.getScreenWidth(), game.getScreenHeight());
		gui.getOffset().x=game.getRenderX();
		gui.getOffset().y=game.getRenderY();
		gui.setLayout(new ServerListLayout());
		backButton=new GuiButton(gui, game.getGameDataFolder().getImage("Server List Back Button.png"), true, new Runnable(){
			public void run(){
				game.getScreen().removeUserInputListener(backButton);
				gui.dispose();
				game.setGameRenderer(new TitleScreen(game));
			}
		});
		game.getScreen().addUserInputListener(backButton);
		gui.addComponent(new GuiImage(gui, game.getGameDataFolder().getImage("Server List Background.png"), true), backButton);
		gui.update();
	}
	public void render(Graphics2D g, int x, int y, int width, int height){ g.drawImage(gui.getPane(), x, y, null); }
}