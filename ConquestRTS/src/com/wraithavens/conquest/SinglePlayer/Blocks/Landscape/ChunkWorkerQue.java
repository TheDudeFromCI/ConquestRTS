package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.util.concurrent.LinkedBlockingQueue;

public class ChunkWorkerQue{
	private LinkedBlockingQueue<ChunkWorkerTask> taskList = new LinkedBlockingQueue();
	void addTask(int x, int y, int z, ChunkHeightData heightData){
		taskList.add(new ChunkWorkerTask(x, y, z, heightData));
	}
	void placeAndWait(int x, int y, int z, ChunkHeightData heightData){
		ChunkWorkerTask task = new ChunkWorkerTask(x, y, z, heightData);
		taskList.add(task);
		while(!task.isFinished())
			try{
				Thread.sleep(1);
			}catch(Exception exception){
				exception.printStackTrace();
			}
	}
	int size(){
		return taskList.size();
	}
	ChunkWorkerTask take(){
		try{
			return taskList.take();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		return null;
	}
}
