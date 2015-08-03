package com.wraithavens.conquest.SinglePlayer.Noise;

import java.util.ArrayList;

class AdvancedNoise{
	private final ArrayList<SubNoise> noise = new ArrayList();
	void addSubNoise(SubNoise subnoise){
		noise.add(subnoise);
	}
	double getMaxHeight(){
		double f = 0;
		for(SubNoise n : noise)
			f += n.amplitude;
		return f;
	}
	double noise(float x, float y){
		if(noise.size()==0)
			throw new IllegalStateException("No noise generators defined!");
		double total = 0;
		for(int i = 0; i<noise.size(); i++)
			total += noise.get(i).noise(x, y);
		return total;
	}
}
