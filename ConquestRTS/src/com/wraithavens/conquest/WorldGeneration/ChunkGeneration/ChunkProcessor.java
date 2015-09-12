package com.wraithavens.conquest.WorldGeneration.ChunkGeneration;

import java.io.File;
import java.util.ArrayList;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.SpiralGridAlgorithm;
import com.wraithavens.conquest.Utility.BinaryFile;
import com.wraithavens.conquest.WorldGeneration.World;
import com.wraithavens.conquest.WorldGeneration.BiomeHandle.BiomeType;

public class ChunkProcessor{
	private static final int LoadingLayers = 2;
	private static final float[] tempHT = new float[2];
	private final World world;
	private final SpiralGridAlgorithm[] algorithms;
	private final ArrayList<ChunkGenerationData> generationData = new ArrayList();
	private int stage;
	private int originX;
	private int originZ;
	public ChunkProcessor(World world){
		this.world = world;
		algorithms = new SpiralGridAlgorithm[LoadingLayers];
		for(int i = 0; i<algorithms.length; i++)
			algorithms[i] = new SpiralGridAlgorithm();
	}
	public int getOriginX(){
		return originX;
	}
	public int getOriginZ(){
		return originZ;
	}
	public boolean isWorking(){
		return stage<LoadingLayers;
	}
	public void setLoadRange(int loadRange, int originX, int originZ){
		this.originX = originX;
		this.originZ = originZ;
		stage = 0;
		for(int i = 0; i<algorithms.length; i++){
			algorithms[i].reset();
			algorithms[i].setMaxDistance(loadRange);
		}
	}
	public void update(){
		if(stage==LoadingLayers)
			return;
		updateChunk(algorithms[stage].getX()*64+originX, algorithms[stage].getY()*64+originZ, stage);
		if(algorithms[stage].hasNext())
			algorithms[stage].next();
		else
			stage++;
	}
	private void updateChunk(int x, int z, int stage){
		// ---
		// Make sure we are loading the right chunk layer, and that we actually
		// can load it. (And cleaning up any old data along the way. :P)
		// ---
		ChunkGenerationData chunkData = null;
		for(ChunkGenerationData data : generationData)
			if(data.getX()==x&&data.getZ()==z){
				if(data.getLayer()!=stage)
					return;
				data.updateLayer();
				chunkData = data;
				break;
			}
		if(stage>0&&chunkData==null)
			return;
		if(chunkData==null){
			File file = world.getWorldFiles().getChunkStack(x, z);
			if(file.exists()&&file.length()>0)
				return;
			generationData.add(chunkData = new ChunkGenerationData(x, z));
		}
		if(stage==LoadingLayers-1)
			generationData.remove(chunkData);
		// ---
		// Now generate each chunk based on it's stage.
		// ---
		if(stage==0){
			File file = world.getWorldFiles().getTempChunkLayer(x, z, stage);
			BinaryFile bin = new BinaryFile(64*64+64*64*2*4);
			int a, b;
			int tempB;
			for(b = 0; b<64; b++){
				tempB = b+z;
				for(a = 0; a<64; a++){
					world.getBiomePainter().getHT(x+a, tempB, tempHT);
					bin.addFloat(tempHT[0]);
					bin.addFloat(tempHT[1]);
					bin.addByte((byte)BiomeType.getFittingType(tempHT[0], tempHT[1]).ordinal());
				}
			}
			bin.compress(true);
			bin.compile(file);
		}else if(stage==1){
			// TODO
		}
	}
}
