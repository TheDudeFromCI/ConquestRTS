package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.Utility.WireframeCube;

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
	public void calculate(Vector3f minEdge, Vector3f maxEdge){
		x = minEdge.x;
		y = minEdge.y;
		z = minEdge.z;
		size = Math.max(Math.max(maxEdge.x-minEdge.x, maxEdge.y-minEdge.y), maxEdge.z-minEdge.z);
	}
	public void draw(){
		WireframeCube cube = WireframeCube.intance();
		cube.setColor(1, 0, 0);
		cube.setPosition(x, y, z);
		cube.setScale(size, size, size);
		cube.push();
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
