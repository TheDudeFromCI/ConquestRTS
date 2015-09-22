package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

class LodRadius{
	private final int lod0;
	private final int lod1;
	private final int lod2;
	private final int lod3;
	private final int lod4;
	private final int lod5;
	LodRadius(int r0, int r1, int r2, int r3, int r4, int r5){
		lod0 = r0*r0+1000*1000;
		lod1 = r1*r1;
		lod2 = r2*r2;
		lod3 = r3*r3;
		lod4 = r4*r4;
		lod5 = r5*r5;
	}
	int getLod(Camera camera, Vector3f position){
		double d =
			Math.pow(camera.x-position.x, 2)+Math.pow(camera.y-position.y, 2)+Math.pow(camera.z-position.z, 2);
		if(d<lod0)
			return 0;
		if(d<lod1)
			return 1;
		if(d<lod2)
			return 2;
		if(d<lod3)
			return 3;
		if(d<lod4)
			return 4;
		if(d<lod5)
			return 5;
		return -1;
	}
}
