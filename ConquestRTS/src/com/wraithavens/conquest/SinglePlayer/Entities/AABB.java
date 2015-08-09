package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class AABB{
	private float x;
	private float y;
	private float z;
	private float size;
	public void calculate(float x, float y, float z, float size){
		this.x = x-size/2;
		this.y = y-size/2;
		this.z = z-size/2;
		this.size = size;
	}
	public boolean visible(Camera camera){
		return camera.getFrustum().cubeInFrustum(x, y, z, size);
	}
	float getCenterX(){
		return x+size/2;
	}
	float getCenterY(){
		return y+size/2;
	}
	float getCenterZ(){
		return z+size/2;
	}
	float getSize(){
		return size;
	}
}
