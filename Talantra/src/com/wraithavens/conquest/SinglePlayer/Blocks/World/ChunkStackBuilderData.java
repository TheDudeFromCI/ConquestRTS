package com.wraithavens.conquest.SinglePlayer.Blocks.World;

import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

public class ChunkStackBuilderData{
	private final byte[] biomeColors;
	private final ChunkStackBuilderMeshData[] meshes;
	private final ChunkStackBuilderMeshData[] water;
	private final ChunkStackBuilderEntityData[] entityData;
	private final ChunkStackBuilderEntityData[] grassData;
	public ChunkStackBuilderData(
		byte[] biomeColors, ChunkStackBuilderMeshData[] meshes, ChunkStackBuilderMeshData[] water,
		ChunkStackBuilderEntityData[] entityData, ChunkStackBuilderEntityData[] grassData){
		this.biomeColors = biomeColors;
		this.meshes = meshes;
		this.water = water;
		this.entityData = entityData;
		this.grassData = grassData;
	}
	private int countByteSize(){
		// Biome colors
		int i = biomeColors.length;
		// Water
		i += 4;
		for(ChunkStackBuilderMeshData d : water)
			i += 12+d.getVertexData().length*4+d.getIndexData().length*2;
		// Entities
		i += (2+5*4)*entityData.length+4;
		// Grass
		i += 4;
		for(ChunkStackBuilderEntityData d : grassData)
			i += d.getData().length*4+6;
		// Meshes
		i += 4;
		for(ChunkStackBuilderMeshData d : meshes)
			i += 12+d.getVertexData().length*4+d.getIndexData().length*2;
		return i;
	}
	void write(int x, int z){
		BinaryFile bin = new BinaryFile(countByteSize());
		// Sizes
		bin.addInt(water.length);
		bin.addInt(entityData.length);
		bin.addInt(grassData.length);
		bin.addInt(meshes.length);
		// Biome Colors
		bin.addBytes(biomeColors, 0, biomeColors.length);
		// Water
		for(ChunkStackBuilderMeshData d : water){
			bin.addInt(d.getVertexData().length);
			for(float f : d.getVertexData())
				bin.addFloat(f);
			bin.addInt(d.getIndexData().length);
			for(short s : d.getIndexData())
				bin.addShort(s);
			bin.addInt(d.getY());
		}
		// Entities
		for(ChunkStackBuilderEntityData d : entityData){
			bin.addShort((short)d.getType());
			for(float f : d.getData())
				bin.addFloat(f);
		}
		// Grass
		for(ChunkStackBuilderEntityData d : grassData){
			bin.addShort((short)d.getType());
			bin.addInt(d.getData().length/8);
			for(float f : d.getData())
				bin.addFloat(f);
		}
		// Meshes
		for(ChunkStackBuilderMeshData d : meshes){
			bin.addInt(d.getVertexData().length);
			for(float f : d.getVertexData())
				bin.addFloat(f);
			bin.addInt(d.getIndexData().length);
			for(short s : d.getIndexData())
				bin.addShort(s);
			bin.addInt(d.getY());
		}
		bin.compress(false);
		bin.compile(Algorithms.getChunkStackPath(x, z));
	}
}
