package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import com.wraithavens.conquest.SinglePlayer.Particles.Particle;

public class AesiaStemPedal extends Particle{
	private final float randomX;
	private final float randomZ;
	private final float endY;
	private final double creationTime;
	private float speed;
	public AesiaStemPedal(float x, float y, float z, double time){
		creationTime = time;
		endY = y-6;
		location.set(x, y, z);
		scale.set(1/7f, 1/7f);
		color.set(200/255f, 140/255f, 120/255f, 1);
		randomX = (float)(Math.random()*2-1);
		randomZ = (float)(Math.random()*2-1);
	}
	@Override
	public boolean isAlive(){
		return location.y>endY&&speed!=-1;
	}
	@Override
	public void update(double delta, double time){
		double age = time-creationTime;
		speed += delta*0.01f;
		location.x += randomX*delta;
		location.y -= speed;
		location.z += randomZ*delta;
		color.w = (float)Math.min(2*age, 1);
		if(age>5)
			speed = -1;
	}
}
