package com.wraithavens.conquest.SinglePlayer.Particles;

public class BiomeParticleEngine{
	private final ParticleEngine[] engines;
	private final int x;
	private final int z;
	public BiomeParticleEngine(int x, int z){
		this.x = x;
		this.z = z;
		engines = new ParticleEngine[0];
		// TODO Redo this part.
		// ArrayList<Biome> biomes = new ArrayList();
		// Biome bio;
		// for(byte b : chunkHeights.getBiomes()){
		// bio = Biome.values()[b&0xFF];
		// if(!biomes.contains(bio))
		// biomes.add(bio);
		// }
		// int i;
		// for(i = 0; i<biomes.size();)
		// if(biomes.get(i).getParticleEngine()!=null)
		// i++;
		// else
		// biomes.remove(i);
		// engines = new ParticleEngine[biomes.size()];
		// try{
		// for(i = 0; i<biomes.size(); i++){
		// engines[i] =
		// (ParticleEngine)biomes.get(i).getParticleEngine().getConstructors()[0]
		// .newInstance(batch, chunkHeights, camera);
		// batch.addEngine(engines[i]);
		// }
		// }catch(Exception exception){
		// exception.printStackTrace();
		// }
	}
	public void dispose(){
		for(ParticleEngine e : engines){
			e.dispose();
			// batch.removeEngine(e);
		}
	}
	public int getX(){
		return x;
	}
	public int getZ(){
		return z;
	}
	public void update(double time){
		for(ParticleEngine e : engines)
			e.update(time);
	}
}
