package com.wraithavens.conquest.Utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Texture;

public class TextBox extends UiElement{
	private final BufferedImage buf;
	public TextBox(int width, int height){
		super(null);
		buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		texture = new Texture(buf);
	}
	public void setText(String text){
		Graphics2D g = buf.createGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, buf.getWidth(), buf.getHeight());
		g.setColor(Color.white);
		g.drawString(text, 0, 13);
		g.dispose();
		texture.reloadTexture(buf);
	}
}
