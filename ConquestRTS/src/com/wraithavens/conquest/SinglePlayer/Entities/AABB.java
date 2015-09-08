package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class AABB{
	private final float[] e = new float[6];
	public void calculate(Vector3f minEdge, Vector3f maxEdge, float scale, Vector3f offset){
		e[0] = maxEdge.x*scale+(offset==null?0:offset.x);
		e[1] = minEdge.x*scale+(offset==null?0:offset.x);
		e[2] = maxEdge.y*scale+(offset==null?0:offset.y);
		e[3] = minEdge.y*scale+(offset==null?0:offset.y);
		e[4] = maxEdge.z*scale+(offset==null?0:offset.z);
		e[5] = minEdge.z*scale+(offset==null?0:offset.z);
	}
	public float getX(){
		return e[1];
	}
	public float getY(){
		return e[3];
	}
	public float getZ(){
		return e[5];
	}
	public void set(float lowX, float lowY, float lowZ, float highX, float highY, float highZ){
		e[0] = highX;
		e[1] = lowX;
		e[2] = highY;
		e[3] = lowY;
		e[4] = highZ;
		e[5] = lowZ;
	}
	public boolean visible(Camera camera){
		return camera.getFrustum().boxInFrustum(e);
	}
}
