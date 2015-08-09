package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.Utility.BinaryFile;

public class LandscapeChunk{
	static final int LandscapeSize = 256;
	private final int x;
	private final int y;
	private final int z;
	private final int vbo;
	private final int ibo;
	LandscapeChunk(WorldNoiseMachine machine, int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
		{
			// ---
			// Load this chunk, or generate if nessicary.
			// ---
			File file =
				new File(WraithavensConquest.currentGameFolder+File.separatorChar+"Chunks", x+","+y+","+z+".dat");
			if(file.exists()&&file.length()>0){
				BinaryFile bin = new BinaryFile(file);
				int vertexCount = bin.getInt()*9;
				int indexCount = bin.getInt();
				FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertexCount);
				IntBuffer indexData = BufferUtils.createIntBuffer(indexCount);
				int i;
				for(i = 0; i<vertexCount; i++)
					vertexData.put(bin.getFloat());
				for(i = 0; i<indexCount; i++)
					indexData.put(bin.getInt());
				vertexData.flip();
				indexData.flip();
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
				GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
			}else{
				// TODO
			}
		}
		GlError.dumpError();
	}
	void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		GlError.dumpError();
	}
	int getX(){
		return x;
	}
	int getY(){
		return y;
	}
	int getZ(){
		return z;
	}
	void render(){
		// ---
		// TODO
		// ---
		GlError.dumpError();
	}
}
