package com.wraithavens.conquest.SinglePlayer.Blocks.World.LoadingLoopTasks;

import com.wraithavens.conquest.SinglePlayer.Blocks.World.LoadingLoop;
import com.wraithavens.conquest.SinglePlayer.Blocks.World.LoadingLoopTask;

public class LoadChunkTask implements LoadingLoopTask{
	private final int x;
	private final int z;
	public LoadChunkTask(int x, int z){
		this.x = x;
		this.z = z;
	}
	public void run(LoadingLoop loop){
		loop.genChunk(x, z);
	}
}
