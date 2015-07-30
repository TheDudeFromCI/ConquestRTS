package com.wraithavens.conquest.SinglePlayer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.WorldRenderer;

public class ChunkMesh{
	public final int vbo;
	public final int ibo;
	public int indexCount;
	public ChunkMesh(){
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
	}
	public void rebuildData(){
		//TODO
	}
	public void render(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 28, 0);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 28, 12);
		GL20.glVertexAttribPointer(WorldRenderer.SHADER_LOCATION, 1, GL11.GL_FLOAT, false, 28, 24);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_SHORT, 0);
	}
}