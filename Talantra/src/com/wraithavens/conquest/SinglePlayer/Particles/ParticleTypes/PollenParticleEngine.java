package com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes;

public class PollenParticleEngine extends ChunkParticleEngine{
	public PollenParticleEngine(){
		super(90, 30);
	}
	public void dispose(){}
	@Override
	public void newParticle(double time){
		// TODO Make quick accessing of biome for a block.
		// float x = Algorithms.random(64);
		// float z = Algorithms.random(64);
		// int fx = (int)x;
		// int fz = (int)z;
		// if(chunkHeights.getBiomeRaw(fx, fz)!=Biome.TayleaMeadow)
		// return;
		// float y = chunkHeights.getHeightRaw(fx, fz)+1.5f;
		// x += chunkHeights.getX();
		// z += chunkHeights.getZ();
		// if(camera.distanceSquared(x, y, z)>=100*100)
		// return;
		// batch.addParticle(new Pollen(x, y, z, time));
	}
}
