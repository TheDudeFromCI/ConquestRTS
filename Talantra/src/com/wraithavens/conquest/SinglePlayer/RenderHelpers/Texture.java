package com.wraithavens.conquest.SinglePlayer.RenderHelpers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class Texture{
	public static void disposeAll(){
		for(int i = 0; i<textures.size(); i++)
			textures.get(i).dispose();
		textures.clear();
	}
	public static Texture getTexture(File file){
		for(int i = 0; i<textures.size(); i++)
			if(textures.get(i).file.equals(file.getAbsolutePath()))
				return textures.get(i);
		return new Texture(file);
	}
	private static ByteBuffer generatePixelBuffer(BufferedImage image){
		int[] pixels = new int[image.getWidth()*image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		ByteBuffer buffer =
			BufferUtils.createByteBuffer(image.getWidth()*image.getHeight()*Texture.BYTES_PER_PIXEL);
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
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0,
			GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, Texture.generatePixelBuffer(image));
		return textureID;
	}
	private static final int BYTES_PER_PIXEL = 4;
	private static final ArrayList<Texture> textures = new ArrayList();
	private final String file;
	private final int textureId;
	public Texture(BufferedImage buf){
		System.out.println("Loading anonomus texture.");
		textureId = Texture.loadTexture(buf);
		file = "";
	}
	private Texture(File file){
		System.out.println("Loading file "+file+".");
		textureId = Texture.loadTexture(Texture.loadImage(file));
		textures.add(this);
		this.file = file.getAbsolutePath();
	}
	public void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
	}
	public void dispose(){
		if(GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)==textureId)
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDeleteTextures(textureId);
		textures.remove(this);
		System.out.println("Disposed "+(file.isEmpty()?"Unnamed file.":file));
	}
	public void reloadTexture(BufferedImage image){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, image.getWidth(), image.getHeight(), GL11.GL_RGBA,
			GL11.GL_UNSIGNED_BYTE, generatePixelBuffer(image));
	}
}
