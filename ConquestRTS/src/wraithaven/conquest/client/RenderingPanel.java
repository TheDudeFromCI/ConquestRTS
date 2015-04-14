package wraithaven.conquest.client;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import wraith.library.WorldManagement.TileGrid.Map;
import wraith.library.WindowUtil.GameRenderer;
import wraith.library.WindowUtil.UserInputAdapter;

public class RenderingPanel extends UserInputAdapter implements GameRenderer{
	private Map map;
	public RenderingPanel(){
		map=new Map(150, 2, 150, new TileGenerator());
		map.setCameraScale(16);
	}
	public void render(Graphics2D g, int x, int y, int width, int height){
		map.render(g, x, y, width, height);
	}
	public void setDimensions(int width, int height){
		map.setCameraDimensions(width, height);
		map.updateImageCompilation();
	}
	@Override public void keyPressed(KeyEvent e){ if(e.getKeyCode()==KeyEvent.VK_F12)System.exit(0); }
}