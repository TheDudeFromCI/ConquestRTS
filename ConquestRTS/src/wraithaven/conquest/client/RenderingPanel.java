package wraithaven.conquest.client;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import wraith.library.WorldManagement.TileGrid.Map;
import wraith.library.WindowUtil.GameRenderer;
import wraith.library.WindowUtil.UserInputAdapter;

public class RenderingPanel extends UserInputAdapter implements GameRenderer{
	private Map map;
	private boolean wHeld;
	private boolean aHeld;
	private boolean sHeld;
	private boolean dHeld;
	public RenderingPanel(){
		map=new Map(150, 2, 150, new TileGenerator());
		map.setCameraScale(16);
		new RepeatingTask(0, 1){
			public void run(){
				int x = 0;
				int z = 0;
				if(wHeld)z-=10;
				if(aHeld)x-=10;
				if(sHeld)z+=10;
				if(dHeld)x+=10;
				if(x!=0||z!=0){
					map.setCameraPosition(x+map.getCameraX(), z+map.getCameraZ());
					map.updateImageCompilation();
				}
			}
		};
	}
	public void setDimensions(int width, int height){
		map.setCameraDimensions(width, height);
		map.updateImageCompilation();
	}
	public void render(Graphics2D g, int x, int y, int width, int height){ map.render(g, x, y, width, height); }
	@Override public void keyPressed(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_F12)System.exit(0);
		if(e.getKeyCode()==KeyEvent.VK_W)wHeld=true;
		if(e.getKeyCode()==KeyEvent.VK_A)aHeld=true;
		if(e.getKeyCode()==KeyEvent.VK_S)sHeld=true;
		if(e.getKeyCode()==KeyEvent.VK_D)dHeld=true;
	}
	@Override public void keyReleased(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_W)wHeld=false;
		if(e.getKeyCode()==KeyEvent.VK_A)aHeld=false;
		if(e.getKeyCode()==KeyEvent.VK_S)sHeld=false;
		if(e.getKeyCode()==KeyEvent.VK_D)dHeld=false;
	}
}