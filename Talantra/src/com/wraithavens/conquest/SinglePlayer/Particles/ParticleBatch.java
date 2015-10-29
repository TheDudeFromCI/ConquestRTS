package com.wraithavens.conquest.SinglePlayer.Particles;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector2f;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.Math.Vector4f;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class ParticleBatch{
	private final int vbo;
	private final int particleBuffer;
	private final ShaderProgram shader;
	private FloatBuffer particleData;
	private final int offsetAttribLocation;
	private final int scaleAttribLocation;
	private final int colorAttribLocation;
	private int maxParticleCount;
	private final ArrayList<Particle> particles = new ArrayList(maxParticleCount);
	private final ArrayList<ParticleEngine> engines = new ArrayList();
	public ParticleBatch(){
		maxParticleCount = WraithavensConquest.Settings.getParticleCount();
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
		particleData = BufferUtils.createFloatBuffer(9*maxParticleCount);
		shader = new ShaderProgram("Particle");
		shader.bind();
		offsetAttribLocation = shader.getAttributeLocation("att_offset");
		scaleAttribLocation = shader.getAttributeLocation("att_scale");
		colorAttribLocation = shader.getAttributeLocation("att_color");
		GL20.glEnableVertexAttribArray(offsetAttribLocation);
		GL20.glEnableVertexAttribArray(scaleAttribLocation);
		GL20.glEnableVertexAttribArray(colorAttribLocation);
	}
	public void addParticle(Particle particle){
		if(particles.size()>=maxParticleCount)
			return;
		particles.add(particle);
	}
	public void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(particleBuffer);
		shader.dispose();
	}
	public void render(){
		if(particles.size()==0||maxParticleCount==0)
			return;
		shader.bind();
		GL11.glEnable(GL11.GL_BLEND);
		GL33.glVertexAttribDivisor(offsetAttribLocation, 1);
		GL33.glVertexAttribDivisor(scaleAttribLocation, 1);
		GL33.glVertexAttribDivisor(colorAttribLocation, 1);
		GL11.glDepthMask(false);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 8, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, particleBuffer);
		GL20.glVertexAttribPointer(offsetAttribLocation, 3, GL11.GL_FLOAT, false, 36, 0);
		GL20.glVertexAttribPointer(scaleAttribLocation, 2, GL11.GL_FLOAT, false, 36, 12);
		GL20.glVertexAttribPointer(colorAttribLocation, 4, GL11.GL_FLOAT, false, 36, 20);
		GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, 4, particles.size());
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL33.glVertexAttribDivisor(offsetAttribLocation, 0);
		GL33.glVertexAttribDivisor(scaleAttribLocation, 0);
		GL33.glVertexAttribDivisor(colorAttribLocation, 0);
		GL11.glDepthMask(true);
	}
	public void setMaxParticles(int count){
		maxParticleCount = count;
		particleData = BufferUtils.createFloatBuffer(9*maxParticleCount);
	}
	public void update(double delta, double time){
		// TODO Improve particle storage system.
		/*
		 * This can be done by storing everything in a static array. Then, when
		 * a particle dies, take whatever particle is at the end, and move it to
		 * the newly created gap. Continue iterating until the end of the list
		 * has been reached. Only update VBO when particles are moved around on
		 * this list. (Or added.)
		 */
		for(int i = 0; i<engines.size(); i++)
			engines.get(i).update(time);
		for(int i = 0; i<particles.size();){
			particles.get(i).update(delta, time);
			if(!particles.get(i).isAlive()){
				particles.remove(i);
				continue;
			}
			particles.get(i).setCameraDistance(SinglePlayerGame.INSTANCE.getCamera());
			i++;
		}
		particleData.clear();
		Vector4f color;
		Vector3f location;
		Vector2f scale;
		for(int i = 0; i<particles.size(); i++){
			if(i>=maxParticleCount)
				break;
			location = particles.get(i).getLocation();
			scale = particles.get(i).getScale();
			color = particles.get(i).getColor();
			particleData.put(location.x);
			particleData.put(location.y);
			particleData.put(location.z);
			particleData.put(scale.x);
			particleData.put(scale.y);
			particleData.put(color.x);
			particleData.put(color.y);
			particleData.put(color.z);
			particleData.put(color.w);
		}
		particleData.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, particleBuffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, particleData, GL15.GL_STREAM_DRAW);
	}
	void addEngine(ParticleEngine engine){
		engines.add(engine);
	}
	void removeEngine(ParticleEngine engine){
		engines.remove(engine);
	}
}
