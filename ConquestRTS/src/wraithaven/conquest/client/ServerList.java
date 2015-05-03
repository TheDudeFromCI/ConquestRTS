package wraithaven.conquest.client;

import java.awt.Graphics2D;
import wraithaven.conquest.client.GameWorld.WindowUtil.GuiImage;
import wraithaven.conquest.client.GameWorld.WindowUtil.RepeatingTask;
import wraithaven.conquest.client.GameWorld.WindowUtil.Game;
import wraithaven.conquest.client.GameWorld.WindowUtil.GuiButton;
import wraithaven.conquest.client.GameWorld.WindowUtil.GuiScrollPanel;
import wraithaven.conquest.client.GameWorld.WindowUtil.GuiFrame;
import wraithaven.conquest.client.GameWorld.WindowUtil.GameRenderer;

public class ServerList implements GameRenderer{
	private GuiFrame gui;
	private GuiButton backButton;
	private GuiScrollPanel list;
	private GuiButton addServerButton;
	private GuiButton refreashButton;
	private ServerListSlot selectedServer;
	public ServerList(final Game game){
		gui=new GuiFrame(game.getScreenWidth(), game.getScreenHeight());
		gui.getOffset().x=game.getRenderX();
		gui.getOffset().y=game.getRenderY();
		gui.setLayout(new ServerListLayout());
		backButton=new GuiButton(gui, game.getFolder().getImage("Server List Back Button.png"), true, new Runnable(){
			public void run(){
				game.getScreen().removeUserInputListener(backButton);
				game.getScreen().removeUserInputListener(list);
				game.getScreen().removeUserInputListener(addServerButton);
				game.getScreen().removeUserInputListener(refreashButton);
				gui.dispose();
				game.setGameRenderer(new TitleScreen(game));
			}
		});
		list=new GuiScrollPanel(gui, (int)(game.getScreenWidth()*0.9), (int)(game.getScreenHeight()*0.8), 100);
		addServerButton=new GuiButton(gui, game.getFolder().getImage("Add Server Button.png"), true, new Runnable(){
			public void run(){
				//TODO Load actual ip and port.
				list.addScrollPanelEntry(new ServerListSlot(ServerList.this, null, "localhost", 10050));
			}
		});
		refreashButton=new GuiButton(gui, game.getFolder().getImage("Refreash Button.png"), true, new Runnable(){
			private long lastClick;
			public void run(){
				long time = System.currentTimeMillis();
				if(time-lastClick<1500)return;
				lastClick=time;
				for(int i = 0; i<list.getListSize(); i++)((ServerListSlot)list.getScrollPaneEntry(i)).refreash();
			}
		});
		game.getScreen().addUserInputListener(backButton);
		game.getScreen().addUserInputListener(list);
		game.getScreen().addUserInputListener(addServerButton);
		game.getScreen().addUserInputListener(refreashButton);
		gui.addComponent(new GuiImage(gui, game.getFolder().getImage("Server List Background.png"), true), backButton, list, addServerButton, refreashButton);
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
	public ServerListSlot getSelectedServer(){ return selectedServer; }
	public void setSelectedServer(ServerListSlot selectedServer){ this.selectedServer=selectedServer; }
	public GuiScrollPanel getScrollPanel(){ return list; }
}