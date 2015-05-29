package wraithaven.conquest;

import java.awt.Graphics2D;

public interface ChannelVisualPart{
	public String compressToText();
	public void decompressFromText(String s);
	public float getHeightPercent();
	public float getWidthPercent();
	public float getXPercent();
	public float getYPercent();
	public void renderPart(Graphics2D g, int x, int y, int width, int height);
}