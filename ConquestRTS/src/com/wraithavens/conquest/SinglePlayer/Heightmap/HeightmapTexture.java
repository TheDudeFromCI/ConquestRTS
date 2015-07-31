package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

class HeightmapTexture{
	private static ByteBuffer generatePixelBuffer(BufferedImage image){
		int[] pixels = new int[image.getWidth()*image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth()*image.getHeight()*4);
		int x, y;
		for(y = 0; y<image.getHeight(); y++){
			for(x = 0; x<image.getWidth(); x++){
				int pixel = pixels[y*image.getWidth()+x];
				buffer.put((byte)(pixel>>16&0xFF));
				buffer.put((byte)(pixel>>8&0xFF));
				buffer.put((byte)(pixel&0xFF));
				buffer.put((byte)(pixel>>24&0xFF));
			}
		}
		buffer.flip();
		return buffer;
	}
	private static BufferedImage loadImage(File file){
		try{
			return ImageIO.read(file);
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	private static int loadTexture(BufferedImage image){
		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0,
			GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, generatePixelBuffer(image));
		return textureID;
	}
	private final int textureId;
	public HeightmapTexture(BufferedImage buf){
		textureId = loadTexture(buf);
	}
	public HeightmapTexture(File file){
		textureId = loadTexture(loadImage(file));
	}
	public void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
	}
	public void dispose(){
		if(GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)==textureId)
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDeleteTextures(textureId);
		System.out.println("Disposed heightmap.");
	}
}
