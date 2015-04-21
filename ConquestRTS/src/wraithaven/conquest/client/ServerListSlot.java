package wraithaven.conquest.client;

import java.awt.Color;
import java.awt.Graphics2D;
import wraith.library.WindowUtil.GUI.ScrollPaneEntry;

public class ServerListSlot implements ScrollPaneEntry{
	private String ip;
	private int port;
	private boolean selected;
	private long clickTime;
	private int refreashCount;
	private ServerList serverList;
	public ServerListSlot(ServerList serverList, String ip, int port){
		this.ip=ip;
		this.port=port;
		this.serverList=serverList;
		refreash();
	}
	public void renderEntry(Graphics2D g, int x, int y, int width, int height){
		g.setColor(selected?Color.red:Color.green);
		g.fillRect(x, y, width, height);
		g.setColor(Color.black);
		g.drawRect(x, y, width, height);
		g.drawString("Ip: "+ip, x+3, y+13);
		g.drawString("Port: "+port, x+3, y+26);
		g.drawString("Refreash Count: "+refreashCount, x+3, y+39);
	}
	public void onEntryClick(){
		long time = System.currentTimeMillis();
		if(selected&&time-clickTime<600){
			//TODO Load server.
			return;
		}
		if(serverList.getSelectedServer()!=null)serverList.getSelectedServer().selected=false;
		selected=true;
		clickTime=time;
		serverList.setSelectedServer(this);
		repaint();
	}
	public void refreash(){
		refreashCount++;
		repaint();
	}
	public boolean isSelected(){ return selected; }
	public String getIp(){ return ip; }
	public int getPort(){ return port; }
	private void repaint(){ serverList.getScrollPanel().setNeedsRepaint(); }
}