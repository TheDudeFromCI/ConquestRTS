package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

import org.lwjgl.opengl.GL15;

//TODO Make these reused buffers, instead of creating new ones for each chunk model.
public class ChunkModel{
	private final int vbo;
	private final int ibo;
	public ChunkModel(){
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
	}
}