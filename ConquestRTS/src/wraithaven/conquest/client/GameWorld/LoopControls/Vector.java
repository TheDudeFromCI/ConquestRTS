package wraithaven.conquest.client.GameWorld.LoopControls;

import java.nio.FloatBuffer;

public abstract class Vector{
	public final float length(){
		return (float)Math.sqrt(lengthSquared());
	}
	public abstract float lengthSquared();
	public abstract Vector load(FloatBuffer buf);
	public abstract Vector negate();
	public final Vector normalise(){
		float len = length();
		if(len!=0){
			float l = 1.0f/len;
			return scale(l);
		}
		throw new IllegalStateException("Zero length vector");
	}
	public abstract Vector scale(float scale);
	public abstract Vector store(FloatBuffer buf);
}