package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

class LodRadius{
	private final int lod;
	LodRadius(int r0){
		lod = r0*r0;
	}
	boolean canSee(Camera camera, Vector3f position){
		if(lod==0)
			return true;
		return Math.pow(camera.x-position.x, 2)+Math.pow(camera.y-position.y, 2)
			+Math.pow(camera.z-position.z, 2)<lod;
	}
}
