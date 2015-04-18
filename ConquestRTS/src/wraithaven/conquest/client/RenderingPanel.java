package wraithaven.conquest.client;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.io.File;
import wraith.library.MiscUtil.ImageUtil;
import wraith.library.WorldManagement.TileGrid.Chipset;
import wraith.library.WorldManagement.TileGrid.Map;
import wraith.library.WindowUtil.GameRenderer;
import wraith.library.WindowUtil.UserInputAdapter;

public class RenderingPanel extends UserInputAdapter implements GameRenderer{
	private Map map;
	private boolean wHeld;
	private boolean aHeld;
	private boolean sHeld;
	private boolean dHeld;
	private boolean upHeld;
	private boolean downHeld;
	private int cameraPanSpeed = 100;
	public RenderingPanel(Chipset chipset, GeneratorProperties genProp){
		map=new Map(genProp.mapXSize, 2, genProp.mapZSize, new TileGenerator(genProp), chipset);
		map.setCameraScale(32);
		new RepeatingTask(0, 1){
			public void run(){
				int x = 0;
				int z = 0;
				if(wHeld)z-=cameraPanSpeed;
				if(aHeld)x-=cameraPanSpeed;
				if(sHeld)z+=cameraPanSpeed;
				if(dHeld)x+=cameraPanSpeed;
				if(x!=0||z!=0||upHeld!=downHeld){
					map.setCameraPosition(x+map.getCameraX(), z+map.getCameraZ());
					if(upHeld!=downHeld){
						if(upHeld)map.setCameraScale(map.getCameraScale()+1);
						else map.setCameraScale(map.getCameraScale()-1);
					}
					map.updateImageCompilation();
				}
			}
		};
	}
	public void setDimensions(int width, int height){
		map.setCameraDimensions(width, height);
		map.updateImageCompilation();
	}
	public void render(Graphics2D g, int x, int y, int width, int height){
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		map.render(g, x, y, width, height);
	}
	@Override public void keyPressed(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_F12)System.exit(0);
		if(e.getKeyCode()==KeyEvent.VK_F2)ImageUtil.saveImage(new File("C:/Users/Phealoon/Desktop/Conquest Folder", System.currentTimeMillis()+".png"), map.screenShot());
		if(e.getKeyCode()==KeyEvent.VK_W)wHeld=true;
		if(e.getKeyCode()==KeyEvent.VK_A)aHeld=true;
		if(e.getKeyCode()==KeyEvent.VK_S)sHeld=true;
		if(e.getKeyCode()==KeyEvent.VK_D)dHeld=true;
		if(e.getKeyCode()==KeyEvent.VK_UP)upHeld=true;
		if(e.getKeyCode()==KeyEvent.VK_DOWN)downHeld=true;
	}
	@Override public void keyReleased(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_W)wHeld=false;
		if(e.getKeyCode()==KeyEvent.VK_A)aHeld=false;
		if(e.getKeyCode()==KeyEvent.VK_S)sHeld=false;
		if(e.getKeyCode()==KeyEvent.VK_D)dHeld=false;
		if(e.getKeyCode()==KeyEvent.VK_UP)upHeld=false;
		if(e.getKeyCode()==KeyEvent.VK_DOWN)downHeld=false;
	}
}