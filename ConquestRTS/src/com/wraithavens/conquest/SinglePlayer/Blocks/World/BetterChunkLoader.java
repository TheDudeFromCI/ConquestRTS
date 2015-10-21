package com.wraithavens.conquest.SinglePlayer.Blocks.World;

public class BetterChunkLoader{
	private int x;
	private int y;
	private int dir;
	private int dis = 1;
	private int centerX;
	private int centerY;
	private int maxDistance;
	private int tempX;
	private int tempY;
	public int getX(){
		return tempX;
	}
	public int getY(){
		return tempY;
	}
	public boolean hasNext(){
		return !(dis==maxDistance+1&&dir==1);
	}
	public void next(){
		// TODO Load in a circle shape, not square.
		tempX = x+centerX;
		tempY = y+centerY;
		if(dir==0){
			x++;
			if(x==dis)
				dir = 1;
		}else if(dir==1){
			y++;
			if(y==dis){
				dir = 2;
				dis = -dis;
			}
		}else if(dir==2){
			x--;
			if(x==dis)
				dir = 3;
		}else{
			y--;
			if(y==dis){
				dir = 0;
				dis = -dis+1;
			}
		}
	}
	public void setMaxDistance(int maxDistance){
		this.maxDistance = maxDistance;
	}
	void reset(){
		x = 0;
		y = 0;
		dir = 0;
		dis = 1;
	}
	void setOrigin(int x, int y){
		centerX = x;
		centerY = y;
	}
}
