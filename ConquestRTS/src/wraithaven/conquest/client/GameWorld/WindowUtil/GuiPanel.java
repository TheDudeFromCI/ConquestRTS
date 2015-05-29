package wraithaven.conquest.client.GameWorld.WindowUtil;

import java.awt.Graphics2D;

public class GuiPanel extends GuiContainer{
	public GuiPanel(GuiContainer parent, int bufferWidth, int bufferHeight){
		super(parent, bufferWidth, bufferHeight);
	}
	@Override public void render(Graphics2D g){
		GuiComponent c;
		for(int i = 0; i<components.size(); i++){
			c = components.get(i);
			c.update();
			g.drawImage(c.getPane(), c.x, c.y, c.width, c.height, null);
		}
	}
}