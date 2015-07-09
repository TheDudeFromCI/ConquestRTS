package com.wraithavens.conquest.SinglePlayer;

import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Texture;

public interface QuadListener{
	public void addQuad(Quad q);
	public void prepare(Texture texture);
}