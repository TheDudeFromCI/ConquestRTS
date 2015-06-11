package wraithaven.conquest.client.GameWorld.WindowUtil;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CircleButton extends GuiButton{
	private static double distanceSquared(int x1, int y1, int x2, int y2){
		return Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2);
	}
	private BufferedImage buf2;
	private boolean highlighted;
	public CircleButton(GuiContainer parent, BufferedImage buf1, BufferedImage buf2, Runnable onClick){
		super(parent, buf1, true, onClick);
		this.buf2 = buf2;
	}
	@Override public boolean isWithinBounds(Point p){
		Point off = getOffset();
		if(off==null) return CircleButton.distanceSquared(x+width/2, y+height/2, p.x, p.y)<width/2*width/2;
		return CircleButton.distanceSquared(x+width/2, y+height/2, p.x-off.x, p.y-off.y)<width/2*width/2;
	}
	@Override public void mouseMoved(MouseEvent e){
		boolean c = isWithinBounds(e.getPoint());
		if(c!=highlighted){
			highlighted = c;
			setNeedsRepaint();
		}
	}
	@Override public void render(Graphics2D g){
		g.drawImage(highlighted?buf2:buf, location.x, location.y, size.width, size.height, null);
	}
}