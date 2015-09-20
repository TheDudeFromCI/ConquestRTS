package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.ChunkHeightData;
import com.wraithavens.conquest.SinglePlayer.Noise.Biome;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleBatch;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class ZipperParticleEngine extends ChunkParticleEngine{
	@SuppressWarnings("ucd")
	public ZipperParticleEngine(ParticleBatch batch, ChunkHeightData chunkHeights, Camera camera){
		super(batch, chunkHeights, camera, 1.5f, 10);
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
		int length = (int)(Math.random()*5+5)*4;
		Vector3f origin = new Vector3f(x, y, z);
		Vector3f direction =
			new Vector3f((float)(Math.random()*2-1), (float)Math.random()*0.01f, (float)(Math.random()*2-1));
		direction.normalize();
		float speed = (float)(Math.random()*2+0.5f)+20;
		float waveRate = (float)(Math.random()*0.1f+0.1f)*1.7f;
		float waveSize = (float)(Math.random()*0.6f+0.2f)*3;
		float life = (float)(Math.random()*10+6);
		int randomColor = (int)(Math.random()*5);
		float red, green, blue;
		switch(randomColor){
			case 0:
				red = 90/255f*1.3f;
				green = 110/255f*1.3f;
				blue = 20/255f*1.3f;
				break;
			case 1:
				red = 220/255f;
				green = 190/255f;
				blue = 20/255f;
				break;
			case 2:
				red = 120/255f;
				green = 120/255f;
				blue = 120/255f;
				break;
			case 3:
				red = 160/255f;
				green = 160/255f;
				blue = 160/255f;
				break;
			case 4:
				red = 70/255f;
				green = 70/255f;
				blue = 70/255f;
				break;
			default:
				red = 0;
				green = 0;
				blue = 0;
				break;
		}
		int generator = length/2;
		for(int i = 0; i<length; i++)
			batch.addParticle(new Zipper(origin, direction, speed, waveRate, waveSize, life, i*0.02f, time, 1-i
				/(length-2f), i==generator?batch:null, red, green, blue));
	}
}
