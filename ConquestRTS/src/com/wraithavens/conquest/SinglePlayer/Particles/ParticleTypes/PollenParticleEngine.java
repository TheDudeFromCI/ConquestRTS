package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import org.lwjgl.glfw.GLFW;
import com.wraithavens.conquest.SinglePlayer.Particles.Particle;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleBatch;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleEngine;

public class PollenParticleEngine implements ParticleEngine{
	private static final int ParticlesPerSecond = 20;
	private final float x;
	private final float y;
	private final float z;
	private final float r;
	private final ParticleBatch batch;
	private long generated = 0;
	private double creationTime;
	public PollenParticleEngine(ParticleBatch batch, float x, float y, float z, float r){
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.batch = batch;
		creationTime = GLFW.glfwGetTime();
	}
	public void update(double time){
		long particles = (long)((time-creationTime)*ParticlesPerSecond)-generated;
		generated += particles;
		for(int i = 0; i<particles; i++)
			batch.addParticle(newParticle(time));
	}
	private Particle newParticle(double time){
		double spin = Math.random()*Math.PI*2;
		double distance = Math.random()*r;
		Pollen p =
			new Pollen((float)(x+Math.cos(spin)*distance), (float)(y+Math.random()*5), (float)(z+Math.sin(spin)
				*distance), time);
		return p;
	}
}
