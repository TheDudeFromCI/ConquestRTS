package wraithaven.conquest.client;

import java.awt.Color;
import java.awt.Graphics2D;
import wraith.library.WindowUtil.GUI.GuiButton;
import wraith.library.WindowUtil.GUI.GuiFrame;
import wraith.library.WindowUtil.GUI.GuiImage;
import wraith.library.WindowUtil.GUI.GuiScrollPanel;
import wraith.library.WindowUtil.GUI.ScrollPaneEntry;
import wraith.library.WindowUtil.GameWindow.Game;
import wraith.library.WindowUtil.GameWindow.GameRenderer;
import wraith.library.WindowUtil.GameWindow.RepeatingTask;

public class ServerList implements GameRenderer{
	private GuiFrame gui;
	private GuiButton backButton;
	private GuiScrollPanel list;
	public ServerList(final Game game){
		gui=new GuiFrame(game.getScreenWidth(), game.getScreenHeight());
		gui.getOffset().x=game.getRenderX();
		gui.getOffset().y=game.getRenderY();
		gui.setLayout(new ServerListLayout());
		backButton=new GuiButton(gui, game.getGameDataFolder().getImage("Server List Back Button.png"), true, new Runnable(){
			public void run(){
				game.getScreen().removeUserInputListener(backButton);
				game.getScreen().removeUserInputListener(list);
				gui.dispose();
				game.setGameRenderer(new TitleScreen(game));
			}
		});
		list=new GuiScrollPanel(gui, (int)(game.getScreenWidth()*0.9), (int)(game.getScreenHeight()*0.8), 100);
		for(int i = 0; i<7; i++){
			list.addScrollPanelEntry(new ScrollPaneEntry(){
				private Color color = Color.green;
				public void renderEntry(Graphics2D g, int x, int y, int width, int height){
					g.setColor(color);
					g.fillRect(x, y, width-1, height-1);
					g.setColor(Color.black);
					g.drawRect(x, y, width-1, height-1);
				}
				public void onEntryClick(){
					color=color==Color.green?Color.red:Color.green;
					list.setNeedsRepaint();
				}
			});
		}
		game.getScreen().addUserInputListener(backButton);
		game.getScreen().addUserInputListener(list);
		gui.addComponent(new GuiImage(gui, game.getGameDataFolder().getImage("Server List Background.png"), true), backButton, list);
		new RepeatingTask(game.getGameThread(), 1, 1){
			public int getPriority(){ return 0; }
			public void run(){
				if(gui.isDisposed())stop();
				else gui.update();
			}
		};
		gui.update();
	}
	public void render(Graphics2D g, int x, int y, int width, int height){ g.drawImage(gui.getPane(), x, y, null); }
}