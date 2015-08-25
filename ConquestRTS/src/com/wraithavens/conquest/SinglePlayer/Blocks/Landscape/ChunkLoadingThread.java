package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Block;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkXQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkYQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkZQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ExtremeQuadOptimizer;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Quad;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.QuadListener;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.QuadOptimizer;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassTransform;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;
import com.wraithavens.conquest.Utility.QuadList;

public class ChunkLoadingThread implements Runnable{
	private volatile boolean running = true;
	private final ChunkWorkerQue que;
	private final int[][] heights = new int[LandscapeChunk.LandscapeSize+2][LandscapeChunk.LandscapeSize+2];
	private final ChunkXQuadCounter xCounter = new ChunkXQuadCounter();
	private final ChunkYQuadCounter yCounter = new ChunkYQuadCounter();
	private final ChunkZQuadCounter zCounter = new ChunkZQuadCounter();
	private final QuadList quadList = new QuadList();
	private final int[][] quads = new int[LandscapeChunk.LandscapeSize][LandscapeChunk.LandscapeSize];
	private final int[][] storage = new int[LandscapeChunk.LandscapeSize][LandscapeChunk.LandscapeSize];
	private final int[][] tempStorage = new int[LandscapeChunk.LandscapeSize][LandscapeChunk.LandscapeSize];
	private final VertexStorage vertices = new VertexStorage();
	private final IndexStorage indices = new IndexStorage();
	private final WorldNoiseMachine machine;
	ChunkLoadingThread(int id, ChunkWorkerQue que, WorldNoiseMachine machine){
		this.que = que;
		this.machine = machine;
		Thread t = new Thread(this);
		t.setName("Chunk Loading Worker "+id);
		t.setDaemon(true);
		t.start();
	}
	public void run(){
		while(running){
			try{
				genChunk(que.take());
			}catch(Exception exception){
				exception.printStackTrace();
			}
		}
	}
	private void genChunk(ChunkWorkerTask task){
		genChunk(Algorithms.getChunkPath(task.getX(), task.getY(), task.getZ()), task.getX(), task.getY(),
			task.getZ());
		task.setFinished();
		SecondaryLoop.chunksLoaded++;
	}
	private void genChunk(File file, int x, int y, int z){
		System.out.println("New landmass discovered. Generating now. ["+x+", "+y+", "+z+"]");
		// ---
		// Prepare the quad building algorithm.
		// ---
		quadList.clear();
		QuadListener listener = new QuadListener(){
			public void addQuad(Quad q){
				quadList.add(q);
			}
		};
		// ---
		// Calculate the world heights.
		// ---
		int a, b, c, j, q;
		int maxHeight = 0;
		for(a = 0; a<LandscapeChunk.LandscapeSize+2; a++)
			for(b = 0; b<LandscapeChunk.LandscapeSize+2; b++){
				heights[a][b] = machine.getGroundLevel(a-1+x, b-1+z)-1;
				if(heights[a][b]>maxHeight)
					maxHeight = heights[a][b];
			}
		maxHeight -= y;
		maxHeight += 1;
		// ---
		// Combine the quads into their final form.
		// ---
		boolean hasBack;
		boolean placeQuad;
		for(j = 0; j<6; j++){
			if(j==3)
				continue;
			if(j==0||j==1){
				for(a = 0; a<LandscapeChunk.LandscapeSize; a++){
					for(b = 0; b<LandscapeChunk.LandscapeSize; b++)
						for(c = 0; c<LandscapeChunk.LandscapeSize; c++){
							hasBack = heights[a+1][c+1]>=b+y;
							placeQuad = heights[a+1+(j==0?1:-1)][c+1]<b+y;
							if(hasBack&&placeQuad)
								quads[b][c] = 1;
							else if(placeQuad)
								quads[b][c] = -1;
							else
								quads[b][c] = 0;
						}
					q =
						ExtremeQuadOptimizer.optimize(storage, tempStorage, quads, LandscapeChunk.LandscapeSize,
							LandscapeChunk.LandscapeSize);
					if(q==0)
						continue;
					xCounter.setup(x, y, z, a, j, listener, Block.GRASS);
					QuadOptimizer.countQuads(xCounter, storage, LandscapeChunk.LandscapeSize,
						LandscapeChunk.LandscapeSize, q);
				}
			}else if(j==2){
				for(b = 0; b<maxHeight; b++){
					for(a = 0; a<LandscapeChunk.LandscapeSize; a++)
						for(c = 0; c<LandscapeChunk.LandscapeSize; c++){
							if(heights[a+1][c+1]==b+y)
								quads[a][c] = 1;
							else if(heights[a+1][c+1]<b+y)
								quads[a][c] = -1;
							else
								quads[a][c] = 0;
						}
					q =
						ExtremeQuadOptimizer.optimize(storage, tempStorage, quads, LandscapeChunk.LandscapeSize,
							LandscapeChunk.LandscapeSize);
					if(q==0)
						continue;
					yCounter.setup(x, y, z, b, j, listener, Block.GRASS);
					QuadOptimizer.countQuads(yCounter, storage, LandscapeChunk.LandscapeSize,
						LandscapeChunk.LandscapeSize, q);
				}
			}else{
				for(c = 0; c<LandscapeChunk.LandscapeSize; c++){
					for(a = 0; a<LandscapeChunk.LandscapeSize; a++)
						for(b = 0; b<LandscapeChunk.LandscapeSize; b++){
							hasBack = heights[a+1][c+1]>=b+y;
							placeQuad = heights[a+1][c+1+(j==4?1:-1)]<b+y;
							if(hasBack&&placeQuad)
								quads[a][b] = 1;
							else if(placeQuad)
								quads[a][b] = -1;
							else
								quads[a][b] = 0;
						}
					q =
						ExtremeQuadOptimizer.optimize(storage, tempStorage, quads, LandscapeChunk.LandscapeSize,
							LandscapeChunk.LandscapeSize);
					if(q==0)
						continue;
					zCounter.setup(x, y, z, c, j, listener, Block.GRASS);
					QuadOptimizer.countQuads(zCounter, storage, LandscapeChunk.LandscapeSize,
						LandscapeChunk.LandscapeSize, q);
				}
			}
		}
		// ---
		// Build the vertices and indices.
		// ---
		vertices.clear();
		indices.clear();
		int v0, v1, v2, v3;
		byte shade;
		Quad quad;
		for(int i = 0; i<quadList.size(); i++){
			quad = quadList.get(i);
			shade = (byte)(quad.side==2?255:quad.side==3?130:quad.side==0||quad.side==1?200:180);
			v0 = vertices.indexOf(quad.data.get(0), quad.data.get(1), quad.data.get(2), shade);
			v1 = vertices.indexOf(quad.data.get(3), quad.data.get(4), quad.data.get(5), shade);
			v2 = vertices.indexOf(quad.data.get(6), quad.data.get(7), quad.data.get(8), shade);
			v3 = vertices.indexOf(quad.data.get(9), quad.data.get(10), quad.data.get(11), shade);
			indices.place(v0);
			indices.place(v1);
			indices.place(v2);
			indices.place(v0);
			indices.place(v2);
			indices.place(v3);
		}
		// ---
		// Load plantlife.
		// ---
		HashMap<EntityType,ArrayList<GrassTransform>> grassLocations = new HashMap();
		HashMap<EntityType,ArrayList<Vector3f>> plantLocations = new HashMap();
		EntityType entity;
		for(a = 0; a<LandscapeChunk.LandscapeSize; a++)
			for(b = 0; b<LandscapeChunk.LandscapeSize; b++){
				entity = machine.randomPlant(a+x, b+z);
				if(entity!=null){
					if(entity.isGrass){
						GrassTransform loc =
							new GrassTransform(a+x+0.5f, machine.getGroundLevel(a+x, b+z), b+z+0.5f,
								(float)(Math.random()*Math.PI*2), 2.0f+(float)(Math.random()*0.3f-0.15f));
						if(grassLocations.containsKey(entity))
							grassLocations.get(entity).add(loc);
						else{
							ArrayList<GrassTransform> locs = new ArrayList();
							locs.add(loc);
							grassLocations.put(entity, locs);
						}
					}else{
						Vector3f loc = new Vector3f(a+x+0.5f, machine.getGroundLevel(a+x, b+z), b+z+0.5f);
						if(plantLocations.containsKey(entity))
							plantLocations.get(entity).add(loc);
						else{
							ArrayList<Vector3f> locs = new ArrayList();
							locs.add(loc);
							plantLocations.put(entity, locs);
						}
					}
				}
			}
		int bytes = 8;
		for(EntityType type : plantLocations.keySet()){
			bytes += 8;
			bytes += plantLocations.get(type).size()*5*4;
		}
		for(EntityType type : grassLocations.keySet()){
			bytes += 8;
			bytes += grassLocations.get(type).size()*5*4;
		}
		// ---
		// Compile and save.
		// ---
		BinaryFile bin = new BinaryFile(vertices.size()*13+indices.size()*4+8+bytes+64*64*64*3);
		bin.addInt(vertices.size());
		bin.addInt(indices.size());
		Vertex v;
		for(int i = 0; i<vertices.size(); i++){
			v = vertices.get(i);
			bin.addFloat(v.getX());
			bin.addFloat(v.getY());
			bin.addFloat(v.getZ());
			bin.addByte(v.getShade());
		}
		for(int i = 0; i<indices.size(); i++)
			bin.addInt(indices.get(i));
		bin.addInt(plantLocations.size());
		for(EntityType type : plantLocations.keySet()){
			bin.addInt(type.ordinal());
			ArrayList<Vector3f> locs = plantLocations.get(type);
			bin.addInt(locs.size());
			for(int i = 0; i<locs.size(); i++){
				Vector3f loc = locs.get(i);
				bin.addFloat(loc.x);
				bin.addFloat(type.isGiant?loc.y-5.1f:loc.y);
				bin.addFloat(loc.z);
				bin.addFloat(type.isGiant?1:(float)(Math.random()*0.1f-0.05f+1/5f));
				bin.addFloat((float)(Math.random()*360));
			}
		}
		bin.addInt(grassLocations.size());
		for(EntityType type : grassLocations.keySet()){
			bin.addInt(type.ordinal());
			ArrayList<GrassTransform> locs = grassLocations.get(type);
			bin.addInt(locs.size());
			for(GrassTransform loc : locs){
				bin.addFloat(loc.getX());
				bin.addFloat(loc.getY());
				bin.addFloat(loc.getZ());
				bin.addFloat(loc.getRotation());
				bin.addFloat(loc.getScale());
			}
		}
		{
			// ---
			// Now load the 3D texture.
			// ---
			{
				// ---
				// Generate biome colors.
				// ---
				int blockX, blockY, blockZ;
				byte red, green, blue;
				Vector3f colors = new Vector3f();
				for(blockZ = 0; blockZ<64; blockZ++)
					for(blockY = 0; blockY<64; blockY++)
						for(blockX = 0; blockX<64; blockX++){
							machine.getBiomeColorAt(blockX+x, blockY+y, blockZ+z, colors);
							red = (byte)Math.round(colors.x*255);
							green = (byte)Math.round(colors.y*255);
							blue = (byte)Math.round(colors.z*255);
							bin.addByte(red);
							bin.addByte(green);
							bin.addByte(blue);
						}
			}
		}
		bin.compress(false);
		bin.compile(file);
	}
	void dispose(){
		running = false;
	}
}
