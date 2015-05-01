package wraithaven.conquest.client;

import java.awt.Point;
import java.awt.image.BufferedImage;
import wraith.library.WindowUtil.GUI.GuiButton;
import wraith.library.WindowUtil.GUI.GuiContainer;

public class CircleButton extends GuiButton{
	@Override public boolean isWithinBounds(Point p){
		Point off = getOffset();
		if(off==null)return distanceSquared(x+width/2, y+height/2, p.x, p.y)<width/2*width/2;
		return distanceSquared(x+width/2, y+height/2, p.x-off.x, p.y-off.y)<width/2*width/2;
	}
	public CircleButton(GuiContainer parent, BufferedImage buf, boolean stretch, Runnable onClick){ super(parent, buf, stretch, onClick); }
	private static double distanceSquared(int x1, int y1, int x2, int y2){ return Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2); }
}