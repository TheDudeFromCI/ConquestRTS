package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.util.ArrayList;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityDatabase;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.Grasslands;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.Particles.BiomeParticleEngine;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleBatch;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.Algorithms;

public class LandscapeWorld{
	static int ShadeAttribLocation;
	private static final int ViewDistance = 8;
	private static final int ChunkPingRate = 2;
	private final ArrayList<LandscapeChunk> chunks = new ArrayList();
	private final ShaderProgram shader;
	private final SpiralGridAlgorithm spiral;
	private final Camera camera;
	private final EntityDatabase entityDatabase;
	private final SecondaryLoop loadingLoop;
	private final WorldNoiseMachine machine;
	private final ArrayList<BiomeParticleEngine> biomeParticleEngines = new ArrayList();
	private final ParticleBatch particleBatch;
	private int chunkX;
	private int chunkZ;
	private int frame = 0;
	private Grasslands grassLands;
	private MassChunkHeightData massChunkHeightData;
	private ChunkWorkerTask currentLoadingChunk;
	private final int[] chunkLoadHeight = new int[5];
	private ChunkHeightData chunkHeightDataTemp;
	public LandscapeWorld(
		WorldNoiseMachine machine, EntityDatabase entityDatabase, Camera camera, ParticleBatch particleBatch){
		this.camera = camera;
		this.entityDatabase = entityDatabase;
		this.machine = machine;
		this.particleBatch = particleBatch;
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "Landscape.vert"), null, new File(
				WraithavensConquest.assetFolder, "Landscape.frag"));
		shader.bind();
		shader.loadUniforms("colorMap", "offset");
		shader.setUniform1I(0, 0);
		ShadeAttribLocation = shader.getAttributeLocation("shade");
		GL20.glEnableVertexAttribArray(ShadeAttribLocation);
		spiral = new SpiralGridAlgorithm();
		spiral.setMaxDistance(ViewDistance);
		loadingLoop = new SecondaryLoop(camera, machine, ViewDistance+3);
		chunkLoadHeight[1] = 0;
	}
	public void dispose(){
		loadingLoop.dispose();
		shader.dispose();
		for(LandscapeChunk c : chunks)
			c.dispose();
		chunks.clear();
	}
	public boolean isWithinView(int x, int z){
		x = Algorithms.groupLocation(x, 64);
		z = Algorithms.groupLocation(z, 64);
		for(LandscapeChunk c : chunks)
			if(c.getX()==x&&c.getZ()==z){
				return isWithinView(c, ViewDistance);
			}
		return false;
	}
	public void render(){
		shader.bind();
		for(LandscapeChunk c : chunks)
			if(isWithinView(c, ViewDistance)
				&&camera.cubeInView(c.getX(), c.getY(), c.getZ(), LandscapeChunk.LandscapeSize)){
				shader.setUniform2f(1, c.getX(), c.getZ());
				c.render();
			}
	}
	public void setup(Grasslands grassLands){
		this.grassLands = grassLands;
	}
	public void start(){
		loadingLoop.start();
	}
	public void update(double time){
		for(BiomeParticleEngine bp : biomeParticleEngines)
			bp.update(time);
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
		if(frame%ChunkPingRate==0){
			if(frame%(ChunkPingRate*2)==0){
				if(currentLoadingChunk!=null){
					if(currentLoadingChunk.isFinished()){
						loadChunk(currentLoadingChunk.getX(), currentLoadingChunk.getY(),
							currentLoadingChunk.getZ(), Algorithms.getChunkPath(currentLoadingChunk.getX(),
								currentLoadingChunk.getY(), currentLoadingChunk.getZ()));
						currentLoadingChunk = null;
					}
					return;
				}
				if(!spiral.hasNext())
					return;
				if(chunkLoadHeight[1]!=0){
					loadNextChunkHeight();
					return;
				}
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
		LandscapeChunk ch;
		int a;
		clearer:for(int i = 0; i<chunks.size();){
			ch = chunks.get(i);
			if(shouldRemove(ch)){
				ch.dispose();
				chunks.remove(i);
				for(LandscapeChunk c : chunks)
					if(c.getX()==ch.getX()&&c.getZ()==ch.getZ())
						continue clearer;
				for(a = 0; a<biomeParticleEngines.size(); a++)
					if(biomeParticleEngines.get(a).getX()==ch.getX()
						&&biomeParticleEngines.get(a).getZ()==ch.getZ()){
						biomeParticleEngines.remove(a).dispose();
						continue clearer;
					}
				throw new AssertionError();
			}
			i++;
		}
	}
	private LandscapeChunk getContainingChunk(int x, int y, int z, boolean load, ChunkHeightData heightData){
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
		File file = Algorithms.getChunkPath(x, y, z);
		if(!(file.exists()&&file.length()>0)){
			currentLoadingChunk = loadingLoop.getQue().place(x, y, z, heightData);
			return null;
		}
		return loadChunk(x, y, z, file);
	}
	private boolean isWithinView(LandscapeChunk c, int distance){
		int x = Algorithms.groupLocation((int)camera.x, 64)/64;
		int z = Algorithms.groupLocation((int)camera.z, 64)/64;
		return Math.abs(x-c.getX()/64)<=distance&&Math.abs(z-c.getZ()/64)<=distance;
	}
	private LandscapeChunk loadChunk(int x, int y, int z, File file){
		LandscapeChunk c = new LandscapeChunk(entityDatabase, grassLands, x, y, z, file);
		chunks.add(c);
		if(grassLands!=null)
			grassLands.updateVisibility();
		return c;
	}
	private void loadChunks(int x, int z){
		loadMassChunkHeightData(x, z);
		chunkHeightDataTemp = new ChunkHeightData(machine, x, z, massChunkHeightData);
		chunkHeightDataTemp.getChunkHeight(chunkLoadHeight);
		chunkLoadHeight[2] = x;
		chunkLoadHeight[3] = z;
		chunkLoadHeight[4] = 0;
		boolean place = true;
		for(BiomeParticleEngine bp : biomeParticleEngines)
			if(bp.getX()==x&&bp.getZ()==z){
				place = false;
				break;
			}
		if(place)
			biomeParticleEngines.add(new BiomeParticleEngine(x, z, particleBatch, chunkHeightDataTemp, camera));
		loadNextChunkHeight();
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
	private void loadNextChunkHeight(){
		getContainingChunk(chunkLoadHeight[2], chunkLoadHeight[4]*LandscapeChunk.LandscapeSize
			+chunkLoadHeight[0], chunkLoadHeight[3], true, chunkHeightDataTemp);
		chunkLoadHeight[4]++;
		if(chunkLoadHeight[4]==chunkLoadHeight[1]){
			chunkLoadHeight[1] = 0;
			chunkHeightDataTemp = null;
		}
	}
	private boolean shouldRemove(LandscapeChunk chunk){
		return !isWithinView(chunk, ViewDistance+3);
	}
}
