package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import org.lwjgl.glfw.GLFW;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleBatch;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleEngine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

abstract class EntityParticleEngine implements ParticleEngine{
	private final double creationTime;
	protected final Camera camera;
	private final double particlesPerSecond;
	private final int maxParticlesPerFrame;
	protected final Vector3f position;
	protected final Vector3f offset;
	protected final Vector3f spawnBubble;
	protected final ParticleBatch batch;
	private long generated = 0;
	EntityParticleEngine(
		ParticleBatch batch, Vector3f position, Vector3f offset, Vector3f spawnBubble, Camera camera,
		double particlesPerSecond, int maxParticlesPerFrame){
		this.batch = batch;
		this.position = position;
		this.offset = offset;
		this.spawnBubble = spawnBubble;
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
