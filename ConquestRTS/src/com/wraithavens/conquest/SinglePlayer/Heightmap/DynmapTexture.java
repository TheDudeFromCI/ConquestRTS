package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.Noise.Biome;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;
import com.wraithavens.conquest.Utility.LoadingScreenTask;

public class DynmapTexture{
	private static void calculateNormal(float x, float z, Vector3f out, WorldNoiseMachine machine){
		double z0 = machine.getLevelRaw(x-1, z+1);
		double z1 = machine.getLevelRaw(x, z+1);
		double z2 = machine.getLevelRaw(x+1, z+1);
		double z3 = machine.getLevelRaw(x-1, z);
		double z4 = machine.getLevelRaw(x+1, z);
		double z5 = machine.getLevelRaw(x-1, z-1);
		double z6 = machine.getLevelRaw(x, z-1);
		double z7 = machine.getLevelRaw(x+1, z-1);
		out.set((float)(z2+2.0f*z4+z7-z0-2.0f*z3-z5), 1.0f/machine.getGroundLevel(x, z), (float)(z5+2.0f*z6+z7
			-z0-2.0f*z1-z2));
		out.normalize();
	}
	private static final int TextureDetail = Dynmap.VertexCount-1;
	private static final int TextureDetail2 = 2048;
	private final int textureId;
	private final int colorTextureId;
	private final SinglePlayerGame singlePlayerGrame;
	public DynmapTexture(WorldNoiseMachine machine, int x, int z, SinglePlayerGame singlePlayerGame){
		singlePlayerGrame = singlePlayerGame;
		textureId = GL11.glGenTextures();
		colorTextureId = GL11.glGenTextures();
		build(machine, x, z);
	}
	private void build(WorldNoiseMachine machine, int x, int z){
		File file = Algorithms.getDynmapFile(x-(Dynmap.BlocksPerChunk-8192)/2, z-(Dynmap.BlocksPerChunk-8192)/2);
		if(file.exists()&&file.length()>0)
			load(file);
		else
			generate(file, machine, x, z);
	}
	private void compile(FloatBuffer data, ByteBuffer data2){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGBA32F, TextureDetail, TextureDetail, 0, GL11.GL_RGBA,
			GL11.GL_FLOAT, data);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureId);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, TextureDetail2, TextureDetail2, 0, GL11.GL_RGB,
			GL11.GL_UNSIGNED_BYTE, data2);
	}
	private void generate(File file, WorldNoiseMachine machine, float posX, float posZ){
		System.out.println("Generating heightmap.");
		singlePlayerGrame.getLoadingScreen().setTask(new LoadingScreenTask(){
			private int stage = 0;
			private BinaryFile bin;
			private FloatBuffer data;
			private ByteBuffer data2;
			private float s;
			private int x;
			private int z;
			private float height;
			private float blockX;
			private float blockZ;
			private Vector3f normal = new Vector3f();
			private byte red, green, blue;
			private float[] tempHeight = new float[3];
			private Biome biome;
			public float getCompletionPercent(){
				if(stage==0)
					return 0;
				if(stage==1)
					return z*2/(2048+2048f);
				if(stage==2)
					return (z+2048)/(2048+2048f);
				return 1;
			}
			public boolean runStep(){
				switch(stage){
					case 0:
						start();
						break;
					case 1:
						firstPass();
						break;
					case 2:
						secondPass();
						break;
					case 3:
						end();
						return true;
				}
				return false;
			}
			private void end(){
				bin.compress(new byte[28*1024*1024], false); // 28 Mb
				bin.compile(file);
				data.flip();
				data2.flip();
				compile(data, data2);
				System.out.println("Heightmap generated.");
			}
			private void firstPass(){
				if(z%10==0)
					System.out.println(z+"/"+TextureDetail+" pixel rows complete. (Pass 1 of 2)");
				for(x = 0; x<TextureDetail; x++){
					blockX = x*s+posX;
					blockZ = z*s+posZ;
					height = machine.getGroundLevel(blockX, blockZ);
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
				z++;
				if(z==TextureDetail){
					stage = 2;
					s = Dynmap.BlocksPerChunk/(TextureDetail2-1.0f);
					z = 0;
				}
			}
			private void secondPass(){
				for(int i = 0; i<2; i++){
					if(z%25==0)
						System.out.println(z+"/"+TextureDetail2+" pixel rows complete. (Pass 2 of 2)");
					for(x = 0; x<TextureDetail2; x++){
						blockX = x*s+posX;
						blockZ = z*s+posZ;
						biome =
							machine.getBiomeAt(blockX<0?(int)blockX-1:(int)blockX, blockZ<0?(int)blockZ-1
								:(int)blockZ, tempHeight);
						height =
							machine.scaleHeight(tempHeight[0], tempHeight[1], tempHeight[2], blockX, blockZ);
						WorldNoiseMachine.getBiomeColorAt(biome, tempHeight[0], tempHeight[1], normal);
						data2.put(red = (byte)Math.round(normal.x*255));
						data2.put(green = (byte)Math.round(normal.y*255));
						data2.put(blue = (byte)Math.round(normal.z*255));
						bin.addByte(red);
						bin.addByte(green);
						bin.addByte(blue);
					}
					z++;
					if(z==TextureDetail2){
						stage = 3;
						return;
					}
				}
			}
			private void start(){
				bin = new BinaryFile(TextureDetail*TextureDetail*16+TextureDetail2*TextureDetail2*3);
				data = BufferUtils.createFloatBuffer(TextureDetail*TextureDetail*4);
				data2 = BufferUtils.createByteBuffer(TextureDetail2*TextureDetail2*3);
				s = Dynmap.BlocksPerChunk/(TextureDetail-1.0f);
				stage = 1;
				z = 0;
			}
		});
	}
	private void load(File file){
		BinaryFile bin = new BinaryFile(file);
		bin.decompress(new byte[28*1024*1024], false); // 28 Mb
		FloatBuffer data = BufferUtils.createFloatBuffer(TextureDetail*TextureDetail*4);
		int size = data.capacity();
		for(int i = 0; i<size; i++)
			data.put(bin.getFloat());
		data.flip();
		ByteBuffer data2 = BufferUtils.createByteBuffer(TextureDetail2*TextureDetail2*3);
		size = data2.capacity();
		for(int i = 0; i<size; i++)
			data2.put(bin.getByte());
		data2.flip();
		compile(data, data2);
	}
	void bind(){
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureId);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
	}
	void dispose(){
		GL11.glDeleteTextures(textureId);
		GL11.glDeleteTextures(colorTextureId);
	}
	void reload(WorldNoiseMachine machine, int x, int z){
		build(machine, x, z);
	}
}
