package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Particles.Particle;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleBatch;

class Zipper extends Particle{
	private final double DustPerSecond = 7;
	private final int MaxDustPerFrame = 3;
	private final Vector3f direction;
	private final Vector3f origin;
	private final float speed;
	private final float waveRate;
	private final float waveSize;
	private final float timerOffset;
	private final float life;
	private final double creationTime;
	private final ParticleBatch batch;
	private boolean alive = true;
	private int generated;
	Zipper(
		Vector3f origin, Vector3f direction, float speed, float waveRate, float waveSize, float life,
		float timerOffset, double creationTime, float scalePercent, ParticleBatch batch, float red, float green,
		float blue){
		this.batch = batch;
		this.direction = direction;
		this.speed = speed;
		this.waveRate = waveRate;
		this.waveSize = waveSize;
		this.timerOffset = timerOffset;
		this.life = life;
		this.origin = origin;
		this.creationTime = creationTime;
		scale.set(2/8f*scalePercent, 2/8f*scalePercent);
		color.set(red, green, blue, 0);
	}
	@Override
	public boolean isAlive(){
		return alive;
	}
	@Override
	public void update(double delta, double time){
		double age = time-creationTime;
		if(age<life){
			alive = true;
			color.w = (float)Math.sin(age/life*Math.PI);
			location.set((float)(direction.x*(age-timerOffset)*speed+origin.x), (float)(direction.y
				*(age-timerOffset)*speed+origin.y)
				+(float)Math.sin((age-timerOffset)/waveRate)*waveSize, (float)(direction.z*(age-timerOffset)
					*speed+origin.z));
			if(batch!=null){
				long particles = (long)((time-creationTime)*DustPerSecond)-generated;
				generated += particles;
				if(particles>MaxDustPerFrame)
					particles = MaxDustPerFrame;
				for(int i = 0; i<particles; i++)
					batch.addParticle(new ZipperDust(location.x, location.y, location.z, time));
			}
		}else
			alive = false;
	}
}
