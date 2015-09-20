package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

class SpiralGridAlgorithm{
	private int x;
	private int y;
	private int dir;
	private int dis = 1;
	private int centerX;
	private int centerY;
	private int maxDistance;
	private int tempX;
	private int tempY;
	public int getY(){
		return tempY;
	}
	public boolean hasNext(){
		return !(dis==maxDistance+1&&dir==1);
	}
	public void next(){
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
	public void reset(){
		x = 0;
		y = 0;
		dir = 0;
		dis = 1;
	}
	public void setMaxDistance(int maxDistance){
		this.maxDistance = maxDistance;
	}
	public void setOrigin(int x, int y){
		centerX = x;
		centerY = y;
	}
	int getX(){
		return tempX;
	}
}
