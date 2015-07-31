package com.wraithavens.conquest.SinglePlayer.RenderHelpers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;

public class Texture{
	public static void disposeAll(){
		for(int i = 0; i<textures.size(); i++)
			textures.get(i).dispose();
		textures.clear();
	}
	public static Texture getTexture(String folder, String file){
		return Texture.getTexture(folder, file, 0);
	}
	public static Texture getTexture(String folder, String file, int mipmapping){
		for(int i = 0; i<textures.size(); i++)
			if(textures.get(i).file.equals(file)&&textures.get(i).folder.equals(folder))
				return textures.get(i);
		return new Texture(folder, new File(folder, file), mipmapping);
	}
	private static ByteBuffer generatePixelBuffer(BufferedImage image){
		int[] pixels = new int[image.getWidth()*image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		ByteBuffer buffer =
			BufferUtils.createByteBuffer(image.getWidth()*image.getHeight()*Texture.BYTES_PER_PIXEL);
		int x, y;
		byte b;
		Texture.TRANSPARENT_RETURN[0] = false;
		for(y = 0; y<image.getHeight(); y++){
			for(x = 0; x<image.getWidth(); x++){
				int pixel = pixels[y*image.getWidth()+x];
				buffer.put((byte)(pixel>>16&0xFF));
				buffer.put((byte)(pixel>>8&0xFF));
				buffer.put((byte)(pixel&0xFF));
				buffer.put(b = (byte)(pixel>>24&0xFF));
				if(b!=-1)
					Texture.TRANSPARENT_RETURN[0] = true;
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
	private static int loadTexture(BufferedImage image, int mipmapLevel){
		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		if(mipmapLevel>0){
			// Quality levels: (Lowest to highest.)
			// GL_NEAREST_MIPMAP_NEAREST Choose the nearest mipmap, and nearest
			// pixel on that mipmap.
			// GL_LINEAR_MIPMAP_NEAREST Interpolate mipmap, and nearest pixel on
			// that mipmap.
			// GL_NEAREST_MIPMAP_LINEAR Choose the nearest mipmap, and
			// interpolate pixel on that mipmap.
			// GL_LINEAR_MIPMAP_LINEAR Interpolate mipmap, and interpolate pixel
			// on that mipmap.
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, mipmapLevel-1);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
		}else{
			// Quality levels: (Lowest to highest.)
			// GL_NEAREST Get nearest pixel.
			// GL_LINEAR Interpolate pixels.
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		}
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0,
			GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, Texture.generatePixelBuffer(image));
		return textureID;
	}
	private static final int BYTES_PER_PIXEL = 4;
	private static final ArrayList<Texture> textures = new ArrayList();
	private static final boolean[] TRANSPARENT_RETURN = new boolean[1];
	private final String file;
	private final String folder;
	private final int textureId;
	public Texture(BufferedImage image){
		textureId = Texture.loadTexture(image, 0);
		textures.add(this);
		file = "";
		folder = "";
	}
	private Texture(String folder, File file, int mipmapLevel){
		textureId = Texture.loadTexture(Texture.loadImage(file), mipmapLevel);
		textures.add(this);
		this.file = file.getName();
		this.folder = folder;
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
	public int getId(){
		return textureId;
	}
}
