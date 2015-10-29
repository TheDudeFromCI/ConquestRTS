package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

class AABB{
	private final float[] e = new float[6];
	void calculate(Vector3f minEdge, Vector3f maxEdge, float scale, Vector3f offset){
		e[0] = maxEdge.x*scale+(offset==null?0:offset.x);
		e[1] = minEdge.x*scale+(offset==null?0:offset.x);
		e[2] = maxEdge.y*scale+(offset==null?0:offset.y);
		e[3] = minEdge.y*scale+(offset==null?0:offset.y);
		e[4] = maxEdge.z*scale+(offset==null?0:offset.z);
		e[5] = minEdge.z*scale+(offset==null?0:offset.z);
	}
	boolean visible(Camera camera){
		return camera.boxInView(e);
	}
}
