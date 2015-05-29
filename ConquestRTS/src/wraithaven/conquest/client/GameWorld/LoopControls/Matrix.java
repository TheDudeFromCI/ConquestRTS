package wraithaven.conquest.client.GameWorld.LoopControls;

import java.nio.FloatBuffer;

public abstract class Matrix{
	public abstract float determinant();
	public abstract Matrix invert();
	public abstract Matrix load(FloatBuffer buf);
	public abstract Matrix loadTranspose(FloatBuffer buf);
	public abstract Matrix negate();
	public abstract Matrix setIdentity();
	public abstract Matrix setZero();
	public abstract Matrix store(FloatBuffer buf);
	public abstract Matrix storeTranspose(FloatBuffer buf);
	public abstract Matrix transpose();
}