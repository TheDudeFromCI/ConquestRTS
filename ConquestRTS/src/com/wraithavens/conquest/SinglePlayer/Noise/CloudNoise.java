package com.wraithavens.conquest.SinglePlayer.Noise;

import java.util.ArrayList;
import com.wraithavens.conquest.Math.Vector4f;

public class CloudNoise{
	private final ArrayList<ColoredSubNoise> noise = new ArrayList();
	private final Vector4f temp = new Vector4f();
	public void addSubNoise(ColoredSubNoise subnoise){
		noise.add(subnoise);
	}
	public void clear(){
		noise.clear();
	}
	public float noise(float x, float y, float z, Vector4f out){
		// ---
		// Check to make sure that we have dice to roll.
		// ---
		if(noise.size()==0)
			throw new IllegalStateException("No noise generators defined!");
		// ---
		// Sum up the output of all functions.
		// ---
		out.zero();
		float h = 0;
		for(int i = 0; i<noise.size(); i++){
			h += noise.get(i).noise(x, y, z, temp);
			out.add(temp);
		}
		// ---
		// And average the color. Clamp JUST in case something went wrong.
		// ---
		out.div(noise.size());
		out.clamp(0.0f, 1.0f);
		// ---
		// And return. :D
		// ---
		return h/noise.size();
	}
	double getMaxHeight(){
		double f = 0;
		for(ColoredSubNoise n : noise)
			f += n.amplitude;
		return f;
	}
}
