package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.io.File;
import java.nio.FloatBuffer;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.Utility.CompactBinaryFile;

public class HeightmapGenerator{
	private static void loadHeightmap(FloatBuffer vertexData, String file){
		System.out.println("Loading heightmap.");
		CompactBinaryFile f = new CompactBinaryFile(file);
		f.read();
		for(int i = 0; i<vertexData.capacity(); i++)
			vertexData.put(Float.intBitsToFloat((int)f.getNumber(32)));
		f.stopReading();
		System.out.println("Heightmap loaded.");
	}
	private final WorldNoiseMachine machine;
	HeightmapGenerator(WorldNoiseMachine machine){
		this.machine = machine;
	}
	private void calculateNormal(float x, float z, Vector3f out){
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
	private void generateHeightmap(int x, int z, FloatBuffer vertexData, String file){
		System.out.println("Generating heightmap: "+x+", "+z+"");
		int a, b;
		float s = HeightMap.ViewDistance*2/(HeightMap.VertexCount-1.0f);
		float blockX;
		float blockZ;
		Vector3f normal = new Vector3f();
		Vector3f color = new Vector3f();
		for(b = 0; b<HeightMap.VertexCount; b++)
			for(a = 0; a<HeightMap.VertexCount; a++){
				// ---
				// Get the vertex information in real-world.
				// ---
				blockX = a*s-HeightMap.ViewDistance/2+x;
				blockZ = b*s-HeightMap.ViewDistance/2+z;
				calculateNormal(blockX, blockZ, normal);
				machine.getPrairieColor(blockX, blockZ, color);
				// ---
				// Bind the vertex location
				// ---
				vertexData.put(blockX);
				vertexData.put(Math.round(machine.getWorldHeight(blockX, blockZ)));
				vertexData.put(blockZ);
				// ---
				// Bind the vertex color.
				// ---
				vertexData.put(color.x);
				vertexData.put(color.y);
				vertexData.put(color.z);
				// ---
				// Bind the vertex normal.
				// ---
				vertexData.put(normal.x);
				vertexData.put(normal.y);
				vertexData.put(normal.z);
			}
		CompactBinaryFile f = new CompactBinaryFile(file);
		f.ensureExistance();
		f.write();
		f.prepareSpace(vertexData.capacity()*4);
		for(int i = 0; i<vertexData.capacity(); i++)
			f.addNumber(Float.floatToIntBits(vertexData.get(i)), 32);
		f.stopWriting();
		System.out.println("Heightmap generated.");
	}
	void getHeightmap(int x, int z, FloatBuffer vertexData){
		File file = new File(WraithavensConquest.saveFolder+File.separatorChar+"Heightmaps", x+","+z+".dat");
		if(!file.exists())
			generateHeightmap(x, z, vertexData, file.getAbsolutePath());
		else
			loadHeightmap(vertexData, file.getAbsolutePath());
	}
}
