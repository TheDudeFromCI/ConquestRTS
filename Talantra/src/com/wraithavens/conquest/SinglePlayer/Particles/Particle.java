package com.wraithavens.conquest.SinglePlayer.Particles;

import com.wraithavens.conquest.Math.Vector2f;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.Math.Vector4f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public abstract class Particle{
	protected final Vector3f location = new Vector3f();
	protected final Vector2f scale = new Vector2f();
	protected final Vector4f color = new Vector4f();
	private double camDistance;
	public abstract boolean isAlive();
	public abstract void update(double delta, double time);
	final double getCameraDistance(){
		return camDistance;
	}
	final Vector4f getColor(){
		return color;
	}
	final Vector3f getLocation(){
		return location;
	}
	final Vector2f getScale(){
		return scale;
	}
	final void setCameraDistance(Camera camera){
		double x = camera.getX()-location.x;
		double y = camera.getY()-location.y;
		double z = camera.getZ()-location.z;
		camDistance = x*x+y*y+z*z;
	}
}
