package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.io.File;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class Dynmap{
	static final int VertexCount = 9;
	static final int BlocksPerChunk = 64;
	private final int vbo;
	private final DynmapChunk chunk;
	private final ShaderProgram shader;
	public Dynmap(){
		vbo = GL15.glGenBuffers();
		loadVbo();
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "Dynmap.vert"), null, new File(
				WraithavensConquest.assetFolder, "Dynmap.frag"));
		shader.loadUniforms("shift");
		chunk = new DynmapChunk(8192/BlocksPerChunk, 8192/BlocksPerChunk);
	}
	public void render(){
		shader.bind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 8, 0);
		chunk.render(shader);
	}
	private void loadVbo(){
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(VertexCount*VertexCount*3);
		int x, z;
		for(z = 0; z<VertexCount; z++)
			for(x = 0; x<VertexCount; x++){
				vertexData.put((float)x/VertexCount*BlocksPerChunk);
				vertexData.put((float)z/VertexCount*BlocksPerChunk);
			}
		vertexData.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
	}
}
