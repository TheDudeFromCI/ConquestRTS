package wraithaven.conquest.client.GameWorld.WindowUtil.OnScreenText;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;

public class TextBox{
	private final Texture texture;
	private final BufferedImage image;
	private final Graphics2D g;
	private static final Color TRANSPARENT_COLOR = new Color(255, 255, 255, 255);
	public TextBox(String text, int width, int height, Color color){
		image=new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g=image.createGraphics();
		g.setBackground(TRANSPARENT_COLOR);
		g.setColor(color);
		writeText(text);
		texture=new Texture(image, 0, null);
	}
	private void writeText(String text){
		g.clearRect(0, 0, image.getWidth(), image.getHeight());
		String[] broken = text.split("\n");
		for(int i = 0; i<broken.length; i++)g.drawString(broken[i], 3, (i+1)*13);
	}
	public void dispose(){
		texture.dispose();
		g.dispose();
	}
	public void setText(String text){
		writeText(text);
		texture.updatePixels(image);
	}
	public Texture getTexture(){ return texture; }
}