package com.wraithavens.conquest.SinglePlayer.Blocks;

public class SpiralGridAlgorithm{
	private int x;
	private int y;
	private int z;
	private int dir;
	private int dis = 16;
	private int centerX;
	private int centerY;
	private int centerZ;
	private int maxDistance;
	private int tempX;
	private int tempY;
	private int tempZ;
	public int getX(){
		return tempX;
	}
	public int getY(){
		return tempY;
	}
	public int getZ(){
		return tempZ;
	}
	public boolean hasNext(){
		return !(dis==maxDistance+16&&dir==1&&y<maxDistance);
	}
	public void next(){
		tempX = x+centerX;
		tempY = y+centerY;
		tempZ = z+centerZ;
		loadNextUnit();
	}
	public void reset(){
		x = 0;
		y = -maxDistance;
		z = 0;
		dir = 0;
		dis = 16;
	}
	public void setMaxDistance(int maxDistance){
		this.maxDistance = maxDistance;
	}
	public void setOrigin(int x, int y, int z){
		centerX = x;
		centerY = y;
		centerZ = z;
	}
	private void loadNextUnit(){
		if(y<maxDistance){
			y += 16;
			return;
		}else if(y==maxDistance)
			y = -maxDistance;
		else{
			y += 16;
			return;
		}
		if(dir==0){
			x += 16;
			if(x==dis)
				dir = 1;
		}else if(dir==1){
			z += 16;
			if(z==dis){
				dir = 2;
				dis = -dis;
			}
		}else if(dir==2){
			x -= 16;
			if(x==dis)
				dir = 3;
		}else{
			z -= 16;
			if(z==dis){
				dir = 0;
				dis = -dis+16;
			}
		}
	}
}
