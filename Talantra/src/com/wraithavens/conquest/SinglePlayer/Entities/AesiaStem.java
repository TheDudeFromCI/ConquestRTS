package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes.AesiaStemPedals;

public class AesiaStem extends Entity{
	private final AesiaStemPedals particles;
	public AesiaStem(EntityType type){
		super(type);
		particles =
			new AesiaStemPedals(SinglePlayerGame.INSTANCE.getParticleBatch(), position,
				SinglePlayerGame.INSTANCE.getCamera());
	}
	@Override
	void update(double time){
		particles.update(time);
	}
}
