package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import org.lwjgl.glfw.GLFW;
import com.wraithavens.conquest.SinglePlayer.Particles.Particle;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleBatch;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleEngine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class PollenParticleEngine implements ParticleEngine{
	private static final int ParticlesPerSecond = 90;
	private final float r;
	private final ParticleBatch batch;
	private long generated = 0;
	private double creationTime;
	private final Camera camera;
	public PollenParticleEngine(ParticleBatch batch, Camera camera, float r){
		this.camera = camera;
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
			new Pollen((float)(camera.x+Math.cos(spin)*distance), (float)(camera.y-Math.random()*3),
				(float)(camera.z+Math.sin(spin)*distance), time);
		return p;
	}
}
