package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import com.wraithavens.conquest.SinglePlayer.Particles.Particle;

public class Pollen extends Particle{
	private float origin;
	private double offset;
	public Pollen(float x, float y, float z, double offset){
		location.set(x, y, z);
		scale.set(1, 1);
		origin = y;
		this.offset = offset;
	}
	@Override
	public boolean isAlive(){
		return true;
	}
	@Override
	public void update(double time){
		location.y = origin+(float)Math.sin(time+offset);
	}
}
