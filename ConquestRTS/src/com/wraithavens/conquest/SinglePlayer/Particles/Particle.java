package com.wraithavens.conquest.SinglePlayer.Particles;

import com.wraithavens.conquest.Math.Vector2f;
import com.wraithavens.conquest.Math.Vector3f;

public abstract class Particle{
	protected final Vector3f location = new Vector3f();
	protected final Vector2f scale = new Vector2f();
	public Vector3f getLocation(){
		return location;
	}
	public Vector2f getScale(){
		return scale;
	}
	public abstract void update(double time);
}
