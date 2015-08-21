package com.wraithavens.conquest.SinglePlayer.Particles;

public class ParticleEngine{
	private final ParticleBatch particleBatch;
	public ParticleEngine(){
		particleBatch = new ParticleBatch();
	}
	public void dispose(){
		particleBatch.dispose();
	}
	public void render(){
		particleBatch.render();
	}
	public void update(double time){
		// TODO
	}
}
