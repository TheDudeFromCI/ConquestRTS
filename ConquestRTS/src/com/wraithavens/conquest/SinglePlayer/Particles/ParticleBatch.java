package com.wraithavens.conquest.SinglePlayer.Particles;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import com.wraithavens.conquest.Math.Vector2f;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class ParticleBatch{
	private static final int MaxParticleCount = 2;
	private final int vbo;
	private final int particleBuffer;
	private final ShaderProgram shader;
	private final FloatBuffer particleData;
	private final int offsetAttribLocation;
	private final int scaleAttribLocation;
	private final ArrayList<Particle> particles = new ArrayList(MaxParticleCount);
	private final Comparator particleSorter = new Comparator<Particle>(){
		public int compare(Particle a, Particle b){
			return a.getCameraDistance()==b.getCameraDistance()?0:a.getCameraDistance()<b.getCameraDistance()?1
				:-1;
		}
	};
	private final Camera camera;
	public ParticleBatch(Camera camera){
		this.camera = camera;
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
	public void addParticle(Particle particle){
		particles.add(particle);
	}
	public void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(particleBuffer);
		shader.dispose();
	}
	public void render(){
		if(particles.size()==0)
			return;
		shader.bind();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 8, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, particleBuffer);
		GL20.glVertexAttribPointer(offsetAttribLocation, 3, GL11.GL_FLOAT, false, 20, 0);
		GL20.glVertexAttribPointer(scaleAttribLocation, 2, GL11.GL_FLOAT, false, 20, 12);
		GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, 4, particles.size());
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
	}
	public void update(double time){
		for(int i = 0; i<particles.size();){
			particles.get(i).update(time);
			if(!particles.get(i).isAlive()){
				particles.remove(i);
				continue;
			}
			particles.get(i).setCameraDistance(camera);
			i++;
		}
		particles.sort(particleSorter);
		particleData.clear();
		Vector3f location;
		Vector2f scale;
		for(int i = 0; i<particles.size(); i++){
			location = particles.get(i).getLocation();
			scale = particles.get(i).getScale();
			particleData.put(location.x);
			particleData.put(location.y);
			particleData.put(location.z);
			particleData.put(scale.x);
			particleData.put(scale.y);
		}
		particleData.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, particleBuffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, particleData, GL15.GL_STREAM_DRAW);
	}
}
