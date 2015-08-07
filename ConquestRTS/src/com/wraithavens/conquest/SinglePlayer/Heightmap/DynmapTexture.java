package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.io.File;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.Utility.BinaryFile;

public class DynmapTexture{
	private static void calculateNormal(float x, float z, Vector3f out, WorldNoiseMachine machine){
		double z0 = machine.getWorldHeight(x-1, z+1)/machine.getMaxHeight();
		double z1 = machine.getWorldHeight(x, z+1)/machine.getMaxHeight();
		double z2 = machine.getWorldHeight(x+1, z+1)/machine.getMaxHeight();
		double z3 = machine.getWorldHeight(x-1, z)/machine.getMaxHeight();
		double z4 = machine.getWorldHeight(x, z)/machine.getMaxHeight();
		double z5 = machine.getWorldHeight(x-1, z-1)/machine.getMaxHeight();
		double z6 = machine.getWorldHeight(x, z-1)/machine.getMaxHeight();
		double z7 = machine.getWorldHeight(x+1, z-1)/machine.getMaxHeight();
		out.set((float)(z2+2.0f*z4+z7-z0-2.0f*z3-z5), (float)(1.0f/machine.getMaxHeight()), (float)(z5+2.0f*z6
			+z7-z0-2.0f*z1-z2));
		out.normalize();
	}
	private static final int TextureDetail = 1024;
	private final int textureId;
	public DynmapTexture(WorldNoiseMachine machine, int x, int z, int size){
		textureId = GL11.glGenTextures();
		build(machine, x, z, size);
	}
	private void build(WorldNoiseMachine machine, int x, int z, int size){
		File file =
			new File(WraithavensConquest.currentGameFolder+File.separatorChar+"Heightmaps", x+","+z+","+size
				+".dat");
		if(file.exists()&&file.length()>0)
			load(file);
		else
			generate(file, machine, x, z, size);
	}
	private void compile(FloatBuffer data){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGBA32F, TextureDetail, TextureDetail, 0, GL11.GL_RGBA,
			GL11.GL_FLOAT, data);
	}
	private void generate(File file, WorldNoiseMachine machine, float posX, float posZ, float size){
		System.out.println("Generating heightmap.");
		BinaryFile bin = new BinaryFile(TextureDetail*TextureDetail*16);
		FloatBuffer data = BufferUtils.createFloatBuffer(TextureDetail*TextureDetail*4);
		int x, z;
		float height;
		float blockX;
		float blockZ;
		float s = size/(TextureDetail-1.0f);
		Vector3f normal = new Vector3f();
		for(z = 0; z<TextureDetail; z++){
			for(x = 0; x<TextureDetail; x++){
				blockX = x*s+posX;
				blockZ = z*s+posZ;
				height = (float)machine.getWorldHeight(blockX, blockZ);
				calculateNormal(blockX, blockZ, normal, machine);
				data.put(normal.x);
				data.put(normal.y);
				data.put(normal.z);
				data.put(height);
				bin.addFloat(normal.x);
				bin.addFloat(normal.y);
				bin.addFloat(normal.z);
				bin.addFloat(height);
			}
			if(z%100==0)
				System.out.println(z+"/"+TextureDetail+" pixel rows complete.");
		}
		bin.compile(file);
		data.flip();
		compile(data);
		System.out.println("Heightmap generated.");
	}
	private void load(File file){
		BinaryFile bin = new BinaryFile(file);
		FloatBuffer data = BufferUtils.createFloatBuffer(TextureDetail*TextureDetail*4);
		int size = data.capacity();
		for(int i = 0; i<size; i++)
			data.put(bin.getFloat());
		data.flip();
		compile(data);
	}
	void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
	}
}
