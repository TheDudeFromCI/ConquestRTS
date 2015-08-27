package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.util.ArrayList;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityDatabase;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.Grasslands;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.Algorithms;

public class LandscapeWorld{
	static int ShadeAttribLocation;
	private static final int ViewDistance = 2;
	private final ArrayList<LandscapeChunk> chunks = new ArrayList();
	private final ShaderProgram shader;
	private final SpiralGridAlgorithm spiral;
	private final Camera camera;
	private final EntityDatabase entityDatabase;
	private final SecondaryLoop loadingLoop;
	private final WorldNoiseMachine machine;
	private int chunkX;
	private int chunkZ;
	private int frame = 0;
	private Grasslands grassLands;
	private MassChunkHeightData massChunkHeightData;
	public LandscapeWorld(WorldNoiseMachine machine, EntityDatabase entityDatabase, Camera camera){
		GlError.out("Building landscape.");
		this.camera = camera;
		this.entityDatabase = entityDatabase;
		this.machine = machine;
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "Landscape.vert"), null, new File(
				WraithavensConquest.assetFolder, "Landscape.frag"));
		shader.bind();
		shader.loadUniforms("texture", "offset");
		shader.setUniform1I(0, 0);
		ShadeAttribLocation = shader.getAttributeLocation("shade");
		GL20.glEnableVertexAttribArray(ShadeAttribLocation);
		GlError.dumpError();
		spiral = new SpiralGridAlgorithm();
		spiral.setMaxDistance(ViewDistance);
		loadingLoop = new SecondaryLoop(camera, machine);
	}
	public void dispose(){
		loadingLoop.dispose();
		shader.dispose();
		GlError.out("Disposing landscape.");
		for(LandscapeChunk c : chunks)
			c.dispose();
		chunks.clear();
		GlError.dumpError();
	}
	public LandscapeChunk getContainingChunk(int x, int y, int z, boolean load, ChunkHeightData heightData){
		x = Algorithms.groupLocation(x, LandscapeChunk.LandscapeSize);
		y = Algorithms.groupLocation(y, LandscapeChunk.LandscapeSize);
		z = Algorithms.groupLocation(z, LandscapeChunk.LandscapeSize);
		for(LandscapeChunk c : chunks)
			if(c.getX()==x&&c.getY()==y&&c.getZ()==z)
				return c;
		if(!load)
			return null;
		// ---
		// If it's current generating the chunk we want, wait until it's done.
		// ---
		LandscapeChunk c =
			new LandscapeChunk(entityDatabase, grassLands, x, y, z, loadingLoop.getQue(), heightData);
		chunks.add(c);
		if(grassLands!=null)
			grassLands.updateVisibility();
		return c;
	}
	public boolean isWithinView(int x, int z){
		int cx = Algorithms.groupLocation((int)camera.x, LandscapeChunk.LandscapeSize);
		int cz = Algorithms.groupLocation((int)camera.z, LandscapeChunk.LandscapeSize);
		x = Algorithms.groupLocation(x, LandscapeChunk.LandscapeSize);
		z = Algorithms.groupLocation(z, LandscapeChunk.LandscapeSize);
		return Math.abs(x-cx)<=ViewDistance*LandscapeChunk.LandscapeSize
			&&Math.abs(z-cz)<=ViewDistance*LandscapeChunk.LandscapeSize;
	}
	public void loadAllChunks(){
		while(spiral.hasNext()){
			spiral.next();
			loadChunks(spiral.getX()*LandscapeChunk.LandscapeSize+chunkX, spiral.getY()
				*LandscapeChunk.LandscapeSize+chunkZ);
		}
		System.out.println("All chunks loaded.");
	}
	public void render(){
		shader.bind();
		for(LandscapeChunk c : chunks)
			if(isWithinView(c, ViewDistance)
				&&camera.getFrustum().cubeInFrustum(c.getX(), c.getY(), c.getZ(), LandscapeChunk.LandscapeSize)){
				shader.setUniform3f(1, c.getX(), c.getY(), c.getZ());
				c.render();
			}
		GlError.dumpError();
	}
	public void setup(Grasslands grassLands){
		this.grassLands = grassLands;
	}
	public void update(){
		// ---
		// First, make sure we are loading from the camera's location.
		// ---
		int x = Algorithms.groupLocation((int)camera.x, LandscapeChunk.LandscapeSize);
		int z = Algorithms.groupLocation((int)camera.z, LandscapeChunk.LandscapeSize);
		if(x!=chunkX||z!=chunkZ){
			spiral.reset();
			chunkX = x;
			chunkZ = z;
		}
		// ---
		// Next, load a chunk. Because of their size, I don't want to load more
		// then 1 chunk, every ten frames. I also want to unload chunks, but
		// every tens frame. I do this by flipping off tasks, and doing one or
		// the other every 5 frames. If the spiral distance is less or equal to
		// 1 chunk away, kick into high gear to prevent the player from reaching
		// the edge.
		// ---
		frame++;
		if(frame%5==0){
			if(frame%10==0||spiral.getDistance()<=0){
				if(!spiral.hasNext())
					return;
				spiral.next();
				loadChunks(spiral.getX()*LandscapeChunk.LandscapeSize+chunkX, spiral.getY()
					*LandscapeChunk.LandscapeSize+chunkZ);
				if(!spiral.hasNext())
					System.out.println("All chunks loaded.");
			}else
				clearDistanceChunks();
		}
	}
	private void clearDistanceChunks(){
		for(int i = 0; i<chunks.size();){
			if(shouldRemove(chunks.get(i))){
				chunks.get(i).dispose();
				chunks.remove(i);
			}else
				i++;
		}
	}
	private boolean isWithinView(LandscapeChunk c, int distance){
		int x = Algorithms.groupLocation((int)camera.x, LandscapeChunk.LandscapeSize);
		int z = Algorithms.groupLocation((int)camera.z, LandscapeChunk.LandscapeSize);
		return Math.abs(x-c.getX())<=distance*LandscapeChunk.LandscapeSize
			&&Math.abs(z-c.getZ())<=distance*LandscapeChunk.LandscapeSize;
	}
	private void loadChunks(int x, int z){
		loadMassChunkHeightData(x, z);
		ChunkHeightData chunkHeightData = new ChunkHeightData(machine, x, z, massChunkHeightData);
		int[] h = new int[2];
		chunkHeightData.getChunkHeight(h);
		for(int i = 0; i<h[1]; i++)
			getContainingChunk(x, i*LandscapeChunk.LandscapeSize+h[0], z, true, chunkHeightData);
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
	private boolean shouldRemove(LandscapeChunk chunk){
		return !isWithinView(chunk, ViewDistance+3);
	}
}
