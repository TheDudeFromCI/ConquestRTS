package com.wraithavens.conquest.SinglePlayer.Blocks.World;

import java.util.ArrayList;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.Algorithms;

public class World{
	public static boolean isWithinView(int x, int z){
		x = Algorithms.groupLocation(x, 32)/32;
		z = Algorithms.groupLocation(z, 32)/32;
		Camera camera = SinglePlayerGame.INSTANCE.getCamera();
		double a = Algorithms.groupLocation(camera.getBlockX(), 32)/32;
		double b = Algorithms.groupLocation(camera.getBlockZ(), 32)/32;
		a -= x;
		b -= z;
		double range = WraithavensConquest.Settings.getChunkRenderDistance();
		return a*a+b*b<range*range;
	}
	static int ShadeAttribLocation;
	static int UvAttribLocation;
	private final ArrayList<ChunkStack> chunks = new ArrayList();
	private final BetterChunkLoader chunkLoader;
	private final ShaderProgram shader;
	private final LoadingLoop loadingLoop;
	private int cameraLocationX;
	private int cameraLocationZ;
	private int frameId;
	public World(){
		chunkLoader = new BetterChunkLoader();
		shader = new ShaderProgram("World");
		shader.loadUniforms("biomeColorTexture", "chunkOffset", "blockTexture");
		shader.setUniform1I(0, 0);
		shader.setUniform1I(2, 1);
		ShadeAttribLocation = shader.getAttributeLocation("att_shade");
		UvAttribLocation = shader.getAttributeLocation("att_uv");
		GL20.glEnableVertexAttribArray(ShadeAttribLocation);
		GL20.glEnableVertexAttribArray(UvAttribLocation);
		loadingLoop = new LoadingLoop();
		/**
		 * TODO Alright, here's how block handling will work. Blocks are stored
		 * in seperate files from chunk stacks. Each sub chunk, 32x32x32 has
		 * it's own file. These chunk blocks are loaded when needed and stored
		 * in a list in memory. After 3 frames without being accessed, the chunk
		 * block is unloaded. If any changes are made to a chunk, it is saved
		 * when the chunk block is unloaded. Chunk remeshing should be done on
		 * the loading loop, and passed over as soon as it's done. Meshing is
		 * done in too passed. Basic Meshing is done on main thread, and
		 * advanced meshing is done on the second thread. Only the advanced
		 * meshing is saved.
		 */
	}
	public void dispose(){
		loadingLoop.stop();
		for(ChunkStack chunk : chunks)
			chunk.dispose();
		shader.dispose();
	}
	public void render(){
		shader.bind();
		for(ChunkStack chunk : chunks)
			if(chunk.isVisible())
				chunk.render(shader);
	}
	public void update(){
		// Check all the distances for each chunk. This helps avoid redundency.
		for(ChunkStack chunk : chunks)
			chunk.updateCameraDistance();
		frameId++;
		Camera camera = SinglePlayerGame.INSTANCE.getCamera();
		int x = Algorithms.groupLocation(camera.getBlockX(), 32);
		int z = Algorithms.groupLocation(camera.getBlockZ(), 32);
		if(x!=cameraLocationX||z!=cameraLocationZ){
			chunkLoader.reset();
			cameraLocationX = x;
			cameraLocationZ = z;
		}
		int chunkUpdateRate = WraithavensConquest.Settings.getChunkUpdateFrames();
		if(frameId%chunkUpdateRate==0){
			if(frameId%(chunkUpdateRate*2)==0)
				loadNextChunk();
			else{
				for(int i = 0; i<chunks.size();){
					if(chunks.get(i).shouldUnload()){
						chunks.get(i).dispose();
						chunks.remove(i);
					}else
						i++;
				}
			}
		}
	}
	private void loadNextChunk(){
		if(chunkLoader.hasNext()){
			chunkLoader.next();
			int x = chunkLoader.getX()*32+cameraLocationX;
			int z = chunkLoader.getY()*32+cameraLocationZ;
			for(ChunkStack chunk : chunks)
				if(chunk.getX()==x&&chunk.getZ()==z)
					return;
			chunks.add(new ChunkStack(x, z));
		}
	}
}
