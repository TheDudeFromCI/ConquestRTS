package com.wraithavens.conquest.SinglePlayer.RenderHelpers;

import com.wraithavens.conquest.Math.Vec3i;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Block;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.BlockData;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkNotGeneratedException;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.MeshFormatter;
import com.wraithavens.conquest.Utility.Algorithms;

class CameraTargetBlock{
	private final Camera camera;
	private final Plotter plotter;
	private final Vec3i v;
	private final Vec3i currentBlock = new Vec3i();
	private final Vec3i currentChunk = new Vec3i();
	private final Vec3i tempChunk = new Vec3i();
	private final BlockData blockData;
	private final CameraTargetBlockCallback callback;
	CameraTargetBlock(Camera camera){
		this.camera = camera;
		plotter = new Plotter(0, 0, 0, 1, 1, 1);
		v = plotter.get();
		blockData = new BlockData(new MeshFormatter());
		callback = new CameraTargetBlockCallback();
		callback.blockData = blockData;
		loadCurrentChunk();
	}
	private void loadCurrentChunk(){
		try{
			blockData.loadFromFile(currentChunk.x, currentChunk.y, currentChunk.z);
		}catch(ChunkNotGeneratedException e){
			blockData.clear();
		}
	}
	CameraTargetBlockCallback getTargetBlock(int range){
		plotter.plot(camera.getPosition(), camera.getDirection(), range);
		boolean firstChunk = true;
		byte block;
		while(plotter.next()){
			tempChunk.x = Algorithms.groupLocation(v.x, 64);
			tempChunk.y = Algorithms.groupLocation(v.y, 64);
			tempChunk.z = Algorithms.groupLocation(v.z, 64);
			if(firstChunk||currentChunk.x!=tempChunk.x||currentChunk.y!=tempChunk.y||currentChunk.z!=tempChunk.z){
				firstChunk = false;
				currentChunk.set(tempChunk);
				loadCurrentChunk();
			}
			currentBlock.set(v);
			currentBlock.sub(currentChunk);
			block = blockData.getBlock(currentBlock.x, currentBlock.y, currentBlock.z);
			if(block!=-1){
				callback.x = v.x;
				callback.y = v.y;
				callback.z = v.z;
				callback.block = Block.values()[block&0xFF];
				callback.side = plotter.getSideHit();
				return callback;
			}
		}
		callback.x = v.x;
		callback.y = v.y;
		callback.z = v.z;
		callback.block = null;
		callback.side = -1;
		return callback;
	}
}
