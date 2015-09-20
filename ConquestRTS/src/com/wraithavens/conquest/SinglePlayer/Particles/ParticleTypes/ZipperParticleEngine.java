package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.ChunkHeightData;
import com.wraithavens.conquest.SinglePlayer.Noise.Biome;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleBatch;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class ZipperParticleEngine extends ChunkParticleEngine{
	public ZipperParticleEngine(ParticleBatch batch, ChunkHeightData chunkHeights, Camera camera){
		super(batch, chunkHeights, camera, 2, 10);
	}
	public void dispose(){}
	@Override
	public void newParticle(double time){
		float x = (float)Math.random()*64;
		float z = (float)Math.random()*64;
		if(chunkHeights.getBiomeRaw((int)x, (int)z)!=Biome.ArcstoneHills)
			return;
		float y = chunkHeights.getHeightRaw((int)x, (int)z)+1.3f;
		x += chunkHeights.getX();
		z += chunkHeights.getZ();
		if(camera.distanceSquared(x, y, z)>=100*100)
			return;
		int length = (int)(Math.random()*5+5);
		Vector3f origin = new Vector3f(x, y, z);
		Vector3f direction =
			new Vector3f((float)(Math.random()*2-1), (float)(Math.random()*2-1)*0.1f, (float)(Math.random()*2-1));
		direction.normalize();
		float speed = (float)(Math.random()*2+0.5f);
		float waveRate = (float)(Math.random()*0.5f+0.1f);
		float waveSize = (float)(Math.random()*0.3f+0.2f);
		float life = (float)(Math.random()*10+6);
		boolean snake = Math.random()>=0.5;
		if(snake){
			waveRate *= 1.5f;
			speed *= 2;
		}else
			waveRate *= 0.5f;
		for(int i = 0; i<length; i++)
			batch.addParticle(new Zipper(origin, direction, speed, waveRate, waveSize, life, i*0.1f, time, snake
				?0.7f:1-i/(length-2f), snake));
	}
}
