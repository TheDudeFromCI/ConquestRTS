package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleBatch;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class AesiaStemPedals extends EntityParticleEngine{
	private static final Vector3f Offset = new Vector3f(0, 4, 0);
	private static final Vector3f SpawnBubble = new Vector3f(1, 3, 1);
	public AesiaStemPedals(ParticleBatch particleBatch, Vector3f position, Camera camera){
		super(particleBatch, position, Offset, SpawnBubble, camera, 0.5f, 1);
	}
	public void dispose(){}
	@Override
	public void newParticle(double time){
		if(camera.distanceSquared(position.x, position.y, position.z)>=100*100)
			return;
		float x = (float)(Math.random()*2-1);
		float y = (float)(Math.random()*2-1);
		float z = (float)(Math.random()*2-1);
		float t = x*x+y*y+z*z;
		float d = (float)Math.random();
		x = x/t*d*spawnBubble.x+offset.x+position.x;
		y = y/t*d*spawnBubble.y+offset.y+position.y;
		z = z/t*d*spawnBubble.z+offset.z+position.z;
		batch.addParticle(new AesiaStemPedal(x, y, z, time));
	}
}
