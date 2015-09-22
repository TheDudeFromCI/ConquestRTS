package com.wraithavens.conquest.SinglePlayer.Entities.Grass;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class GrassPatch{
	private final ArrayList<GrassTransform> grassBlades;
	private final EntityType grassType;
	private final int x;
	private final int z;
	private final float[] view = new float[6];
	private final float centerX;
	private final float centerY;
	private final float centerZ;
	private boolean inView;
	public GrassPatch(EntityType grassType, ArrayList<GrassTransform> grassBlades, int x, int z){
		this.grassType = grassType;
		this.grassBlades = grassBlades;
		this.x = x;
		this.z = z;
		view[0] = Float.MIN_VALUE;
		view[1] = Float.MAX_VALUE;
		view[2] = Float.MIN_VALUE;
		view[3] = Float.MAX_VALUE;
		view[4] = Float.MIN_VALUE;
		view[5] = Float.MAX_VALUE;
		for(GrassTransform t : grassBlades){
			if(t.getX()>view[0])
				view[0] = t.getX();
			if(t.getX()<view[1])
				view[1] = t.getX();
			if(t.getY()>view[2])
				view[2] = t.getY();
			if(t.getY()<view[3])
				view[3] = t.getY();
			if(t.getZ()>view[4])
				view[4] = t.getZ();
			if(t.getZ()<view[5])
				view[5] = t.getZ();
		}
		centerX = (view[0]+view[1])/2;
		centerY = (view[2]+view[3])/2;
		centerZ = (view[4]+view[5])/2;
	}
	void calculateView(LandscapeWorld landscape, Camera camera){
		inView =
			camera.distanceSquared(centerX, centerY, centerZ)<250*250&&landscape.isWithinView(x, z)
			&&camera.boxInView(view);
	}
	int getCount(){
		return grassBlades.size();
	}
	EntityType getType(){
		return grassType;
	}
	boolean inView(){
		return inView;
	}
	void store(FloatBuffer data){
		for(GrassTransform t : grassBlades){
			data.put(t.getX());
			data.put(t.getY());
			data.put(t.getZ());
			data.put(t.getRotation());
			data.put(t.getScale());
		}
	}
}
