package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.util.concurrent.LinkedBlockingQueue;

public class ChunkWorkerQue{
	private LinkedBlockingQueue<ChunkWorkerTask> taskList = new LinkedBlockingQueue();
	void addTask(int x, int y, int z){
		taskList.add(new ChunkWorkerTask(x, y, z));
	}
	void placeAndWait(int x, int y, int z){
		ChunkWorkerTask task = new ChunkWorkerTask(x, y, z);
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
