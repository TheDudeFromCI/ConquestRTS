package me.ci.World;

import java.awt.image.BufferedImage;

public interface TileMaterial{
	public BufferedImage getImage();
	public boolean isAnimated();
}