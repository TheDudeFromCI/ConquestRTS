package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.ChunkHeightData;
import com.wraithavens.conquest.SinglePlayer.Noise.Biome;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleBatch;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class PollenParticleEngine extends ChunkParticleEngine{
	@SuppressWarnings("ucd")
	public PollenParticleEngine(ParticleBatch batch, ChunkHeightData chunkHeights, Camera camera){
		super(batch, chunkHeights, camera, 90, 30);
	}
	public void dispose(){}
	@Override
	public void newParticle(double time){
		float x = (float)Math.random()*64;
		float z = (float)Math.random()*64;
		if(chunkHeights.getBiomeRaw((int)x, (int)z)!=Biome.TayleaMeadow)
			return;
		float y = chunkHeights.getHeightRaw((int)x, (int)z)+1.5f;
		x += chunkHeights.getX();
		z += chunkHeights.getZ();
		if(camera.distanceSquared(x, y, z)>=100*100)
			return;
		batch.addParticle(new Pollen(x, y, z, time));
	}
}
