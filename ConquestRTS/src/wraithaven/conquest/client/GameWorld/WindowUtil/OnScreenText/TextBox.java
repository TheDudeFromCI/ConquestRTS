package wraithaven.conquest.client.GameWorld.WindowUtil.OnScreenText;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;

public class TextBox{
	private static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
	private final Graphics2D g;
	private final BufferedImage image;
	private final Texture texture;
	private String text;
	public TextBox(String text, int width, int height, Color color, Color background){
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();
		g.setBackground(background);
		g.setColor(color);
		writeText(text);
		texture = new Texture(image, 0, null);
		texture.updatePixels(image);
	}
	public TextBox(String text, int width, int height, Color color){
		this(text, width, height, color, TRANSPARENT_COLOR);
	}
	public void dispose(){
		texture.dispose();
		g.dispose();
	}
	public void disposeGraphics(){
		g.dispose();
	}
	public Texture getTexture(){
		return texture;
	}
	public void setText(String text){
		writeText(text);
		texture.updatePixels(image);
	}
	private void writeText(String text){
		this.text = text;
		g.clearRect(0, 0, image.getWidth(), image.getHeight());
		String[] broken = text.split("\n");
		for(int i = 0; i<broken.length; i++)
			g.drawString(broken[i], 3, (i+1)*13);
	}
	public String getText(){
		return text;
	}
	public void setTextWithCursor(String text, int line, int cursorPos){
		writeText(text);
		g.drawString("|", g.getFontMetrics().stringWidth(text.substring(0, cursorPos))+1, (line+1)*13);
		texture.updatePixels(image);
	}
}