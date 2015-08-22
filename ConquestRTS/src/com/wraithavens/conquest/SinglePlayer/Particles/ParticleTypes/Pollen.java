package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import com.wraithavens.conquest.SinglePlayer.Particles.Particle;

public class Pollen extends Particle{
	private double endOfLife;
	private double speed;
	public Pollen(float x, float y, float z, double time){
		location.set(x, y, z);
		scale.set(1, 1);
		endOfLife = time+7;
	}
	@Override
	public boolean isAlive(){
		return endOfLife!=-1;
	}
	@Override
	public void update(double delta, double time){
		speed += delta*0.01f;
		location.y -= speed;
		if(time>=endOfLife)
			endOfLife = -1;
	}
}
