package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Particles.Particle;

public class Zipper extends Particle{
	private final Vector3f direction;
	private final Vector3f origin;
	private final float speed;
	private final float waveRate;
	private final float waveSize;
	private final float timerOffset;
	private final float life;
	private final double creationTime;
	private boolean alive = true;
	Zipper(
		Vector3f origin, Vector3f direction, float speed, float waveRate, float waveSize, float life,
		float timerOffset, double creationTime, float scalePercent, boolean snake){
		this.direction = direction;
		this.speed = speed;
		this.waveRate = waveRate;
		this.waveSize = waveSize;
		this.timerOffset = timerOffset;
		this.life = life;
		this.origin = origin;
		this.creationTime = creationTime;
		scale.set(2/8f*scalePercent, 2/8f*scalePercent);
		color.set(90/255f, 110/255f, 20/255f, 0);
		color.scale(snake?1.6f:1.3f);
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
		}else
			alive = false;
	}
}
