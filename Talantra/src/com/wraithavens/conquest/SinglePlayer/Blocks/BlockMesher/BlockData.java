package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher;

import java.io.File;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Block;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

public class BlockData{
	static final byte Air = -1;
	private final byte[] blocks = new byte[32*32*32];
	private final BlockClipData clipData;
	private final MeshFormatter meshFormatter;
	public BlockData(MeshFormatter meshFormatter){
		this.meshFormatter = meshFormatter;
		clipData = new BlockClipData();
		clear();
	}
	public void clear(){
		fill(Air);
		clipData.clear();
	}
	public void fill(byte type){
		for(int i = 0; i<blocks.length; i++)
			blocks[i] = type;
	}
	public byte getBlock(int x, int y, int z){
		return blocks[x*32*32+y*32+z];
	}
	public void loadFromFile(int x, int y, int z) throws ChunkNotGeneratedException{
		File file = Algorithms.getChunkBlocksPath(x, y, z);
		if(file.exists()&&file.length()>0){
			BinaryFile bin = new BinaryFile(file);
			bin.decompress(false);
			bin.getBytes(blocks);
			bin.getBytes(clipData.getBytes());
		}else
			throw new ChunkNotGeneratedException(x, y, z);
	}
	/**
	 * Creates and saves the mesh for this chunk. If basic is enabled, then no
	 * optimization steps are taken for creating the mesh. Simply speed. If not
	 * basic, then a full optimazation is preformed. (This takes much longer,
	 * but with far, far, fewer quads.)
	 */
	public MeshRenderer mesh(@SuppressWarnings("unused") boolean basic){
		// TODO Fix basic meshing.
		// meshFormatter.setBasicState(basic);
		meshFormatter.setBasicState(false);
		int x, y, z, j, i;
		byte block;
		for(j = 0; j<6; j++){
			if(j==0||j==1){
				for(x = 0; x<32; x++){
					meshFormatter.clearTypeList();
					for(y = 0; y<32; y++)
						for(z = 0; z<32; z++)
							if((block = getBlock(x, y, z))!=Air)
								meshFormatter.addBlockType(block);
					for(i = 0; i<meshFormatter.typeListSize(); i++){
						block = meshFormatter.getBlockType(i);
						for(y = 0; y<32; y++)
							for(z = 0; z<32; z++)
								meshFormatter.setBlock(y, z, placeSide(x, y, z, j, block));
						meshFormatter.setCurrentTexutre(Block.values()[block&0xFF].getTexture(j));
						meshFormatter.compileLayer(j, x);
					}
				}
			}else if(j==2||j==3){
				for(y = 0; y<32; y++){
					meshFormatter.clearTypeList();
					for(x = 0; x<32; x++)
						for(z = 0; z<32; z++)
							if((block = getBlock(x, y, z))!=Air)
								meshFormatter.addBlockType(block);
					for(i = 0; i<meshFormatter.typeListSize(); i++){
						block = meshFormatter.getBlockType(i);
						for(x = 0; x<32; x++)
							for(z = 0; z<32; z++)
								meshFormatter.setBlock(x, z, placeSide(x, y, z, j, block));
						meshFormatter.setCurrentTexutre(Block.values()[block&0xFF].getTexture(j));
						meshFormatter.compileLayer(j, y);
					}
				}
			}else{
				for(z = 0; z<32; z++){
					meshFormatter.clearTypeList();
					for(x = 0; x<32; x++)
						for(y = 0; y<32; y++)
							if((block = getBlock(x, y, z))!=Air)
								meshFormatter.addBlockType(block);
					for(i = 0; i<meshFormatter.typeListSize(); i++){
						block = meshFormatter.getBlockType(i);
						for(x = 0; x<32; x++)
							for(y = 0; y<32; y++)
								meshFormatter.setBlock(x, y, placeSide(x, y, z, j, block));
						meshFormatter.setCurrentTexutre(Block.values()[block&0xFF].getTexture(j));
						meshFormatter.compileLayer(j, z);
					}
				}
			}
		}
		return meshFormatter.extract();
	}
	public void saveToFile(int x, int y, int z){
		BinaryFile bin = new BinaryFile(blocks.length+clipData.getBytes().length);
		bin.addBytes(blocks, 0, blocks.length);
		bin.addBytes(clipData.getBytes(), 0, clipData.getBytes().length);
		bin.compress(false);
		bin.compile(Algorithms.getChunkBlocksPath(x, y, z));
	}
	public void setBlock(int x, int y, int z, byte b){
		if(x>=0&&x<32&&y>=0&&y<32&&z>=0&&z<32)
			blocks[x*32*32+y*32+z] = b;
		else
			clipData.setHasBlockWeak(x, y, z, b);
	}
	private BlockClipData getClipData(){
		return clipData;
	}
	private byte hasAdvancedBlock(int x, int y, int z, int j){
		switch(j){
			case 0:
				x++;
				break;
			case 1:
				x--;
				break;
			case 2:
				y++;
				break;
			case 3:
				y--;
				break;
			case 4:
				z++;
				break;
			case 5:
				z--;
				break;
		}
		if(x>=0&&x<32&&y>=0&&y<32&&z>=0&&z<32)
			return getBlock(x, y, z);
		return getClipData().hasBlock(x, y, z);
	}
	private byte placeSide(int x, int y, int z, int j, byte type){
		byte to = hasAdvancedBlock(x, y, z, j);
		byte at = getBlock(x, y, z);
		boolean toTrans = to==Air?false:Block.values()[to&0xFF].isTranspartent();
		boolean fromTrans = at==Air?false:Block.values()[at&0xFF].isTranspartent();
		if(at==type&&toTrans&&!fromTrans)
			return 1;
		if(toTrans)
			return -1;
		if(to!=Air)
			return 0;
		if(at==type)
			return 1;
		return -1;
	}
}
