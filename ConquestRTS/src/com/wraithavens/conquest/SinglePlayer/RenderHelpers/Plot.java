package com.wraithavens.conquest.SinglePlayer.RenderHelpers;

public interface Plot<T>{
	public void end();
	public T get();
	public boolean next();
	public void reset();
}
