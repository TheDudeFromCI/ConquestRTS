package com.wraithavens.conquest.SinglePlayer.Blocks;

import java.io.File;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Blocks.Octree.Octree;
import com.wraithavens.conquest.Utility.BinaryFile;

class ChunkLoader{
	private static RawChunk load(File file, int x, int y, int z){
		RawChunk rawChunk = new RawChunk(x, y, z);
		BinaryFile bin = new BinaryFile(file);
		int a, b, c;
		for(a = 0; a<16; a++)
			for(b = 0; b<16; b++)
				for(c = 0; c<16; c++)
					rawChunk.setBlock(a, b, c, bin.getByte());
		return rawChunk;
	}
	private static void save(File file, RawChunk raw){
		BinaryFile bin = new BinaryFile(4096);
		int a, b, c;
		for(a = 0; a<16; a++)
			for(b = 0; b<16; b++)
				for(c = 0; c<16; c++)
					bin.addByte(raw.getBlock(a, b, c));
		bin.compile(file);
	}
	private final CellSorter cellSorter;
	private final ChunkGenerator generator;
	private int x;
	private int y;
	private int z;
	ChunkLoader(ChunkGenerator generator, int maxDistance){
		this.generator = generator;
		cellSorter = new CellSorter(maxDistance/16);
	}
	public int getY(){
		return y;
	}
	public int getZ(){
		return z;
	}
	public RawChunk loadNextChunk(Octree chunks){
		// ---
		// Don't load anything if we've reached the end of our view distance.
		// ---
		RawChunk raw;
		while(cellSorter.hasNext()){
			cellSorter.next();
			raw = load(chunks);
			if(raw!=null)
				return raw;
		}
		return null;
	}
	public void updateLocation(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		cellSorter.reset();
	}
	private RawChunk load(Octree chunks){
		int x = cellSorter.getX()+this.x;
		int y = cellSorter.getY()+this.y;
		int z = cellSorter.getZ()+this.z;
		if(chunks.containsChunk(x, y, z))
			return null;
		File file = new File(WraithavensConquest.saveFolder+File.separatorChar+"Chunks", x+","+y+","+z+".dat");
		if(file.exists()&&file.length()>0)
			return load(file, x, y, z);
		RawChunk raw = generator.generateRawChunk(x, y, z);
		save(file, raw);
		return raw;
	}
	int getX(){
		return x;
	}
}
