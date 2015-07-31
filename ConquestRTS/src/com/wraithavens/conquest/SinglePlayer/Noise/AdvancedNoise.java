package com.wraithavens.conquest.SinglePlayer.Noise;

import java.util.ArrayList;

public class AdvancedNoise{
	private final ArrayList<SubNoise> noise = new ArrayList();
	private double offset;
	public void addSubNoise(SubNoise subnoise){
		noise.add(subnoise);
	}
	public double getMaxHeight(){
		double f = 0;
		for(SubNoise n : noise)
			f += n.amplitude;
		return f+offset;
	}
	public double getOffset(){
		return offset;
	}
	public double noise(float x, float y){
		if(noise.size()==0)
			throw new IllegalStateException("No noise generators defined!");
		double total = 0;
		for(int i = 0; i<noise.size(); i++)
			total += noise.get(i).noise(x, y);
		return total+offset;
	}
	public double normalisedNoise(float x, float y){
		return noise(x, y)/getMaxHeight();
	}
	public void setOffset(double offset){
		this.offset = offset;
	}
}
