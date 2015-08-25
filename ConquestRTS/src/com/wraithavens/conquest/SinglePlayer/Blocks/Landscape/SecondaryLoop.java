package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.text.NumberFormat;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.Utility.Algorithms;

public class SecondaryLoop implements Runnable{
	private static final int WorkerThreadCount = 1;
	static long chunksLoaded = 0;
	private volatile boolean running = true;
	private final SpiralGridAlgorithm spiral;
	private volatile Camera camera;
	private final int[] temp = new int[2];
	private volatile ChunkHeightData chunkHeights;
	private int lastX = Integer.MAX_VALUE;
	private int lastZ = Integer.MAX_VALUE;
	private int skippedChunks = 0;
	private long startTime;
	private long lastMessage;
	private final ChunkLoadingThread[] workers = new ChunkLoadingThread[WorkerThreadCount];
	private final ChunkWorkerQue que;
	public SecondaryLoop(Camera camera, ChunkHeightData chunkHeights, WorldNoiseMachine machine){
		this.camera = camera;
		this.chunkHeights = chunkHeights;
		spiral = new SpiralGridAlgorithm();
		spiral.setMaxDistance(20);
		que = new ChunkWorkerQue();
		for(int i = 0; i<workers.length; i++)
			workers[i] = new ChunkLoadingThread(i, que, machine.createInstance());
		Thread t = new Thread(this);
		t.setName("Secondary Loading Thread");
		t.setDaemon(true);
		t.start();
	}
	public void dispose(){
		running = false;
		for(ChunkLoadingThread worker : workers)
			worker.dispose();
	}
	public void run(){
		startTime = System.currentTimeMillis();
		chunksLoaded = 0;
		lastMessage = startTime;
		while(running){
			try{
				loadNext();
			}catch(Exception exception){
				exception.printStackTrace();
			}
		}
	}
	private void attemptGenerateChunk(){
		int x = spiral.getX()*LandscapeChunk.LandscapeSize;
		int z = spiral.getY()*LandscapeChunk.LandscapeSize;
		chunkHeights.getChunkHeight(x, z, temp);
		int y;
		for(int i = 0; i<temp[1]; i++){
			y = i*LandscapeChunk.LandscapeSize+temp[0];
			File file = Algorithms.getChunkPath(x, y, z);
			if(file.exists()&&file.length()>0){
				skippedChunks++;
				continue;
			}
			if(skippedChunks>0)
				System.out.println("Skipped "+skippedChunks+" chunks.");
			skippedChunks = 0;
			while(que.size()>=WorkerThreadCount)
				try{
					Thread.sleep(1);
				}catch(Exception exception){
					exception.printStackTrace();
				}
			que.addTask(x, y, z);
			chunksLoaded++;
		}
	}
	private void loadNext(){
		updateCameraLocation();
		if(spiral.hasNext()){
			spiral.next();
			attemptGenerateChunk();
		}else
			try{
				Thread.sleep(50);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		printMessage();
	}
	private void printMessage(){
		long time = System.currentTimeMillis();
		if(time-lastMessage>=5000){
			lastMessage = time;
			System.out.println("Loading ~"
				+NumberFormat.getInstance().format((float)(chunksLoaded/((time-startTime)/1000.0/60.0)))
				+" chunks per minute. ["+chunksLoaded+" chunks loaded]");
		}
	}
	private void updateCameraLocation(){
		int x =
			Algorithms.groupLocation((int)camera.x, LandscapeChunk.LandscapeSize)/LandscapeChunk.LandscapeSize;
		int z =
			Algorithms.groupLocation((int)camera.z, LandscapeChunk.LandscapeSize)/LandscapeChunk.LandscapeSize;
		if(x!=lastX||z!=lastZ){
			lastX = x;
			lastZ = z;
			spiral.setOrigin(lastX, lastZ);
			spiral.reset();
		}
	}
	ChunkWorkerQue getQue(){
		return que;
	}
}
