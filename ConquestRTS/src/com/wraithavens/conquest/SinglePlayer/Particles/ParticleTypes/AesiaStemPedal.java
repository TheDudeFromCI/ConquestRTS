package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import com.wraithavens.conquest.SinglePlayer.Particles.Particle;

public class AesiaStemPedal extends Particle{
	private final float randomX;
	private final float randomZ;
	private final float endY;
	private final double creationTime;
	private float speed;
	public AesiaStemPedal(float x, float y, float z, double time){
		creationTime = time;
		endY = y-6;
		location.set(x, y, z);
		scale.set(1/7f, 1/7f);
		int randomColor = (int)(Math.random()*6);
		switch(randomColor){
			case 0:
				color.set(249/255f, 181/255f, 223/255f, 1);
				break;
			case 1:
				color.set(231/255f, 143/255f, 197/255f, 1);
				break;
			case 2:
				color.set(152/255f, 82/255f, 125/255f, 1);
				break;
			case 3:
				color.set(112/255f, 29/255f, 81/255f, 1);
				break;
			case 4:
				color.set(200/255f, 118/255f, 169/255f, 1);
				break;
			case 5:
				color.set(234/255f, 114/255f, 188/255f, 1);
				break;
			default:
				throw new AssertionError();
		}
		randomX = (float)(Math.random()*2-1);
		randomZ = (float)(Math.random()*2-1);
	}
	@Override
	public boolean isAlive(){
		return location.y>endY&&speed!=-1;
	}
	@Override
	public void update(double delta, double time){
		double age = time-creationTime;
		speed += delta*0.01f;
		location.x += randomX*delta;
		location.y -= speed;
		location.z += randomZ*delta;
		color.w = (float)Math.min(2*age, 1);
		if(age>5)
			speed = -1;
	}
}
