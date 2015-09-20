package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import org.lwjgl.glfw.GLFW;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.ChunkHeightData;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleBatch;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleEngine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

abstract class ChunkParticleEngine implements ParticleEngine{
	protected final ChunkHeightData chunkHeights;
	protected final ParticleBatch batch;
	private final double creationTime;
	protected final Camera camera;
	private final double particlesPerSecond;
	private final int maxParticlesPerFrame;
	private long generated = 0;
	ChunkParticleEngine(
		ParticleBatch batch, ChunkHeightData chunkHeights, Camera camera, double particlesPerSecond,
		int maxParticlesPerFrame){
		this.chunkHeights = chunkHeights;
		this.batch = batch;
		this.camera = camera;
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
