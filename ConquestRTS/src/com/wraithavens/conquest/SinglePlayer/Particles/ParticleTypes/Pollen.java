package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import com.wraithavens.conquest.SinglePlayer.Particles.Particle;

public class Pollen extends Particle{
	private float endOfLife;
	private float speed;
	private float randomX;
	private float randomZ;
	public Pollen(float x, float y, float z, double time){
		location.set(x, y, z);
		scale.set(1/8f, 1/8f);
		endOfLife = (float)(time+2);
		color.set(140/255f, 160/255f, 80/255f, 1);
		randomX = (float)(Math.random()*2-1);
		randomZ = (float)(Math.random()*2-1);
	}
	@Override
	public boolean isAlive(){
		return endOfLife!=-1;
	}
	@Override
	public void update(double delta, double time){
		speed += delta*0.02f;
		location.x += randomX*delta;
		location.y += speed;
		location.z += randomZ*delta;
		color.w = (float)Math.sin((endOfLife-time)/2*Math.PI);
		if(time>=endOfLife)
			endOfLife = -1;
	}
}
