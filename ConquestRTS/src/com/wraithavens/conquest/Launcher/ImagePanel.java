package com.wraithavens.conquest.Launcher;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

class ImagePanel extends JPanel{
	private final BufferedImage image;
	ImagePanel(BufferedImage image){
		this.image = image;
	}
	@Override
	public void paintComponent(Graphics g){
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
	}
}