package wraithaven.conquest.client;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import wraith.library.WindowUtil.GUI.GuiFrame;
import wraith.library.WindowUtil.GameWindow.Game;
import wraith.library.WindowUtil.GameWindow.GameRenderer;

public class TitleScreen implements GameRenderer{
	private Game game;
	private BufferedImage bg;
	private GuiFrame gui;
	public TitleScreen(Game game){
		this.game=game;
		bg=this.game.getGameDataFolder().getImage("Title Screen.png");
		gui=new GuiFrame(game.getScreenWidth(), game.getScreenHeight());
	}
	public void render(Graphics2D g, int x, int y, int width, int height){ g.drawImage(gui.getPane(), x, y, null); }
	public GuiFrame getGui(){ return gui; }
}