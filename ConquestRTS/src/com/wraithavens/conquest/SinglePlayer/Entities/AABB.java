package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class AABB{
	private float x;
	private float y;
	private float z;
	private float size;
	public void calculate(Vector3f minEdge, Vector3f maxEdge, float scale, Vector3f offset){
		x = minEdge.x*scale+(offset==null?0:offset.x);
		y = minEdge.y*scale+(offset==null?0:offset.y);
		z = minEdge.z*scale+(offset==null?0:offset.z);
		size =
			Math.max(Math.max(maxEdge.x*scale-minEdge.x*scale, maxEdge.y*scale-minEdge.y*scale), maxEdge.z*scale
				-minEdge.z*scale);
	}
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public float getZ(){
		return z;
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
