package com.wraithavens.conquest.SinglePlayer.Blocks;

import java.io.File;
import java.util.ArrayList;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Utility.CompactBinaryFile;

class ChunkLoader{
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
	public RawChunk loadNextChunk(ArrayList<VoxelChunk> chunks){
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
	private boolean containsVoxel(ArrayList<VoxelChunk> chunks, int x, int y, int z){
		for(int i = 0; i<chunks.size(); i++)
			if(containsVoxel(chunks.get(i), x, y, z))
				return true;
		return false;
	}
	private boolean containsVoxel(VoxelChunk voxel, int x, int y, int z){
		if(x<voxel.x)
			return false;
		if(y<voxel.y)
			return false;
		if(z<voxel.z)
			return false;
		if(x>=voxel.x+voxel.size)
			return false;
		if(y>=voxel.y+voxel.size)
			return false;
		if(z>=voxel.z+voxel.size)
			return false;
		if(x==voxel.x&&y==voxel.y&&z==voxel.z)
			return true;
		if(!voxel.hasChildrenVoxels())
			return false;
		for(int i = 0; i<8; i++)
			if(containsVoxel(voxel.getChild(i), x, y, z))
				return true;
		return false;
	}
	private RawChunk load(ArrayList<VoxelChunk> chunks){
		int x = cellSorter.getX()+this.x;
		int y = cellSorter.getY()+this.y;
		int z = cellSorter.getZ()+this.z;
		if(containsVoxel(chunks, x, y, z))
			return null;
		File file = new File(WraithavensConquest.saveFolder+File.separatorChar+"Chunks", x+","+y+","+z+".dat");
		if(file.exists())
			return load(file, x, y, z);
		RawChunk raw = generator.generateRawChunk(x, y, z);
		save(file, raw);
		return raw;
	}
	int getX(){
		return x;
	}
}
