package wraithaven.conquest.client;

import java.awt.Color;
import java.awt.Graphics2D;
import wraith.library.WindowUtil.GUI.ScrollPaneEntry;

public class ServerListSlot implements ScrollPaneEntry{
	private String ip;
	private int port;
	private boolean selected;
	private ServerList serverList;
	public ServerListSlot(ServerList serverList, String ip, int port){
		this.ip=ip;
		this.port=port;
		this.serverList=serverList;
	}
	public void renderEntry(Graphics2D g, int x, int y, int width, int height){
		g.setColor(selected?Color.red:Color.green);
		g.fillRect(x, y, width, height);
		g.setColor(Color.black);
		g.drawRect(x, y, width, height);
		g.drawString("Ip: "+ip, x+3, y+13);
		g.drawString("Port: "+port, x+3, y+26);
	}
	public void onEntryClick(){
		if(serverList.getSelectedServer()!=null)serverList.getSelectedServer().selected=false;
		selected=true;
		serverList.setSelectedServer(this);
		serverList.getScrollPanel().setNeedsRepaint();
	}
	public boolean isSelected(){ return selected; }
	public String getIp(){ return ip; }
	public int getPort(){ return port; }
}