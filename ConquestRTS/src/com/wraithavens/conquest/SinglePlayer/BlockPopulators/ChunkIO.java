package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

import java.io.File;
import java.util.ArrayList;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Utility.CompactBinaryFile;

public class ChunkIO{
	private static int indexOf(int x, int y, int z, int chunkSize){
		return x+y*chunkSize+z*chunkSize*chunkSize;
	}
	private byte[] blocks;
	private int x;
	private int y;
	private int z;
	private BorderBlocks borderBlocks;
	/**
	 * Gets the current block array from last load, or last block set. This
	 * array is instanced, not copied.
	 */
	public byte[] getBlocks(){
		return blocks;
	}
	/**
	 * Loads all blocks at this specified chunk location. Returns true if blocks
	 * could be read. False otherwise. If false, current block array will not
	 * change from it's previous state.
	 */
	public boolean loadBlocks(){
		String worldFolder =
			WraithavensConquest.saveFolder+File.separatorChar+WraithavensConquest.currentGameUUID;
		CompactBinaryFile file = new CompactBinaryFile(worldFolder, x+","+y+","+z+".blocks");
		if(!file.exists())
			return false;
		blocks = new byte[Chunk.TOTAL_BLOCKS];
		file.read();
		int x, y, z;
		for(x = 0; x<Chunk.BLOCKS_PER_CHUNK; x++)
			for(y = 0; y<Chunk.BLOCKS_PER_CHUNK; y++)
				for(z = 0; z<Chunk.BLOCKS_PER_CHUNK; z++)
					blocks[Chunk.getBlockIndex(x, y, z)] = (byte)file.getNumber(8);
		file.stopReading();
		return true;
	}
	/**
	 * Saves all current blocks to file at the specified chunk location.
	 */
	public void saveBlocks(){
		String worldFolder =
			WraithavensConquest.saveFolder+File.separatorChar+WraithavensConquest.currentGameUUID;
		CompactBinaryFile file = new CompactBinaryFile(worldFolder, x+","+y+","+z+".blocks");
		file.ensureExistance();
		file.write();
		int x, y, z;
		for(x = 0; x<Chunk.BLOCKS_PER_CHUNK; x++)
			for(y = 0; y<Chunk.BLOCKS_PER_CHUNK; y++)
				for(z = 0; z<Chunk.BLOCKS_PER_CHUNK; z++)
					file.addNumber(blocks[Chunk.getBlockIndex(x, y, z)], 8);
		file.stopWriting();
	}
	/**
	 * The same as calling: <br>
	 * <br>
	 * <code>saveBlocks();</code> <br>
	 * <code>saveMesh();</code>
	 */
	public void saveChunk(){
		saveBlocks();
		saveMesh();
	}
	/**
	 * Saves all LOD levels for the chunk's mesh.
	 */
	public void saveMesh(){
		int[][] a = new int[Chunk.BLOCKS_PER_CHUNK][Chunk.BLOCKS_PER_CHUNK];
		int[][] b = new int[Chunk.BLOCKS_PER_CHUNK][Chunk.BLOCKS_PER_CHUNK];
		boolean[][] c = new boolean[Chunk.BLOCKS_PER_CHUNK][Chunk.BLOCKS_PER_CHUNK];
		ChunkXQuadCounter xCounter = new ChunkXQuadCounter();
		ChunkYQuadCounter yCounter = new ChunkYQuadCounter();
		ChunkZQuadCounter zCounter = new ChunkZQuadCounter();
		BlockProperties d =
			new BlockProperties(Math.min(Chunk.BLOCKS_PER_CHUNK*Chunk.BLOCKS_PER_CHUNK, Block.values().length));
		for(int i = 0; i<=0; i++)
			saveLOD(i, a, b, c, d, xCounter, yCounter, zCounter);
	}
	/**
	 * Sets the current block set for saving. This is instanced, not copied.
	 */
	public void setBlocks(byte[] blocks){
		this.blocks = blocks;
	}
	/**
	 * Sets the current border blocks for this chunk. This is used for
	 * calculating occulision culling, and simplifiying the mesh for faster
	 * rendering.
	 */
	public void setBorderBlocks(BorderBlocks borderBlocks){
		this.borderBlocks = borderBlocks;
	}
	/**
	 * Sets the current target chunk location.
	 */
	public void setChunkLocation(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	private byte getAverage(int x, int y, int z, int blockSize){
		BlockProperties p = new BlockProperties(blockSize*blockSize*blockSize);
		int a, b, c;
		x *= blockSize;
		y *= blockSize;
		z *= blockSize;
		byte type;
		for(a = 0; a<blockSize; a++)
			for(b = 0; b<blockSize; b++)
				for(c = 0; c<blockSize; c++){
					type = blocks[Chunk.getBlockIndex(x+a, y+b, z+c)];
					if(!p.contains(type))
						p.place(type);
				}
		return p.getMostOccuring();
	}
	private boolean isSideShown(int x, int y, int z, int j, byte[] newBlocks, int blockSize, int chunkSize){
		if(j==0){
			x++;
			if(x==chunkSize){
				int u, v;
				for(u = 0; u<blockSize; u++)
					for(v = 0; v<blockSize; v++)
						if(!borderBlocks.getBlock(0, y*blockSize+u, z*blockSize+v, j))
							return true;
				return false;
			}
		}else if(j==1){
			x--;
			if(x==-1){
				int u, v;
				for(u = 0; u<blockSize; u++)
					for(v = 0; v<blockSize; v++)
						if(!borderBlocks.getBlock(0, y*blockSize+u, z*blockSize+v, j))
							return true;
				return false;
			}
		}else if(j==2){
			y++;
			if(y==chunkSize){
				int u, v;
				for(u = 0; u<blockSize; u++)
					for(v = 0; v<blockSize; v++)
						if(!borderBlocks.getBlock(x*blockSize+u, 0, z*blockSize+v, j))
							return true;
				return false;
			}
		}else if(j==3){
			y--;
			if(y==-1){
				int u, v;
				for(u = 0; u<blockSize; u++)
					for(v = 0; v<blockSize; v++)
						if(!borderBlocks.getBlock(x*blockSize+u, 0, z*blockSize+v, j))
							return true;
				return false;
			}
		}else if(j==4){
			z++;
			if(z==chunkSize){
				int u, v;
				for(u = 0; u<blockSize; u++)
					for(v = 0; v<blockSize; v++)
						if(!borderBlocks.getBlock(x*blockSize+u, y*blockSize+v, 0, j))
							return true;
				return false;
			}
		}else{
			z--;
			if(z==-1){
				int u, v;
				for(u = 0; u<blockSize; u++)
					for(v = 0; v<blockSize; v++)
						if(!borderBlocks.getBlock(x*blockSize+u, y*blockSize+v, 0, j))
							return true;
				return false;
			}
		}
		return newBlocks[indexOf(x, y, z, chunkSize)]==Block.AIR;
	}
	private void saveLOD(int level, int[][] a, int[][] b, boolean[][] c, BlockProperties d,
		ChunkXQuadCounter xCounter, ChunkYQuadCounter yCounter, ChunkZQuadCounter zCounter){
		ArrayList<Quad> quads = new ArrayList();
		QuadListener listener = new QuadListener(){
			public void addQuad(Quad q){
				quads.add(q);
			}
		};
		int chunkSize = (int)Math.pow(2, level);
		int blockSize = Chunk.BLOCKS_PER_CHUNK/chunkSize;
		byte[] newBlocks = new byte[chunkSize*chunkSize*chunkSize];
		int x, y, z, j, i, q, u, v;
		byte block;
		boolean shown;
		for(x = 0; x<chunkSize; x++)
			for(y = 0; y<chunkSize; y++)
				for(z = 0; z<chunkSize; z++)
					newBlocks[x+y*chunkSize+z*chunkSize*chunkSize] = getAverage(x, y, z, blockSize);
		for(j = 0; j<6; j++){
			if(j==0||j==1){
				for(x = 0; x<chunkSize; x++){
					d.reset();
					for(y = 0; y<chunkSize; y++)
						for(z = 0; z<chunkSize; z++){
							block = newBlocks[indexOf(x, y, z, chunkSize)];
							if(block==Block.AIR)
								continue;
							if(!d.contains(block))
								d.place(block);
						}
					for(i = 0; i<d.size(); i++){
						for(y = 0; y<chunkSize; y++)
							for(z = 0; z<chunkSize; z++){
								shown =
									newBlocks[indexOf(x, y, z, chunkSize)]==d.get(i)
									&&isSideShown(x, y, z, j, newBlocks, blockSize, chunkSize);
								for(u = 0; u<blockSize; u++)
									for(v = 0; v<blockSize; v++)
										c[y*blockSize+u][z*blockSize+v] = shown;
							}
						q =
							QuadOptimizer
								.optimize(a, b, c, Chunk.BLOCKS_PER_CHUNK, Chunk.BLOCKS_PER_CHUNK, true);
						if(q==0)
							continue;
						xCounter.setup(this.x<<Chunk.CHUNK_BITS, this.y<<Chunk.CHUNK_BITS,
							this.z<<Chunk.CHUNK_BITS, x, j, listener, Block.values()[d.get(i)+Block.ID_SHIFT]);
						QuadOptimizer.countQuads(xCounter, a, Chunk.BLOCKS_PER_CHUNK, Chunk.BLOCKS_PER_CHUNK, q);
					}
				}
			}else if(j==2||j==3){
				for(y = 0; y<chunkSize; x++){
					d.reset();
					for(x = 0; x<chunkSize; x++)
						for(z = 0; z<chunkSize; z++){
							block = newBlocks[indexOf(x, y, z, chunkSize)];
							if(block==Block.AIR)
								continue;
							if(!d.contains(block))
								d.place(block);
						}
					for(i = 0; i<d.size(); i++){
						for(x = 0; x<chunkSize; x++)
							for(z = 0; z<chunkSize; z++){
								shown =
									newBlocks[indexOf(x, y, z, chunkSize)]==d.get(i)
									&&isSideShown(x, y, z, j, newBlocks, blockSize, chunkSize);
								for(u = 0; u<blockSize; u++)
									for(v = 0; v<blockSize; v++)
										c[x*blockSize+u][z*blockSize+v] = shown;
							}
						q =
							QuadOptimizer
								.optimize(a, b, c, Chunk.BLOCKS_PER_CHUNK, Chunk.BLOCKS_PER_CHUNK, true);
						if(q==0)
							continue;
						yCounter.setup(this.x<<Chunk.CHUNK_BITS, this.y<<Chunk.CHUNK_BITS,
							this.z<<Chunk.CHUNK_BITS, y, j, listener, Block.values()[d.get(i)+Block.ID_SHIFT]);
						QuadOptimizer.countQuads(yCounter, a, Chunk.BLOCKS_PER_CHUNK, Chunk.BLOCKS_PER_CHUNK, q);
					}
				}
			}else{
				for(z = 0; z<chunkSize; z++){
					d.reset();
					for(x = 0; x<chunkSize; x++)
						for(y = 0; y<chunkSize; y++){
							block = newBlocks[indexOf(x, y, z, chunkSize)];
							if(block==Block.AIR)
								continue;
							if(!d.contains(block))
								d.place(block);
						}
					for(i = 0; i<d.size(); i++){
						for(x = 0; x<chunkSize; x++)
							for(y = 0; y<chunkSize; y++){
								shown =
									newBlocks[indexOf(x, y, z, chunkSize)]==d.get(i)
									&&isSideShown(x, y, z, j, newBlocks, blockSize, chunkSize);
								for(u = 0; u<blockSize; u++)
									for(v = 0; v<blockSize; v++)
										c[x*blockSize+u][y*blockSize+v] = shown;
							}
						q =
							QuadOptimizer
								.optimize(a, b, c, Chunk.BLOCKS_PER_CHUNK, Chunk.BLOCKS_PER_CHUNK, true);
						if(q==0)
							continue;
						zCounter.setup(this.x<<Chunk.CHUNK_BITS, this.y<<Chunk.CHUNK_BITS,
							this.z<<Chunk.CHUNK_BITS, z, j, listener, Block.values()[d.get(i)+Block.ID_SHIFT]);
						QuadOptimizer.countQuads(zCounter, a, Chunk.BLOCKS_PER_CHUNK, Chunk.BLOCKS_PER_CHUNK, q);
					}
				}
			}
		}
		String worldFolder =
			WraithavensConquest.saveFolder+File.separatorChar+WraithavensConquest.currentGameUUID;
		CompactBinaryFile file =
			new CompactBinaryFile(worldFolder, this.x+","+this.y+","+this.z+","+level+".lod");
		file.ensureExistance();
		file.write();
		file.addNumber(quads.size()*4, 16);
		file.addNumber(quads.size()*6, 16);
		file.prepareSpace(quads.size()*4*7+quads.size()*6);
		Quad l;
		for(i = 0; i<quads.size(); i++){
			l = quads.get(i);
			for(j = 0; j<4; j++){
				file.addFloat(l.data.get(j*3));
				file.addFloat(l.data.get(j*3+1));
				file.addFloat(l.data.get(j*3+2));
				file.addFloat(l.data.get(12));
				file.addFloat(l.data.get(13));
				file.addFloat(l.data.get(14));
				file.addFloat(l.side==2?1.0f:l.side==3?0.6f:0.8f);
			}
		}
		for(i = 0; i<quads.size(); i++){
			file.addNumber(i*4, 16);
			file.addNumber(i*4+1, 16);
			file.addNumber(i*4+2, 16);
			file.addNumber(i*4, 16);
			file.addNumber(i*4+2, 16);
			file.addNumber(i*4+3, 16);
		}
		file.stopWriting();
	}
}
