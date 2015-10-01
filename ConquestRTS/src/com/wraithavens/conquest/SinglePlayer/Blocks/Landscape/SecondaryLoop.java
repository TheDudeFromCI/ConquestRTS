package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Block;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkXQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkYQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ChunkZQuadCounter;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.ExtremeQuadOptimizer;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Quad;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.QuadListener;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityTransform;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities.GiantEntityDictionary;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassTransform;
import com.wraithavens.conquest.SinglePlayer.Noise.Biome;
import com.wraithavens.conquest.SinglePlayer.Noise.PointGenerator2D;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;
import com.wraithavens.conquest.Utility.QuadList;

public class SecondaryLoop implements Runnable{
	private static Biome randomBiomeObject(float h, float t){
		final float mapSize = WorldNoiseMachine.BiomeTransitionSize;
		h *= mapSize;
		t *= mapSize;
		Biome c1 = Biome.getFittingBiome((int)h/mapSize, (int)t/mapSize, 1.0f);
		Biome c2 = Biome.getFittingBiome((int)(h+1)/mapSize, (int)t/mapSize, 1.0f);
		Biome c3 = Biome.getFittingBiome((int)h/mapSize, (int)(t+1)/mapSize, 1.0f);
		Biome c4 = Biome.getFittingBiome((int)(h+1)/mapSize, (int)(t+1)/mapSize, 1.0f);
		if(c1==c2&&c2==c3&&c3==c4)
			return c1;
		float x = h-(int)h;
		float y = t-(int)t;
		float p1 = 1/(float)(Math.pow(x, 2)+Math.pow(y, 2));
		float p2 = 1/(float)(Math.pow(x-1, 2)+Math.pow(y, 2));
		float p3 = 1/(float)(Math.pow(x, 2)+Math.pow(y-1, 2));
		float p4 = 1/(float)(Math.pow(x-1, 2)+Math.pow(y-1, 2));
		float c = p1+p2+p3+p4;
		p1 /= c;
		p2 = p2/c+p1;
		p3 = p3/c+p2;
		// Normally this should also be done to p4, but since it's never used
		// again, I'll skip it. :P (Though it would always == 1.)
		float r = (float)Math.random();
		if(r<=p1)
			return c1;
		if(r<=p2)
			return c2;
		if(r<=p3)
			return c3;
		return c4;
	}
	private static EntityType randomPlant(float h, float t, int x, int z, long seed){
		{
			long tr = seed;
			tr = tr*s+x;
			tr = tr*s+z;
			tr += x*x+s;
			tr += z*z+s;
			random.setSeed(tr);
			random.nextFloat(); // This helps shuffle up the random number, a
			// lot.
		}
		if(random.nextFloat()<0.2){
			Biome biome = randomBiomeObject(h, t);
			switch(biome){
				case TayleaMeadow:
					if(random.nextFloat()<0.02)
						return EntityType.getVariation(EntityType.TayleaFlower, (int)(random.nextFloat()*6));
					if(random.nextFloat()<0.0025)
						return EntityType.getVariation(EntityType.VallaFlower, (int)(random.nextFloat()*4));
					if(random.nextFloat()<0.005)
						return EntityType.values()[EntityType.TayleaMeadowRock1.ordinal()
							+(int)(random.nextFloat()*3)];
					return EntityType.values()[EntityType.Grass.ordinal()+(int)(random.nextFloat()*8)];
				case ArcstoneHills:
					return EntityType.values()[EntityType.Grass.ordinal()+(int)(random.nextFloat()*8)];
				case AesiaFields:
					if(random.nextFloat()<0.03)
						return EntityType.getVariation(EntityType.AesiaStems, (int)(random.nextFloat()*24));
					if(random.nextFloat()<0.1)
						return EntityType.getVariation(EntityType.AesiaPedals, (int)(random.nextFloat()*7));
					return EntityType.values()[EntityType.Grass.ordinal()+(int)(random.nextFloat()*8)];
				default:
					throw new AssertionError();
			}
		}
		return null;
	}
	private static final long s = 4294967291L;
	private static final Random random = new Random();
	private volatile boolean running = true;
	private final SpiralGridAlgorithm spiral;
	private volatile Camera camera;
	private final int[] temp = new int[2];
	private int lastX = Integer.MAX_VALUE;
	private int lastZ = Integer.MAX_VALUE;
	private MassChunkHeightData massChunkHeightData;
	private final ChunkWorkerQue que;
	private final WorldNoiseMachine machine;
	private final int[][] heights = new int[66][66];
	private final ChunkXQuadCounter xCounter = new ChunkXQuadCounter();
	private final ChunkYQuadCounter yCounter = new ChunkYQuadCounter();
	private final ChunkZQuadCounter zCounter = new ChunkZQuadCounter();
	private final QuadList quadList = new QuadList();
	private final int[][] quads = new int[64][64];
	private final int[][] storage = new int[64][64];
	private final int[][] tempStorage = new int[64][64];
	private final VertexStorage vertices = new VertexStorage();
	private final IndexStorage indices = new IndexStorage();
	private final GiantEntityDictionary dictionary;
	private final PointGenerator2D giantEntitySpawner;
	private Thread t;
	private boolean working;
	SecondaryLoop(Camera camera, WorldNoiseMachine machine, int maxLoadDistance){
		dictionary = new GiantEntityDictionary();
		giantEntitySpawner =
			new PointGenerator2D(machine.getGiantEntitySeed(), dictionary.getAverageDistance(),
				dictionary.getMinDistance(), 1.0f);
		this.camera = camera;
		this.machine = machine;
		spiral = new SpiralGridAlgorithm();
		// ---
		// And this should prevent the map from generating too many chunks will
		// AFK.
		// ---
		spiral.setMaxDistance(maxLoadDistance);
		que = new ChunkWorkerQue();
		t = new Thread(this);
		t.setName("Secondary Loading Thread");
		t.setDaemon(true);
	}
	public void run(){
		while(running)
			loadNext();
	}
	public void setMaxLoadDistance(int distance){
		spiral.setMaxDistance(distance);
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
				try{
					Thread.sleep(1);
				}catch(Exception exception){
					exception.printStackTrace();
				}
				continue;
			}
			if(heightData==null)
				heightData = new ChunkHeightData(machine, x, z, massChunkHeightData);
			if(que.size()>0){
				ChunkWorkerTask task = que.take();
				genChunk(Algorithms.getChunkPath(task.getX(), task.getY(), task.getZ()), task.getX(),
					task.getY(), task.getZ(), task.getHeightData());
				task.setFinished();
			}
			genChunk(Algorithms.getChunkPath(x, y, z), x, y, z, heightData);
		}
	}
	private void genChunk(File file, int x, int y, int z, ChunkHeightData heightData){
		System.out.println("New landmass discovered. Generating now. ["+x+", "+y+", "+z+"]");
		// ---
		// Prepare the quad building algorithm.
		// ---
		quadList.clear();
		indices.clear();
		vertices.clear();
		QuadListener listener = new QuadListener(){
			public void addQuad(Quad q){
				quadList.add(q);
			}
		};
		// ---
		// Calculate the world heights.
		// ---
		int a, b, c, j, q, h;
		int maxHeight = Integer.MIN_VALUE;
		int minHeight = Integer.MAX_VALUE;
		int tempA, tempB, tempC;
		for(a = 0; a<66; a++){
			tempA = a-1+x;
			for(b = 0; b<66; b++){
				heights[a][b] =
					(a==0||b==0||a==65||b==65?machine.getGroundLevel(tempA, b-1+z):heightData.getHeight(tempA, b
						-1+z))-1;
				if(heights[a][b]>maxHeight)
					maxHeight = heights[a][b];
				if(heights[a][b]<minHeight)
					minHeight = heights[a][b];
			}
		}
		maxHeight -= y-1;
		minHeight -= y;
		if(maxHeight>64)
			maxHeight = 64;
		// ---
		// Combine the quads into their final form.
		// ---
		boolean hasBack;
		boolean placeQuad;
		for(j = 0; j<6; j++){
			if(j==3)
				continue;
			if(j==0||j==1){
				for(a = 0; a<64; a++){
					h = 0;
					tempA = a+1;
					for(b = 0; b<64; b++){
						tempB = b+y;
						for(c = 0; c<64; c++){
							tempC = c+1;
							hasBack = heights[tempA][tempC]>=tempB;
							placeQuad = heights[tempA+(j==0?1:-1)][tempC]<tempB;
							if(hasBack&&placeQuad){
								quads[b][c] = 1;
								h++;
							}else if(placeQuad)
								quads[b][c] = -1;
							else
								quads[b][c] = 0;
						}
					}
					if(h==0)
						continue;
					q = ExtremeQuadOptimizer.optimize(storage, tempStorage, quads, 64, 64);
					if(q==0)
						continue;
					xCounter.setup(x, y, z, a, j, listener, Block.Grass);
					ExtremeQuadOptimizer.countQuads(xCounter, storage, 64, 64, q);
				}
			}else if(j==2){
				for(b = minHeight; b<maxHeight; b++){
					tempB = b+y;
					h = 0;
					for(a = 0; a<64; a++){
						tempA = a+1;
						for(c = 0; c<64; c++){
							tempC = c+1;
							if(heights[tempA][tempC]==tempB){
								quads[a][c] = 1;
								h++;
							}else if(heights[tempA][tempC]<tempB)
								quads[a][c] = -1;
							else
								quads[a][c] = 0;
						}
					}
					if(h==0)
						continue;
					q = ExtremeQuadOptimizer.optimize(storage, tempStorage, quads, 64, 64);
					if(q==0)
						continue;
					yCounter.setup(x, y, z, b, j, listener, Block.Grass);
					ExtremeQuadOptimizer.countQuads(yCounter, storage, 64, 64, q);
				}
			}else{
				for(c = 0; c<64; c++){
					tempC = c+1;
					h = 0;
					for(a = 0; a<64; a++){
						tempA = a+1;
						for(b = 0; b<64; b++){
							tempB = b+y;
							hasBack = heights[tempA][tempC]>=tempB;
							placeQuad = heights[tempA][tempC+(j==4?1:-1)]<tempB;
							if(hasBack&&placeQuad){
								quads[a][b] = 1;
								h++;
							}else if(placeQuad)
								quads[a][b] = -1;
							else
								quads[a][b] = 0;
						}
					}
					if(h==0)
						continue;
					q = ExtremeQuadOptimizer.optimize(storage, tempStorage, quads, 64, 64);
					if(q==0)
						continue;
					zCounter.setup(x, y, z, c, j, listener, Block.Grass);
					ExtremeQuadOptimizer.countQuads(zCounter, storage, 64, 64, q);
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
		boolean grass;
		for(int i = 0; i<quadList.size(); i++){
			quad = quadList.get(i);
			shade = (byte)(quad.side==2?255:quad.side==3?130:quad.side==0||quad.side==1?200:180);
			grass = quad.grass;
			v0 =
				vertices.indexOf(quad.data.get(0), quad.data.get(1), quad.data.get(2), shade, quad.data.get(12),
					quad.data.get(13), (byte)quad.data.get(20), grass);
			v1 =
				vertices.indexOf(quad.data.get(3), quad.data.get(4), quad.data.get(5), shade, quad.data.get(14),
					quad.data.get(15), (byte)quad.data.get(20), grass);
			v2 =
				vertices.indexOf(quad.data.get(6), quad.data.get(7), quad.data.get(8), shade, quad.data.get(16),
					quad.data.get(17), (byte)quad.data.get(20), grass);
			v3 =
				vertices.indexOf(quad.data.get(9), quad.data.get(10), quad.data.get(11), shade,
					quad.data.get(18), quad.data.get(19), (byte)quad.data.get(20), grass);
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
		HashMap<EntityType,ArrayList<EntityTransform>> entityLocations = new HashMap();
		EntityType entity;
		float humidity;
		float tempature;
		Vector3f colorVec = new Vector3f();
		for(a = 0; a<LandscapeChunk.LandscapeSize; a++)
			for(b = 0; b<LandscapeChunk.LandscapeSize; b++){
				// ---
				// This little check is to stop multiple plants from spawning on
				// the same block while a chunk spans multiple verticle chunks.
				// ---
				h = heights[a+1][b+1];
				if(h<y||h>=y+64)
					continue;
				tempA = a+x;
				tempB = b+z;
				entity =
					randomPlant(humidity = heightData.getHumidity(tempA, tempB),
						tempature = heightData.getTempature(tempA, tempB), tempA, tempB,
						machine.getGiantEntitySeed()^100799);
				if(entity!=null){
					if(entity.isGrass){
						WorldNoiseMachine.getBiomeColorAt(humidity, tempature, colorVec);
						GrassTransform loc =
							new GrassTransform(tempA+0.5f, heightData.getHeight(tempA, tempB), tempB+0.5f,
								(float)(Math.random()*Math.PI*2), 2.0f+(float)(Math.random()*0.3f-0.15f),
								colorVec.x, colorVec.y, colorVec.z);
						if(grassLocations.containsKey(entity))
							grassLocations.get(entity).add(loc);
						else{
							ArrayList<GrassTransform> locs = new ArrayList();
							locs.add(loc);
							grassLocations.put(entity, locs);
						}
					}else{
						EntityTransform loc =
							new EntityTransform(null, tempA+0.5f, heightData.getHeight(tempA, tempB),
								tempB+0.5f, (float)(Math.random()*Math.PI*2), (float)(Math.random()*0.1f+0.15f));
						if(entityLocations.containsKey(entity))
							entityLocations.get(entity).add(loc);
						else{
							ArrayList<EntityTransform> locs = new ArrayList();
							locs.add(loc);
							entityLocations.put(entity, locs);
						}
					}
				}
			}
		ArrayList<EntityTransform> giants = new ArrayList();
		{
			ArrayList<float[]> locations = new ArrayList();
			giantEntitySpawner.noise(x, z, 64, locations);
			EntityType type;
			int fx, fz;
			for(float[] f : locations){
				fx = (int)Math.floor(f[0]);
				fz = (int)Math.floor(f[1]);
				type =
					dictionary.randomEntity(heightData.getBiome(fx, fz), machine.getGiantEntitySeed()+1, fx, fz);
				if(type!=null)
					giants.add(new EntityTransform(type, f[0], type.groundHits.getGround(machine, heightData,
						f[0], f[1], f[2], f[3]), f[1], f[2], f[3]));
			}
		}
		ArrayList<EntityTransform> locs;
		for(EntityTransform g : giants){
			if(entityLocations.containsKey(g.getType()))
				locs = entityLocations.get(g.getType());
			else{
				locs = new ArrayList();
				entityLocations.put(g.getType(), locs);
			}
			locs.add(g);
		}
		int bytes = 8;
		for(EntityType type : entityLocations.keySet()){
			bytes += 8;
			bytes += entityLocations.get(type).size()*5*4;
		}
		for(EntityType type : grassLocations.keySet()){
			bytes += 8;
			bytes += grassLocations.get(type).size()*8*4;
		}
		// ---
		// Compile and save.
		// ---
		BinaryFile bin = new BinaryFile(vertices.size()*(5*4+3)+indices.size()*4+8+bytes+64*64*3);
		bin.addInt(vertices.size());
		bin.addInt(indices.size());
		Vertex v;
		int i;
		for(i = 0; i<vertices.size(); i++){
			v = vertices.get(i);
			bin.addFloat(v.getX());
			bin.addFloat(v.getY());
			bin.addFloat(v.getZ());
			bin.addByte(v.getShade());
			bin.addFloat(v.getTx());
			bin.addFloat(v.getTy());
			bin.addByte(v.getTexture());
			bin.addBoolean(v.isGrass());
		}
		for(i = 0; i<indices.size(); i++)
			bin.addInt(indices.get(i));
		bin.addInt(entityLocations.size());
		EntityTransform loc;
		for(EntityType type : entityLocations.keySet()){
			bin.addInt(type.ordinal());
			locs = entityLocations.get(type);
			bin.addInt(locs.size());
			for(i = 0; i<locs.size(); i++){
				loc = locs.get(i);
				bin.addFloat(loc.getX());
				bin.addFloat(loc.getY());
				bin.addFloat(loc.getZ());
				bin.addFloat(loc.getR());
				bin.addFloat(loc.getS());
			}
		}
		bin.addInt(grassLocations.size());
		ArrayList<GrassTransform> locs1;
		for(EntityType type : grassLocations.keySet()){
			bin.addInt(type.ordinal());
			locs1 = grassLocations.get(type);
			bin.addInt(locs1.size());
			for(GrassTransform transform : locs1){
				bin.addFloat(transform.getX());
				bin.addFloat(transform.getY());
				bin.addFloat(transform.getZ());
				bin.addFloat(transform.getRotation());
				bin.addFloat(transform.getScale());
				bin.addFloat(transform.getRed());
				bin.addFloat(transform.getGreen());
				bin.addFloat(transform.getBlue());
			}
		}
		{
			{
				// ---
				// Now paint the biome colors.
				// ---
				int blockX, blockZ;
				byte red, green, blue;
				int tempX, tempZ;
				Vector3f colors = new Vector3f();
				for(blockZ = 0; blockZ<64; blockZ++)
					for(blockX = 0; blockX<64; blockX++){
						tempX = blockX+x;
						tempZ = blockZ+z;
						WorldNoiseMachine.getBiomeColorAt(heightData.getHumidity(tempX, tempZ),
							heightData.getTempature(tempX, tempZ), colors);
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
		try{
			Thread.sleep(1);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		updateCameraLocation();
		if(spiral.hasNext()){
			updateWorkingState(true);
			spiral.next();
			attemptGenerateChunk();
		}else{
			updateWorkingState(false);
			try{
				Thread.sleep(50);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
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
	private void updateWorkingState(boolean state){
		if(state==working)
			return;
		working = state;
		if(working)
			System.out.println("Generator is now working.");
		else
			System.out.println("Generator is now resting.");
	}
	void dispose(){
		running = false;
	}
	ChunkWorkerQue getQue(){
		return que;
	}
	void start(){
		t.start();
		t = null;
	}
}
