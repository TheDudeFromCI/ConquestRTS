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
	private int lastX = Integer.MAX_VALUE;
	private int lastZ = Integer.MAX_VALUE;
	private int skippedChunks = 0;
	private long startTime;
	private long lastMessage;
	private final ChunkLoadingThread[] workers = new ChunkLoadingThread[WorkerThreadCount];
	private final ChunkWorkerQue que;
	private final WorldNoiseMachine machine;
	private MassChunkHeightData massChunkHeightData;
	public SecondaryLoop(Camera camera, WorldNoiseMachine machine){
		this.camera = camera;
		this.machine = machine;
		spiral = new SpiralGridAlgorithm();
		spiral.setMaxDistance(50);
		que = new ChunkWorkerQue();
		for(int i = 0; i<workers.length; i++)
			workers[i] = new ChunkLoadingThread(i, que, machine);
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
		loadMassChunkHeightData(x, z);
		ChunkHeightData heightData = null;
		if(!massChunkHeightData.getHeights(x, z, temp)){
			heightData = new ChunkHeightData(machine, x, z, massChunkHeightData);
			heightData.getChunkHeight(temp);
		}
		int y;
		for(int i = 0; i<temp[1]; i++){
			y = i*LandscapeChunk.LandscapeSize+temp[0];
			File file = Algorithms.getChunkPath(x, y, z);
			if(file.exists()&&file.length()>0){
				skippedChunks++;
				try{
					Thread.sleep(1);
				}catch(Exception exception){
					exception.printStackTrace();
				}
				continue;
			}
			if(heightData==null)
				heightData = new ChunkHeightData(machine, x, z, massChunkHeightData);
			if(skippedChunks>0)
				System.out.println("Skipped "+skippedChunks+" chunks.");
			skippedChunks = 0;
			while(que.size()>=WorkerThreadCount)
				try{
					Thread.sleep(1);
				}catch(Exception exception){
					exception.printStackTrace();
				}
			que.addTask(x, y, z, heightData);
		}
	}
	private void loadMassChunkHeightData(int x, int z){
		if(massChunkHeightData==null){
			massChunkHeightData =
				new MassChunkHeightData(Algorithms.groupLocation(x, 128*64), Algorithms.groupLocation(z, 128*64));
			return;
		}
		int minX = massChunkHeightData.getX();
		int minZ = massChunkHeightData.getZ();
		if(x>=minX&&z>=minZ&&x<minX+128*64&&z<minZ+128*64)
			return;
		massChunkHeightData =
			new MassChunkHeightData(Algorithms.groupLocation(x, 128*64), Algorithms.groupLocation(z, 128*64));
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
