package wraithaven.conquest;

import java.awt.image.BufferedImage;

public class Misc{
	public static void compressImageToText(BufferedImage img, StringBuilder sb, char seperator){
		int width = img.getWidth();
		int height = img.getHeight();
		sb.append(width).append(seperator);
		sb.append(height).append(seperator);
		int x, y;
		for(x=0; x<width; x++)for(y=0; y<height; y++)sb.append(Integer.toHexString(img.getRGB(x, y))).append(seperator);
	}
	public static String compressImageToText(BufferedImage img, char seperator){
		StringBuilder sb = new StringBuilder();
		compressImageToText(img, sb, seperator);
		return sb.toString();
	}
}