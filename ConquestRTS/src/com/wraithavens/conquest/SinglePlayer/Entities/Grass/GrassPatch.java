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
	private final float centerZ;
	private boolean inView;
	public GrassPatch(EntityType grassType, ArrayList<GrassTransform> grassBlades, int x, int z){
		this.grassType = grassType;
		this.grassBlades = grassBlades;
		this.x = x;
		this.z = z;
		view[0] = x+64;
		view[1] = x;
		view[2] = Integer.MAX_VALUE;
		view[3] = Integer.MIN_VALUE;
		view[4] = z+64;
		view[5] = z;
		centerX = x+32;
		centerZ = z+32;
	}
	void calculateView(LandscapeWorld landscape, Camera camera){
		// TODO
		inView =
			camera.distanceSquared(centerX, camera.y, centerZ)<250*250&&landscape.isWithinView(x, z)
			&&camera.boxInView(view);
		// inView = landscape.isWithinView(x, z)&&camera.boxInView(view);
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
