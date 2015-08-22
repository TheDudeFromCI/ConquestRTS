package com.wraithavens.conquest.SinglePlayer.Particles;

import com.wraithavens.conquest.Math.Vector2f;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public abstract class Particle{
	protected final Vector3f location = new Vector3f();
	protected final Vector2f scale = new Vector2f();
	private double camDistance;
	public Vector3f getLocation(){
		return location;
	}
	public Vector2f getScale(){
		return scale;
	}
	public abstract boolean isAlive();
	public abstract void update(double delta, double time);
	double getCameraDistance(){
		return camDistance;
	}
	void setCameraDistance(Camera camera){
		double x = camera.x-location.x;
		double y = camera.y-location.y;
		double z = camera.z-location.z;
		camDistance = x*x+y*y+z*z;
	}
}
