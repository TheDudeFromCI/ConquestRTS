package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import org.lwjgl.glfw.GLFW;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleEngine;

abstract class ChunkParticleEngine implements ParticleEngine{
	private final double creationTime;
	private final double particlesPerSecond;
	private final int maxParticlesPerFrame;
	private long generated = 0;
	ChunkParticleEngine(double particlesPerSecond, int maxParticlesPerFrame){
		creationTime = GLFW.glfwGetTime();
		this.particlesPerSecond = particlesPerSecond;
		this.maxParticlesPerFrame = maxParticlesPerFrame;
	}
	public abstract void newParticle(double time);
	public void update(double time){
		long particles = (long)((time-creationTime)*particlesPerSecond)-generated;
		generated += particles;
		if(particles>maxParticlesPerFrame)
			particles = maxParticlesPerFrame;
		for(int i = 0; i<particles; i++)
			newParticle(time);
	}
}
