package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import com.wraithavens.conquest.SinglePlayer.Particles.Particle;

public class ZipperDust extends Particle{
	private float endOfLife;
	private float speed;
	private float randomX;
	private float randomZ;
	public ZipperDust(float x, float y, float z, double time){
		location.set(x, y, z);
		scale.set(1/12f, 1/12f);
		endOfLife = (float)(time+1);
		color.set(140/255f, 160/255f, 80/255f, 1);
		color.scale(1.8f);
		randomX = (float)(Math.random()*2-1);
		randomZ = (float)(Math.random()*2-1);
	}
	@Override
	public boolean isAlive(){
		return endOfLife!=-1;
	}
	@Override
	public void update(double delta, double time){
		speed += delta*0.01f;
		location.x += randomX*delta;
		location.y += speed;
		location.z += randomZ*delta;
		color.w = (float)Math.sin(Math.pow((endOfLife-time)/2, 0.25f)*Math.PI);
		if(time>=endOfLife)
			endOfLife = -1;
	}
}
