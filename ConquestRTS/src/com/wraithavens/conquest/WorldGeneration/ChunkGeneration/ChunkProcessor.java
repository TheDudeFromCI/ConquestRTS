package com.wraithavens.conquest.WorldGeneration.ChunkGeneration;

import com.wraithavens.conquest.WorldGeneration.World;

public class ChunkProcessor{
	private static final int LoadingLayers = 1;
	private final World world;
	private int loadRange;
	private int viewRange;
	private int viewLength;
	private ChunkStub[] stubs;
	private int originX;
	private int originZ;
	private int posX;
	private int posZ;
	private int pass;
	public ChunkProcessor(World world){
		this.world = world;
	}
	public int getOriginX(){
		return originX;
	}
	public int getOriginZ(){
		return originZ;
	}
	public void setLoadRange(int loadRange, int originX, int originZ){
		int previousX = this.originX;
		int previousZ = this.originZ;
		int previousRange = this.loadRange;
		this.loadRange = loadRange;
		this.originX = originX;
		this.originZ = originZ;
		viewRange = loadRange+LoadingLayers-1;
		viewLength = viewRange*2+1;
		posX = -viewRange;
		posZ = -viewRange;
		pass = 0;
		stubs = buildNewStubs(previousX, previousZ, previousRange);
	}
	public void update(){
		if(pass==LoadingLayers)
			return;
		while(update2());
	}
	private ChunkStub[] buildNewStubs(int previousX, int previousZ, int previousRange){
		ChunkStub[] s = new ChunkStub[viewLength*viewLength];
		if(stubs==null){
			int x, z, tempX, tempZ, tempZIndex;
			for(z = -viewRange; z<=viewRange; z++){
				tempZ = z*64+originZ;
				tempZIndex = z*viewLength;
				for(x = -viewRange; x<=viewRange; x++){
					tempX = x*64+originX;
					s[tempZIndex+x] =
						new ChunkStub(world, tempX, tempZ, world.getWorldFiles().getChunkStack(tempX, tempZ));
				}
			}
		}else{
			int x, z, tempX, tempZ, a, b, tempZIndex;
			int previousLength = previousRange*2+1;
			for(z = -viewRange; z<=viewRange; z++){
				tempZ = z*64+originZ;
				tempZIndex = z*viewLength;
				for(x = -viewRange; x<=viewRange; x++){
					tempX = x*64+originX;
					a = (tempX-previousX)/64;
					b = (tempZ-previousZ)/64;
					if(Math.abs(a)>previousRange||Math.abs(b)>previousRange)
						s[tempZIndex+x] =
							new ChunkStub(world, tempX, tempZ, world.getWorldFiles().getChunkStack(tempX, tempZ));
					else
						s[tempZIndex+x] = stubs[a*previousLength+b];
				}
			}
		}
		return s;
	}
	private boolean update2(){
		int index = posZ*viewLength+posX;
		int pass = this.pass;
		int x = posX;
		int z = posZ;
		posX++;
		if(posX>viewRange){
			posZ++;
			if(posZ>viewRange){
				this.pass++;
				viewRange--;
				posZ = -viewRange;
			}
			posX = -viewRange;
		}
		if(stubs[index].getLayer()>=pass)
			return true;
		stubs[index].update(stubs, x, z, viewLength);
		return false;
	}
}
