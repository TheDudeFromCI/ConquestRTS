package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.util.Arrays;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

public class ChunkHeightData{
	private int[] heights = new int[0];
	private final WorldNoiseMachine machine;
	public ChunkHeightData(WorldNoiseMachine machine){
		this.machine = machine;
		loadList();
	}
	public void getChunkHeight(int x, int z, int[] out){
		for(int i = 0; i<heights.length; i += 4)
			if(heights[i]==x&&heights[i+1]==z){
				out[0] = heights[i+2];
				out[1] = heights[i+3];
				return;
			}
		loadChunkHeight(out, x, z);
		int o = heights.length;
		heights = Arrays.copyOf(heights, o+4);
		heights[o] = x;
		heights[o+1] = z;
		heights[o+2] = out[0];
		heights[o+3] = out[1];
		saveList();
	}
	private void loadChunkHeight(int[] out, int x, int z){
		int a, b, h;
		int minHeight = Integer.MAX_VALUE;
		int maxHeight = 0;
		for(a = 0; a<LandscapeChunk.LandscapeSize; a++)
			for(b = 0; b<LandscapeChunk.LandscapeSize; b++){
				h = (int)machine.getWorldHeight(x+a, z+b);
				if(h<minHeight)
					minHeight = h;
				if(h>maxHeight)
					maxHeight = h;
			}
		minHeight = Algorithms.groupLocation(minHeight, LandscapeChunk.LandscapeSize);
		maxHeight = Algorithms.groupLocation(maxHeight, LandscapeChunk.LandscapeSize);
		out[0] = minHeight;
		out[1] = (maxHeight-minHeight)/LandscapeChunk.LandscapeSize+1;
	}
	private void loadList(){
		File file = new File(WraithavensConquest.currentGameFolder, "ChunkHeights.dat");
		if(file.exists()&&file.length()>0){
			BinaryFile bin = new BinaryFile(file);
			heights = new int[bin.length()/4];
			for(int i = 0; i<heights.length; i++)
				heights[i] = bin.getInt();
		}
	}
	private void saveList(){
		File file = new File(WraithavensConquest.currentGameFolder, "ChunkHeights.dat");
		BinaryFile bin = new BinaryFile(heights.length*4);
		for(int i : heights)
			bin.addInt(i);
		bin.compile(file);
	}
}
