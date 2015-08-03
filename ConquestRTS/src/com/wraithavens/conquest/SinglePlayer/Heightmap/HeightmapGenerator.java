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
		for(int i = 0; i<HeightMap.Vertices; i++){
			// ---
			// Load height.
			// ---
			vertexData.put(Float.intBitsToFloat((int)f.getNumber(32)));
			vertexData.put(Float.intBitsToFloat((int)f.getNumber(32)));
			vertexData.put(Float.intBitsToFloat((int)f.getNumber(32)));
			// ---
			// Load color.
			// ---
			vertexData.put(Float.intBitsToFloat((int)f.getNumber(32)));
			vertexData.put(Float.intBitsToFloat((int)f.getNumber(32)));
			vertexData.put(Float.intBitsToFloat((int)f.getNumber(32)));
			// ---
			// Load normal.
			// ---
			vertexData.put(Float.intBitsToFloat((int)f.getNumber(32)));
			vertexData.put(Float.intBitsToFloat((int)f.getNumber(32)));
			vertexData.put(Float.intBitsToFloat((int)f.getNumber(32)));
		}
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
		float height;
		Vector3f normal = new Vector3f();
		Vector3f color = new Vector3f();
		CompactBinaryFile f = new CompactBinaryFile(file);
		f.ensureExistance();
		f.write();
		f.prepareSpace(HeightMap.Vertices*36);
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
				f.addNumber(Float.floatToIntBits(blockX), 32);
				f.addNumber(Float.floatToIntBits(height), 32);
				f.addNumber(Float.floatToIntBits(blockZ), 32);
				// ---
				// Bind the vertex color.
				// ---
				vertexData.put(color.x);
				vertexData.put(color.y);
				vertexData.put(color.z);
				f.addNumber(Float.floatToIntBits(color.x), 32);
				f.addNumber(Float.floatToIntBits(color.y), 32);
				f.addNumber(Float.floatToIntBits(color.z), 32);
				// ---
				// Bind the vertex normal.
				// ---
				vertexData.put(normal.x);
				vertexData.put(normal.y);
				vertexData.put(normal.z);
				f.addNumber(Float.floatToIntBits(normal.x), 32);
				f.addNumber(Float.floatToIntBits(normal.y), 32);
				f.addNumber(Float.floatToIntBits(normal.z), 32);
			}
		f.stopWriting();
		System.out.println("Heightmap generated.");
	}
	void getHeightmap(int x, int z, FloatBuffer vertexData){
		File file = new File(WraithavensConquest.saveFolder+File.separatorChar+"Heightmaps", x+","+z+".dat");
		// ---
		// Check to see if the file exists, and contains data. If yes, load it.
		// Otherwise generate a new one, and save it.
		// ---
		if(!file.exists()||file.length()==0)
			generateHeightmap(x, z, vertexData, file.getAbsolutePath());
		else
			loadHeightmap(vertexData, file.getAbsolutePath());
	}
}
