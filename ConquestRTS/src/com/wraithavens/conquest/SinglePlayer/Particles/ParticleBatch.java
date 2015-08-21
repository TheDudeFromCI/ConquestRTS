package com.wraithavens.conquest.SinglePlayer.Particles;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class ParticleBatch{
	private static final int MaxParticleCount = 2;
	private final int vbo;
	private final int particleBuffer;
	private final ShaderProgram shader;
	private final FloatBuffer particleData;
	private final int offsetAttribLocation;
	private final int scaleAttribLocation;
	private int particleCount = 1;
	ParticleBatch(){
		vbo = GL15.glGenBuffers();
		particleBuffer = GL15.glGenBuffers();
		{
			FloatBuffer vertexData = BufferUtils.createFloatBuffer(8);
			vertexData.put(-0.5f).put(-0.5f);
			vertexData.put(0.5f).put(-0.5f);
			vertexData.put(-0.5f).put(0.5f);
			vertexData.put(0.5f).put(0.5f);
			vertexData.flip();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
		}
		{
			particleData = BufferUtils.createFloatBuffer(5*MaxParticleCount);
			particleData.put(4096).put(1097).put(4091).put(1.0f).put(1.0f);
			particleData.flip();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, particleBuffer);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, particleData, GL15.GL_STREAM_DRAW);
		}
		shader = new ShaderProgram("Particle");
		shader.bind();
		offsetAttribLocation = shader.getAttributeLocation("att_offset");
		scaleAttribLocation = shader.getAttributeLocation("att_scale");
		GL20.glEnableVertexAttribArray(offsetAttribLocation);
		GL33.glVertexAttribDivisor(offsetAttribLocation, 1);
		GL20.glEnableVertexAttribArray(scaleAttribLocation);
		GL33.glVertexAttribDivisor(scaleAttribLocation, 1);
	}
	void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(particleBuffer);
		shader.dispose();
	}
	void render(){
		shader.bind();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 8, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, particleBuffer);
		GL20.glVertexAttribPointer(offsetAttribLocation, 3, GL11.GL_FLOAT, false, 20, 0);
		GL20.glVertexAttribPointer(scaleAttribLocation, 2, GL11.GL_FLOAT, false, 20, 12);
		GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, 4, particleCount);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
}
