package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

import java.io.File;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import com.wraithavens.conquest.Launcher.WraithavensConquest;

public enum BlockTextures{
	/*
	 * For the clip types, 0 Means full biome color. 1 Means full original
	 * color. 2 Means it uses a clip map. (Half and Half.)
	 */
	Grass("Grass.png", 0),
	Dirt("Dirt.png", 1),
	SideDirt("SideDirt.png", 2),
	Water("Water0.png", 1);
	public static void bind(){
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, textureId);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	}
	public static void dispose(){
		GL11.glDeleteTextures(textureId);
	}
	public static void load(){
		int textureCount = values().length;
		final int textureSize = 32;
		ByteBuffer pixels = BufferUtils.createByteBuffer(textureSize*textureSize*textureCount*4);
		int[] pixelBuffer = new int[textureSize*textureSize];
		int[] clipPixelBuffer = new int[textureSize*textureSize];
		File file;
		String textureFolder = WraithavensConquest.assetFolder+File.separatorChar+"BlockTextures";
		for(BlockTextures t : values()){
			try{
				file = new File(textureFolder, t.name);
				ImageIO.read(file).getRGB(0, 0, textureSize, textureSize, pixelBuffer, 0, textureSize);
				if(t.clipType==2){
					file = new File(textureFolder, t.name.substring(0, t.name.length()-4)+"Clip.png");
					ImageIO.read(file).getRGB(0, 0, textureSize, textureSize, clipPixelBuffer, 0, textureSize);
				}
			}catch(Exception exception){
				exception.printStackTrace();
				throw new RuntimeException();
			}
			int x, y, p;
			for(y = 0; y<textureSize; y++){
				for(x = 0; x<textureSize; x++){
					p = pixelBuffer[y*textureSize+x];
					pixels.put((byte)(p>>16&0xFF));
					pixels.put((byte)(p>>8&0xFF));
					pixels.put((byte)(p&0xFF));
					if(t.clipType==0)
						pixels.put((byte)255);
					else if(t.clipType==1)
						pixels.put((byte)0);
					else
						pixels.put((byte)(clipPixelBuffer[y*textureSize+x]>>16&0xFF));
				}
			}
		}
		pixels.flip();
		{
			// ---
			// Build texture.
			// ---
			textureId = GL11.glGenTextures();
			GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, textureId);
			GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
			GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_NEAREST_MIPMAP_NEAREST);
			GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL12.glTexImage3D(GL30.GL_TEXTURE_2D_ARRAY, 0, GL11.GL_RGBA8, textureSize, textureSize,
				textureCount, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
		}
	}
	private static int textureId;
	private final String name;
	private final int clipType;
	private BlockTextures(String name, int clipType){
		this.name = name;
		this.clipType = clipType;
	}
	public boolean isWater(){
		return this==Water;
	}
}
