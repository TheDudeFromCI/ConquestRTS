package com.wraithavens.conquest.WorldGeneration;

import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class WorldGeneratorProperties{
	private final boolean duelCoreLoading;
	private final long[] seeds;
	private final int loadRange;
	private final Camera camera;
	public WorldGeneratorProperties(boolean duelCoreLoading, long[] seeds, int loadRange, Camera camera){
		this.duelCoreLoading = duelCoreLoading;
		this.seeds = seeds;
		this.loadRange = loadRange;
		this.camera = camera;
	}
	Camera getCamera(){
		return camera;
	}
	int getLoadRange(){
		return loadRange;
	}
	long[] getSeeds(){
		return seeds;
	}
	boolean hasDuelCoreLoading(){
		return duelCoreLoading;
	}
}
