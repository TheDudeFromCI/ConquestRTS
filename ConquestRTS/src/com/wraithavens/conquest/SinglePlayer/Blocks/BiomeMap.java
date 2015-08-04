package com.wraithavens.conquest.SinglePlayer.Blocks;

import java.io.File;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.Utility.BinaryFile;

public class BiomeMap{
	private static int build(ByteBuffer colors){
		int id = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, TextureSize, TextureSize, 0, GL11.GL_RGB,
			GL11.GL_UNSIGNED_BYTE, colors);
		return id;
	}
	private static float clamp(float a, float min, float max){
		return a<min?min:a>max?max:a;
	}
	private static byte colorToByte(float f){
		f = clamp(f, 0.0f, 1.0f);
		return (byte)(f==1.0f?255:(int)(f*256));
	}
	private static int generateTexture(WorldNoiseMachine machine, File file, int posX, int posZ){
		ByteBuffer colors = BufferUtils.createByteBuffer(TextureSize*TextureSize*3);
		int x, z;
		int maxX = posX+TextureSize;
		int maxZ = posZ+TextureSize;
		Vector3f color = new Vector3f();
		byte b;
		BinaryFile bin = new BinaryFile(colors.capacity());
		for(z = posZ; z<maxZ; z++)
			for(x = posX; x<maxX; x++){
				machine.getPrairieColor(x, z, color);
				colors.put(b = colorToByte(color.x));
				bin.addByte(b);
				colors.put(b = colorToByte(color.y));
				bin.addByte(b);
				colors.put(b = colorToByte(color.z));
				bin.addByte(b);
			}
		bin.compile(file);
		colors.flip();
		return build(colors);
	}
	private static int loadTexture(File file){
		ByteBuffer colors = BufferUtils.createByteBuffer(TextureSize*TextureSize*3);
		BinaryFile bin = new BinaryFile(file);
		int size = colors.capacity();
		for(int i = 0; i<size; i++)
			colors.put(bin.getByte());
		colors.flip();
		return build(colors);
	}
	private static int loadTexture(WorldNoiseMachine machine, int posX, int posZ){
		File file =
			new File(WraithavensConquest.currentGameFolder+File.separatorChar+"Biomes", posX+","+posZ+".dat");
		if(file.exists()&&file.length()>0)
			return loadTexture(file);
		return generateTexture(machine, file, posX, posZ);
	}
	public static final int TextureSize = 512;
	private final int textureId;
	BiomeMap(WorldNoiseMachine machine, int posX, int posZ){
		textureId = loadTexture(machine, posX, posZ);
	}
	void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
	}
	void dispose(){
		GL11.glDeleteTextures(textureId);
	}
}
