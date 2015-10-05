package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher;

import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Block;

public class BlockData{
	private static final byte Air = -1;
	private final byte[] blocks = new byte[64*64*64];
	private final BlockClipData clipData;
	private final MeshFormatter meshFormatter;
	public BlockData(MeshFormatter meshFormatter){
		this.meshFormatter = meshFormatter;
		clipData = new BlockClipData();
		clear();
	}
	/**
	 * Creates and saves the mesh for this chunk. If basic is enabled, then no
	 * optimization steps are taken for creating the mesh. Simply speed. If not
	 * basic, then a full optimazation is preformed. (This takes much longer,
	 * but with far, far, fewer quads.)
	 */
	public MeshRenderer mesh(boolean basic){
		meshFormatter.setBasicState(basic);
		int x, y, z, j, i;
		byte block;
		for(j = 0; j<6; j++){
			if(j==0||j==1){
				for(x = 0; x<64; x++){
					meshFormatter.clearTypeList();
					for(y = 0; y<64; y++)
						for(z = 0; z<64; z++)
							if((block = getBlock(x, y, z))!=Air)
								meshFormatter.addBlockType(block);
					for(i = 0; i<meshFormatter.typeListSize(); i++){
						block = meshFormatter.getBlockType(i);
						for(y = 0; y<64; y++)
							for(z = 0; z<64; z++)
								meshFormatter.setBlock(y, z, placeSide(x, y, z, j, block));
						meshFormatter.setCurrentTexutre(Block.values()[block].getTexture(j));
						meshFormatter.compileLayer(j, x);
					}
				}
			}else if(j==2||j==3){
				for(y = 0; y<64; y++){
					meshFormatter.clearTypeList();
					for(x = 0; x<64; x++)
						for(z = 0; z<64; z++)
							if((block = getBlock(x, y, z))!=Air)
								meshFormatter.addBlockType(block);
					for(i = 0; i<meshFormatter.typeListSize(); i++){
						block = meshFormatter.getBlockType(i);
						for(x = 0; x<64; x++)
							for(z = 0; z<64; z++)
								meshFormatter.setBlock(x, z, placeSide(x, y, z, j, block));
						meshFormatter.setCurrentTexutre(Block.values()[block].getTexture(j));
						meshFormatter.compileLayer(j, y);
					}
				}
			}else{
				for(z = 0; z<64; z++){
					meshFormatter.clearTypeList();
					for(x = 0; x<64; x++)
						for(y = 0; y<64; y++)
							if((block = getBlock(x, y, z))!=Air)
								meshFormatter.addBlockType(block);
					for(i = 0; i<meshFormatter.typeListSize(); i++){
						block = meshFormatter.getBlockType(i);
						for(x = 0; x<64; x++)
							for(y = 0; y<64; y++)
								meshFormatter.setBlock(x, y, placeSide(x, y, z, j, block));
						meshFormatter.setCurrentTexutre(Block.values()[block].getTexture(j));
						meshFormatter.compileLayer(j, z);
					}
				}
			}
		}
		return meshFormatter.extract();
	}
	public void setBlock(int x, int y, int z, byte b){
		if(x>=0&&x<64&&y>=0&&y<64&&z>=0&&z<64)
			blocks[x*64*64+y*64+z] = b;
		else
			clipData.setHasBlockWeak(x, y, z, b!=Air);
	}
	private void clear(){
		for(int i = 0; i<blocks.length; i++)
			blocks[i] = Air;
		clipData.clear();
	}
	private byte getBlock(int x, int y, int z){
		return blocks[x*64*64+y*64+z];
	}
	private BlockClipData getClipData(){
		return clipData;
	}
	private boolean hasAdvancedBlock(int x, int y, int z, int j){
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
		if(x>=0&&x<64&&y>=0&&y<64&&z>=0&&z<64)
			return getBlock(x, y, z)!=Air;
		return getClipData().hasBlock(x, y, z);
	}
	private byte placeSide(int x, int y, int z, int j, byte type){
		/*
		 * From has 3 possible states. Air, required block, or non-required
		 * block. To has 2 possible states. Air, or solid block. This means we
		 * have 6 conditions to check for.
		 */
		// byte from = getBlock(x, y, z);
		// boolean to = hasAdvancedBlock(x, y, z, j);
		// if(to){
		// if(from==type)
		// return 0;
		// else if(from==Air)
		// return 0;
		// else
		// return 0;
		// }
		// if(from==type)
		// return 1;
		// else if(from==Air)
		// return -1;
		// else
		// return -1;
		if(hasAdvancedBlock(x, y, z, j))
			return 0;
		if(getBlock(x, y, z)==type)
			return 1;
		return -1;
	}
}
