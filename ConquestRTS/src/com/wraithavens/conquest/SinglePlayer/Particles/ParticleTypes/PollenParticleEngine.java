package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import org.lwjgl.glfw.GLFW;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.ChunkHeightData;
import com.wraithavens.conquest.SinglePlayer.Noise.Biome;
import com.wraithavens.conquest.SinglePlayer.Particles.Particle;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleBatch;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleEngine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class PollenParticleEngine implements ParticleEngine{
	private static final int ParticlesPerSecond = 90;
	private static final int MaxParticlesPerFrame = 30;
	private final ChunkHeightData chunkHeights;
	private final ParticleBatch batch;
	private final double creationTime;
	private final Camera camera;
	private long generated = 0;
	public PollenParticleEngine(ParticleBatch batch, ChunkHeightData chunkHeights, Camera camera){
		this.chunkHeights = chunkHeights;
		this.batch = batch;
		this.camera = camera;
		creationTime = GLFW.glfwGetTime();
	}
	public void dispose(){}
	public void update(double time){
		long particles = (long)((time-creationTime)*ParticlesPerSecond)-generated;
		generated += particles;
		if(particles>MaxParticlesPerFrame)
			particles = MaxParticlesPerFrame;
		Particle particle;
		for(int i = 0; i<particles; i++){
			particle = newParticle(time);
			if(particle!=null)
				batch.addParticle(particle);
		}
	}
	private Particle newParticle(double time){
		float x = (float)Math.random()*64;
		float z = (float)Math.random()*64;
		if(chunkHeights.getBiomeRaw((int)x, (int)z)!=Biome.TayleaMeadow)
			return null;
		float y = chunkHeights.getHeightRaw((int)x, (int)z)+1.5f;
		x += chunkHeights.getX();
		z += chunkHeights.getZ();
		if(camera.distanceSquared(x, y, z)>=100*100)
			return null;
		return new Pollen(x, y, z, time);
	}
}
