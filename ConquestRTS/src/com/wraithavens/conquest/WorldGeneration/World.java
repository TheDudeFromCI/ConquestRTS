package com.wraithavens.conquest.WorldGeneration;

import java.util.Random;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.WorldGeneration.BiomeHandle.BiomePainter;
import com.wraithavens.conquest.WorldGeneration.ChunkGeneration.ChunkProcessor;

public class World{
	private static long generateSeed(String s){
		if(s.isEmpty())
			return 0;
		Random r = new Random(s.charAt(0));
		if(s.length()==1)
			return r.nextLong();
		long l = 4294967291L;
		for(int i = 1; i<s.length(); i++)
			l *= r.nextLong()+s.charAt(i);
		return l;
	}
	private static long[] generateSeeds(String seed, int count){
		if(seed.length()<count){
			long l = 4294967291L;
			Random r = new Random(seed.charAt(0));
			for(int i = 1; i<seed.length(); i++)
				l *= r.nextLong()+seed.charAt(i);
			r.setSeed(l);
			long[] seeds = new long[count];
			for(int i = 0; i<count; i++)
				seeds[i] = r.nextLong();
			return seeds;
		}
		long[] seeds = new long[count];
		int charsToEach = seed.length()/count;
		int charsLeftOffset = charsToEach*count;
		for(int i = 0; i<count; i++)
			seeds[i] = generateSeed(seed.substring(charsToEach*i, charsToEach)+seed.substring(charsLeftOffset));
		return seeds;
	}
	private static WorldGeneratorProperties getDefaultProperties(String seed, Camera camera){
		long[] s;
		if(seed==null||seed.isEmpty())
			s = randomSeeds(3);
		else
			s = generateSeeds(seed, 3);
		return new WorldGeneratorProperties(Runtime.getRuntime().availableProcessors()>1, s, 5, camera);
	}
	private static long[] randomSeeds(int count){
		long[] seeds = new long[count];
		for(int i = 0; i<count; i++){
			seeds[i] = (long)(Math.random()*Long.MAX_VALUE);
			if(Math.random()>0.5)
				seeds[i] *= -1;
		}
		return seeds;
	}
	private static Object ThreadLock = null;
	private final long[] seeds;
	private final Builder builder;
	private final WorldFiles worldFiles;
	private final ChunkProcessor chunkProcessor;
	private final BiomePainter biomePainter;
	private final boolean safelySupportsDuelCore;
	private final Camera camera;
	private boolean duelCoreLoading;
	private int loadRange;
	public World(String seed, Camera camera){
		this(getDefaultProperties(seed, camera));
	}
	public World(WorldGeneratorProperties props){
		// ---
		// Calculate system capabilities.
		// ---
		safelySupportsDuelCore = Runtime.getRuntime().availableProcessors()>1;
		// ---
		// Build generators.
		// ---
		builder = new Builder(this);
		worldFiles = new WorldFiles();
		chunkProcessor = new ChunkProcessor(this);
		biomePainter = new BiomePainter();
		{
			// ---
			// Load generator settings.
			// ---
			seeds = props.getSeeds();
			camera = props.getCamera();
			setDuelCoreLoading(props.hasDuelCoreLoading());
			biomePainter.setSeeds(seeds[0], seeds[1], seeds[2]);
		}
	}
	public BiomePainter getBiomePainter(){
		return biomePainter;
	}
	public Builder getBuilder(){
		return builder;
	}
	public ChunkProcessor getChunkProcessor(){
		return chunkProcessor;
	}
	public int getLoadRange(){
		return loadRange;
	}
	public long[] getSeeds(){
		return seeds;
	}
	public WorldFiles getWorldFiles(){
		return worldFiles;
	}
	public boolean isDuelCoreLoading(){
		return duelCoreLoading;
	}
	public boolean safelySupportsDuelCore(){
		return safelySupportsDuelCore;
	}
	public void setDuelCoreLoading(boolean multi){
		if(duelCoreLoading==multi)
			return;
		duelCoreLoading = multi;
		if(multi)
			builder.newThread();
	}
	public void setLoadRange(int range){
		if(loadRange==range)
			return;
		loadRange = range;
		chunkProcessor.setLoadRange(range, Algorithms.groupLocation((int)camera.x, 64),
			Algorithms.groupLocation((int)camera.z, 64));
	}
	public void update(){
		synchronized(ThreadLock){
			int chunkX = Algorithms.groupLocation((int)camera.x, 64);
			int chunkZ = Algorithms.groupLocation((int)camera.z, 64);
			if(chunkX!=chunkProcessor.getOriginX()||chunkZ!=chunkProcessor.getOriginZ())
				chunkProcessor.setLoadRange(loadRange, chunkX, chunkZ);
			else if(chunkProcessor.isWorking())
				chunkProcessor.update();
		}
	}
}
