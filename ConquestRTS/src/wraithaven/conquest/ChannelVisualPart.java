package wraithaven.conquest;

import java.awt.Graphics2D;

public interface ChannelVisualPart{
	public void renderPart(Graphics2D g, int x, int y, int width, int height);
	public float getXPercent();
	public float getYPercent();
	public float getWidthPercent();
	public float getHeightPercent();
}