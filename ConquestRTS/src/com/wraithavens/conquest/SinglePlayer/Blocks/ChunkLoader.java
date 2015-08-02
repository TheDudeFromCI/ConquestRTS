package com.wraithavens.conquest.SinglePlayer.Blocks;

import java.io.File;
import java.util.ArrayList;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Utility.CompactBinaryFile;

public class ChunkLoader{
	private static RawChunk load(File file, int x, int y, int z){
		RawChunk rawChunk = new RawChunk(x, y, z);
		CompactBinaryFile f = new CompactBinaryFile(file.getAbsolutePath());
		f.read();
		int a, b, c;
		for(a = 0; a<16; a++)
			for(b = 0; b<16; b++)
				for(c = 0; c<16; c++)
					rawChunk.setBlock(a, b, c, (byte)f.getNumber(8));
		f.stopReading();
		return rawChunk;
	}
	private static void save(File file, RawChunk raw){
		CompactBinaryFile f = new CompactBinaryFile(file.getAbsolutePath());
		f.ensureExistance();
		f.write();
		int a, b, c;
		for(a = 0; a<16; a++)
			for(b = 0; b<16; b++)
				for(c = 0; c<16; c++)
					f.addNumber(raw.getBlock(a, b, c), 8);
		f.stopWriting();
	}
	private final SpiralGridAlgorithm spiral;
	private final ChunkGenerator generator;
	private int x;
	private int y;
	private int z;
	public ChunkLoader(ChunkGenerator generator){
		this.generator = generator;
		spiral = new SpiralGridAlgorithm();
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getZ(){
		return z;
	}
	public RawChunk loadNextChunk(ArrayList<ChunkPainter> chunks){
		// ---
		// Don't load anything if we've reached the end of our view distance.
		// ---
		RawChunk raw;
		while(spiral.hasNext()){
			spiral.next();
			raw = load(chunks);
			if(raw!=null)
				return raw;
		}
		return null;
	}
	public void setViewDistance(int distance){
		spiral.setMaxDistance(distance);
		spiral.reset();
	}
	public void updateLocation(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		spiral.setOrigin(x, y, z);
		spiral.reset();
	}
	private RawChunk load(ArrayList<ChunkPainter> chunks){
		int x = spiral.getX();
		int y = spiral.getY();
		int z = spiral.getZ();
		for(ChunkPainter painter : chunks)
			if(painter.x==x&&painter.y==y&&painter.z==z)
				return null;
		File file = new File(WraithavensConquest.saveFolder+File.separatorChar+"Chunks", x+","+y+","+z+".dat");
		if(file.exists())
			return load(file, x, y, z);
		RawChunk raw = generator.generateRawChunk(x, y, z);
		save(file, raw);
		return raw;
	}
}
