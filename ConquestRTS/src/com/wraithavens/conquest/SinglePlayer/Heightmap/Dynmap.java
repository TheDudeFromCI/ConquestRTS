package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.io.File;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.MatrixUtils;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.Algorithms;

public class Dynmap{
	static final int VertexCount = 1025;
	public static final int BlocksPerChunk = 16768;
	static final int MaxDepth = Integer.numberOfTrailingZeros(VertexCount-1)-1;
	public static final int WalkingWrapDistance = 8192;
	private static final int WalkingViewBuffer = (BlocksPerChunk-WalkingWrapDistance)/2;
	private final int vbo;
	private DynmapChunk chunk;
	private final ShaderProgram shader;
	private final WorldNoiseMachine machine;
	private final SinglePlayerGame singlePlayerGame;
	public Dynmap(WorldNoiseMachine machine, SinglePlayerGame singlePlayerGame){
		this.machine = machine;
		this.singlePlayerGame = singlePlayerGame;
		vbo = GL15.glGenBuffers();
		loadVbo();
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "Dynmap.vert"), null, new File(
				WraithavensConquest.assetFolder, "Dynmap.frag"));
		shader.loadUniforms("texture", "shift", "size", "sunDirection", "colors");
		{
			shader.bind();
			shader.setUniform1I(0, 0);
			shader.setUniform2f(1, 0, 0);
			shader.setUniform2f(2, BlocksPerChunk, BlocksPerChunk);
			shader.setUniform1I(4, 1);
			Vector3f sunDirection = new Vector3f(1, 2, 0.5f);
			double mag = Math.sqrt(sunDirection.lengthSquared());
			shader.setUniform3f(3, (float)(sunDirection.x/mag), (float)(sunDirection.y/mag),
				(float)(sunDirection.z/mag));
		}
	}
	public void dispose(){
		GL15.glDeleteBuffers(vbo);
		if(chunk!=null)
			chunk.dispose();
		shader.dispose();
	}
	public int getX(){
		if(chunk==null)
			return Integer.MAX_VALUE;
		return chunk.getX();
	}
	public int getZ(){
		if(chunk==null)
			return Integer.MAX_VALUE;
		return chunk.getZ();
	}
	public void render(){
		MatrixUtils.setupPerspective(70, WraithavensConquest.INSTANCE.getScreenWidth()
			/(float)WraithavensConquest.INSTANCE.getScreenHeight(), 0.5f, 15000);
		if(chunk==null)
			return;
		shader.bind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 8, 0);
		chunk.render();
	}
	public void update(float x, float z){
		int boardX = Algorithms.groupLocation((int)x, WalkingWrapDistance)-WalkingViewBuffer;
		int boardZ = Algorithms.groupLocation((int)z, WalkingWrapDistance)-WalkingViewBuffer;
		if(chunk==null||chunk.getX()!=boardX||chunk.getZ()!=boardZ){
			if(chunk==null)
				chunk = new DynmapChunk(machine, boardX, boardZ, singlePlayerGame);
			else
				chunk.reloadTexture(machine, boardX, boardZ);
			shader.bind();
			shader.setUniform2f(1, boardX, boardZ);
		}
		chunk.update(x, z);
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
