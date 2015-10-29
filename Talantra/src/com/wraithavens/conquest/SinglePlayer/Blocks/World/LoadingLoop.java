package com.wraithavens.conquest.SinglePlayer.Blocks.World;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.Settings;

public class LoadingLoop{
	private volatile boolean running = true;
	private LinkedBlockingQueue<LoadingLoopTask> que = new LinkedBlockingQueue<>();
	@SuppressWarnings("unused")
	private byte[] biomeColors = new byte[32*32*3];
	private int[][] heights = new int[34][34];
	public LoadingLoop(){
		Thread t = new Thread(new Runnable(){
			public void run(){
				update();
			}
		});
		t.setDaemon(true);
		t.setName("Loading Loop");
		t.start();
	}
	public void genChunk(int x, int z){
		File file = Algorithms.getChunkStackPath(x, z);
		if(!(file.exists()&&file.length()>0)){
			{
				// ---
				// Add mesh data.
				// ---
				int a, b;
				int tempA;
				WorldNoiseMachine machine = SinglePlayerGame.INSTANCE.getWorldNoiseMachine();
				int minHeight = Integer.MAX_VALUE;
				for(a = 0; a<34; a++){
					tempA = a-1+x;
					for(b = 0; b<34; b++){
						heights[a][b] = machine.getGroundLevel(tempA, b-1+z);
						minHeight = Math.min(heights[a][b], minHeight);
					}
				}
				/*
				 * TODO Fill up chunk blocks with main data. Get nearby chunks
				 * and fill up their clip data. Then remesh them.
				 */
				// MeshRenderer render = blockData.mesh(false);
				// painter.getPackets().add(new
				// MeshDataPacket(render));
				// blockData.saveToFile(x, y, z);
				// blockData.clear();
			}
			// TODO Load biome colors.
			// TODO Load water.
			// TODO Load entities.
			// TODO Load grass.
			// TODO Load meshes.
			// ChunkStackBuilderData builder =
			// new ChunkStackBuilderData(biomeColors, null, null,
			// null, null);
			// builder.write(x, z);
		}
	}
	private void update(){
		Settings settings = WraithavensConquest.Settings;
		BetterChunkLoader loader = new BetterChunkLoader();
		Camera camera = SinglePlayerGame.INSTANCE.getCamera();
		int lastCameraX = Algorithms.groupLocation(camera.getBlockX(), 32);
		int lastCameraZ = Algorithms.groupLocation(camera.getBlockZ(), 32);
		int x, z;
		while(running){
			try{
				Thread.sleep(settings.getGeneratorSleeping());
			}catch(Exception exception){}
			if(que.isEmpty()){
				x = Algorithms.groupLocation(camera.getBlockX(), 32);
				z = Algorithms.groupLocation(camera.getBlockZ(), 32);
				if(x!=lastCameraX||z!=lastCameraZ){
					lastCameraX = x;
					lastCameraZ = z;
					loader.reset();
				}
				if(loader.hasNext()){
					loader.next();
					x = loader.getX()*32+lastCameraX;
					z = loader.getY()*32+lastCameraZ;
					genChunk(x, z);
				}
			}else
				try{
					que.take().run(this);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
		}
	}
	void addTask(LoadingLoopTask task){
		que.add(task);
	}
	void stop(){
		running = false;
	}
}
