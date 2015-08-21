package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import com.wraithavens.conquest.SinglePlayer.Particles.Particle;

public class Pollen extends Particle{
	private float origin;
	public Pollen(float x, float y, float z){
		location.set(x, y, z);
		scale.set(1, 1);
		origin = y;
	}
	@Override
	public boolean isAlive(){
		return true;
	}
	@Override
	public void update(double time){
		location.y = origin+(float)Math.sin(time);
	}
}
