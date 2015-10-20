package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vec3i;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Block;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.BlockTextures;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.BlockData;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkNotGeneratedException;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.MeshFormatter;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.MeshRenderer;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder.BiomeColorDataPacket;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder.ChunkPainter;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder.MeshDataPacket;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.Grasslands;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.Particles.BiomeParticleEngine;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleBatch;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.Algorithms;

public class LandscapeWorld{
	public static Block getBlock(int x, int y, int z) throws ChunkNotGeneratedException{
		int chunkX = Algorithms.groupLocation(x, 64);
		int chunkY = Algorithms.groupLocation(y, 64);
		int chunkZ = Algorithms.groupLocation(z, 64);
		int block = BlockData.getBlockFromFile(chunkX, chunkY, chunkZ, x-chunkX, y-chunkY, z-chunkZ)&0xFF;
		if(block==255)
			return null;
		return Block.values()[block];
	}
	private static final BlockData tempBlockData = new BlockData(new MeshFormatter());
	static int ShadeAttribLocation;
	static int UvAttribLocation;
	private final ArrayList<LandscapeChunk> chunks = new ArrayList();
	private final ShaderProgram shader;
	private final SpiralGridAlgorithm spiral;
	private final Camera camera;
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
	private LinkedList<ChunkRepaintRequest> repaintRequests = new LinkedList();
	public LandscapeWorld(WorldNoiseMachine machine, Camera camera, ParticleBatch particleBatch){
		this.camera = camera;
		this.machine = machine;
		this.particleBatch = particleBatch;
		shader = new ShaderProgram("Landscape");
		shader.bind();
		shader.loadUniforms("colorMap", "offset", "texture1", "clipmap");
		shader.setUniform1I(0, 0);
		shader.setUniform1I(2, 1);
		shader.setUniform1I(3, 2);
		ShadeAttribLocation = shader.getAttributeLocation("shade");
		UvAttribLocation = shader.getAttributeLocation("att_uv");
		GL20.glEnableVertexAttribArray(ShadeAttribLocation);
		GL20.glEnableVertexAttribArray(UvAttribLocation);
		spiral = new SpiralGridAlgorithm();
		spiral.setMaxDistance(WraithavensConquest.Settings.getChunkRenderDistance());
		loadingLoop = new SecondaryLoop(camera, machine, WraithavensConquest.Settings.getChunkLoadDistance());
		chunkLoadHeight[1] = 0;
		BlockTextures.load();
	}
	public void addRepaintRequest(ChunkRepaintRequest req){
		repaintRequests.add(req);
		loadingLoop.addRepaintRequest(req.getX(), req.getY(), req.getZ());
	}
	public void dispose(){
		loadingLoop.dispose();
		BlockTextures.dispose();
		shader.dispose();
		for(LandscapeChunk c : chunks)
			c.dispose();
		chunks.clear();
	}
	public SecondaryLoop getLoadingLoop(){
		return loadingLoop;
	}
	public boolean isWithinView(int x, int z){
		x = Algorithms.groupLocation(x, 64);
		z = Algorithms.groupLocation(z, 64);
		for(LandscapeChunk c : chunks)
			if(c.getX()==x&&c.getZ()==z){
				return isWithinView(c, WraithavensConquest.Settings.getChunkRenderDistance());
			}
		return false;
	}
	public void render(){
		shader.bind();
		BlockTextures.bind();
		for(LandscapeChunk c : chunks)
			if(isWithinView(c, WraithavensConquest.Settings.getChunkRenderDistance())
				&&camera.cubeInView(c.getX(), c.getY(), c.getZ(), LandscapeChunk.LandscapeSize)){
				shader.setUniform3f(1, c.getX(), c.getY(), c.getZ());
				c.render();
			}
	}
	public void setBlock(int x, int y, int z, Block type){
		byte block = type==null?(byte)255:type.id();
		int chunkX = Algorithms.groupLocation(x, 64);
		int chunkY = Algorithms.groupLocation(y, 64);
		int chunkZ = Algorithms.groupLocation(z, 64);
		x -= chunkX;
		y -= chunkY;
		z -= chunkZ;
		try{
			tempBlockData.loadFromFile(chunkX, chunkY, chunkZ);
			tempBlockData.setBlock(x, y, z, block);
			tempBlockData.saveToFile(chunkX, chunkY, chunkZ);
			addRepaintRequest(new ChunkRepaintRequest(chunkX, chunkY, chunkZ, tempBlockData.mesh(true)));
		}catch(ChunkNotGeneratedException e){
			fullyGenerateChunk(chunkX, chunkY, chunkZ);
			tempBlockData.setBlock(x, y, z, block);
			tempBlockData.saveToFile(chunkX, chunkY, chunkZ);
			addRepaintRequest(new ChunkRepaintRequest(chunkX, chunkY, chunkZ, tempBlockData.mesh(true)));
		}
		ArrayList<Vec3i> toGen = new ArrayList();
		{
			// ---
			// Update touching chunks.
			// ---
			if(x==0){
				try{
					chunkX -= 64;
					tempBlockData.loadFromFile(chunkX, chunkY, chunkZ);
					tempBlockData.setBlock(x+64, y, z, block);
					tempBlockData.saveToFile(chunkX, chunkY, chunkZ);
					addRepaintRequest(new ChunkRepaintRequest(chunkX, chunkY, chunkZ, tempBlockData.mesh(true)));
				}catch(ChunkNotGeneratedException e){
					// Generate this chunk.
					toGen.add(new Vec3i(chunkX, chunkY, chunkZ));
				}
			}else if(x==63){
				try{
					chunkX += 64;
					tempBlockData.loadFromFile(chunkX, chunkY, chunkZ);
					tempBlockData.setBlock(x-64, y, z, block);
					tempBlockData.saveToFile(chunkX, chunkY, chunkZ);
					addRepaintRequest(new ChunkRepaintRequest(chunkX, chunkY, chunkZ, tempBlockData.mesh(true)));
				}catch(ChunkNotGeneratedException e){
					// Generate this chunk.
					toGen.add(new Vec3i(chunkX, chunkY, chunkZ));
				}
			}
			if(y==0){
				try{
					chunkY -= 64;
					tempBlockData.loadFromFile(chunkX, chunkY, chunkZ);
					tempBlockData.setBlock(x, y+64, z, block);
					tempBlockData.saveToFile(chunkX, chunkY, chunkZ);
					addRepaintRequest(new ChunkRepaintRequest(chunkX, chunkY, chunkZ, tempBlockData.mesh(true)));
				}catch(ChunkNotGeneratedException e){
					// Generate this chunk.
					toGen.add(new Vec3i(chunkX, chunkY, chunkZ));
				}
			}else if(y==63){
				try{
					chunkY += 64;
					tempBlockData.loadFromFile(chunkX, chunkY, chunkZ);
					tempBlockData.setBlock(x, y-64, z, block);
					tempBlockData.saveToFile(chunkX, chunkY, chunkZ);
					addRepaintRequest(new ChunkRepaintRequest(chunkX, chunkY, chunkZ, tempBlockData.mesh(true)));
				}catch(ChunkNotGeneratedException e){
					// Generate this chunk.
					toGen.add(new Vec3i(chunkX, chunkY, chunkZ));
				}
			}
			if(z==0){
				try{
					chunkZ -= 64;
					tempBlockData.loadFromFile(chunkX, chunkY, chunkZ);
					tempBlockData.setBlock(x, y, z+64, block);
					tempBlockData.saveToFile(chunkX, chunkY, chunkZ);
					addRepaintRequest(new ChunkRepaintRequest(chunkX, chunkY, chunkZ, tempBlockData.mesh(true)));
				}catch(ChunkNotGeneratedException e){
					// Generate this chunk.
					toGen.add(new Vec3i(chunkX, chunkY, chunkZ));
				}
			}else if(z==63){
				try{
					chunkZ += 64;
					tempBlockData.loadFromFile(chunkX, chunkY, chunkZ);
					tempBlockData.setBlock(x, y, z-64, block);
					tempBlockData.saveToFile(chunkX, chunkY, chunkZ);
					addRepaintRequest(new ChunkRepaintRequest(chunkX, chunkY, chunkZ, tempBlockData.mesh(true)));
				}catch(ChunkNotGeneratedException e){
					// Generate this chunk.
					toGen.add(new Vec3i(chunkX, chunkY, chunkZ));
				}
			}
		}
		// TODO Move this chunk generation process to loading loop.
		for(Vec3i co : toGen){
			fullyGenerateChunk(co.x, co.y, co.z);
			tempBlockData.saveToFile(co.x, co.y, co.z);
			addRepaintRequest(new ChunkRepaintRequest(co.x, co.y, co.z, tempBlockData.mesh(true)));
		}
	}
	public void setRenderDistance(int renderDistance){
		spiral.setMaxDistance(renderDistance);
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
		if(frame%WraithavensConquest.Settings.getChunkUpdateFrames()==0){
			if(frame%(WraithavensConquest.Settings.getChunkUpdateFrames()*2)==0){
				if(repaintRequests.size()>0){
					ChunkRepaintRequest req = repaintRequests.removeFirst();
					for(LandscapeChunk chunk : chunks){
						if(chunk.getX()==req.getX()&&chunk.getY()==req.getY()&&chunk.getZ()==req.getZ()){
							chunk.reload(req.getNewMesh());
							return;
						}
					}
					return;
				}
				if(currentLoadingChunk!=null){
					if(currentLoadingChunk.isFinished()){
						loadChunk(currentLoadingChunk.getX(), currentLoadingChunk.getY(),
							currentLoadingChunk.getZ());
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
			}
			i++;
		}
	}
	private void fullyGenerateChunk(int x, int y, int z){
		boolean air = machine.getGroundLevel(x, z)<y; // Works in theory.
		loadMassChunkHeightData(x, z);
		int[] h = new int[2];
		massChunkHeightData.getHeights(x, z, h);
		if(!air)
			h[0] -= 64;
		h[1]++;
		massChunkHeightData.setHeight(x, z, h);
		tempBlockData.clear();
		if(!air)
			tempBlockData.fill(Block.Dirt.id());
		{
			// ---
			// Fill in clip data.
			// ---
			BlockData blockData = new BlockData(null);
			int a, b, c;
			try{
				blockData.loadFromFile(x+64, y, z);
				a = 0;
				for(b = 0; b<64; b++)
					for(c = 0; c<64; c++)
						tempBlockData.setBlock(64, b, c, blockData.getBlock(a, b, c));
			}catch(ChunkNotGeneratedException e){} // Ignore chunk, then.
			try{
				blockData.loadFromFile(x-64, y, z);
				a = 63;
				for(b = 0; b<64; b++)
					for(c = 0; c<64; c++)
						tempBlockData.setBlock(-1, b, c, blockData.getBlock(a, b, c));
			}catch(ChunkNotGeneratedException e){} // Ignore chunk, then.
			try{
				blockData.loadFromFile(x, y+64, z);
				b = 0;
				for(a = 0; a<64; a++)
					for(c = 0; c<64; c++)
						tempBlockData.setBlock(a, 64, c, blockData.getBlock(a, b, c));
			}catch(ChunkNotGeneratedException e){} // Ignore chunk, then.
			try{
				blockData.loadFromFile(x, y-64, z);
				b = 63;
				for(a = 0; a<64; a++)
					for(c = 0; c<64; c++)
						tempBlockData.setBlock(a, -1, c, blockData.getBlock(a, b, c));
			}catch(ChunkNotGeneratedException e){} // Ignore chunk, then.
			try{
				blockData.loadFromFile(x, y, z+64);
				c = 0;
				for(a = 0; a<64; a++)
					for(b = 0; b<64; b++)
						tempBlockData.setBlock(a, b, 64, blockData.getBlock(a, b, c));
			}catch(ChunkNotGeneratedException e){} // Ignore chunk, then.
			try{
				blockData.loadFromFile(x, y, z-64);
				c = 63;
				for(a = 0; a<64; a++)
					for(b = 0; b<64; b++)
						tempBlockData.setBlock(a, b, -1, blockData.getBlock(a, b, c));
			}catch(ChunkNotGeneratedException e){} // Ignore chunk, then.
		}
		// TODO Move this chunk generation process to the loading loop.
		ChunkPainter chunkPainter = new ChunkPainter();
		MeshRenderer newMesh = tempBlockData.mesh(true);
		chunkPainter.getPackets().add(new MeshDataPacket(newMesh));
		{
			// ---
			// Get biome colors.
			// ---
			// TODO Make this use actual biome color data.
			ByteBuffer colorData = ByteBuffer.allocate(64*64*3);
			while(colorData.hasRemaining())
				colorData.put((byte)255);
			colorData.flip();
			chunkPainter.getPackets().add(new BiomeColorDataPacket(colorData));
		}
		chunkPainter.save(x, y, z);
		loadChunk(x, y, z);
		addRepaintRequest(new ChunkRepaintRequest(x, y, z, newMesh));
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
		return loadChunk(x, y, z);
	}
	private boolean isWithinView(LandscapeChunk c, int distance){
		int x = Algorithms.groupLocation((int)camera.x, 64)/64;
		int z = Algorithms.groupLocation((int)camera.z, 64)/64;
		return Math.abs(x-c.getX()/64)<=distance&&Math.abs(z-c.getZ()/64)<=distance;
	}
	private LandscapeChunk loadChunk(int x, int y, int z){
		LandscapeChunk c = new LandscapeChunk(x, y, z);
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
		return !isWithinView(chunk, WraithavensConquest.Settings.getChunkCatcheDistance());
	}
}
