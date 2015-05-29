package wraithaven.conquest;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class CvpImage implements ChannelVisualPart{
	private BufferedImage img;
	private float x,
			y,
			width,
			height;
	public CvpImage(BufferedImage img){
		this.img = img;
	}
	public String compressToText(){
		StringBuilder sb = new StringBuilder();
		Misc.compressImageToText(img, sb, '+');
		return sb.toString();
	}
	public void decompressFromText(String s){
		// TODO
	}
	public float getHeightPercent(){
		return height;
	}
	public float getWidthPercent(){
		return width;
	}
	public float getXPercent(){
		return x;
	}
	public float getYPercent(){
		return y;
	}
	public void renderPart(Graphics2D g, int x, int y, int width, int height){
		g.drawImage(img, x, y, width, height, null);
	} // TODO Add image effects and such, later on.
}