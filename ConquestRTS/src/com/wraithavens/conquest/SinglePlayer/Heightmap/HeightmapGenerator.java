package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.io.File;
import java.nio.FloatBuffer;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.Utility.BinaryFile;

class HeightmapGenerator{
	private static void loadHeightmap(FloatBuffer vertexData, File file){
		System.out.println("Loading heightmap.");
		BinaryFile bin = new BinaryFile(file);
		int floats = HeightMap.Vertices*9;
		for(int i = 0; i<floats; i++)
			vertexData.put(bin.getFloat());
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
	private void generateHeightmap(int x, int z, FloatBuffer vertexData, File file){
		System.out.println("Generating heightmap: "+x+", "+z+"");
		int a, b;
		float s = HeightMap.ViewDistance*2/(HeightMap.VertexCount-1.0f);
		float blockX;
		float blockZ;
		float height;
		Vector3f normal = new Vector3f();
		Vector3f color = new Vector3f();
		BinaryFile bin = new BinaryFile(HeightMap.Vertices*36);
		for(b = 0; b<HeightMap.VertexCount; b++)
			for(a = 0; a<HeightMap.VertexCount; a++){
				// ---
				// Get the vertex information in real-world.
				// ---
				blockX = Math.round(a*s-HeightMap.ViewDistance/2+x);
				blockZ = Math.round(b*s-HeightMap.ViewDistance/2+z);
				calculateNormal(blockX, blockZ, normal);
				machine.getPrairieColor(blockX, blockZ, color);
				// ---
				// Bind the vertex location
				// ---
				vertexData.put(blockX);
				vertexData.put(height = (float)machine.getWorldHeight(blockX, blockZ));
				vertexData.put(blockZ);
				bin.addFloat(blockX);
				bin.addFloat(height);
				bin.addFloat(blockZ);
				// ---
				// Bind the vertex color.
				// ---
				vertexData.put(color.x);
				vertexData.put(color.y);
				vertexData.put(color.z);
				bin.addFloat(color.x);
				bin.addFloat(color.y);
				bin.addFloat(color.z);
				// ---
				// Bind the vertex normal.
				// ---
				vertexData.put(normal.x);
				vertexData.put(normal.y);
				vertexData.put(normal.z);
				bin.addFloat(normal.x);
				bin.addFloat(normal.y);
				bin.addFloat(normal.z);
			}
		bin.compile(file);
		System.out.println("Heightmap generated.");
	}
	void getHeightmap(int x, int z, FloatBuffer vertexData){
		File file =
			new File(WraithavensConquest.currentGameFolder+File.separatorChar+"Heightmaps", x+","+z+".dat");
		// ---
		// Check to see if the file exists, and contains data. If yes, load it.
		// Otherwise generate a new one, and save it.
		// ---
		if(!file.exists()||file.length()==0)
			generateHeightmap(x, z, vertexData, file);
		else
			loadHeightmap(vertexData, file);
	}
}
