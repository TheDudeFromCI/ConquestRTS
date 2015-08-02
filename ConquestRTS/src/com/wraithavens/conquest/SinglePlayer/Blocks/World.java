package com.wraithavens.conquest.SinglePlayer.Blocks;

import java.io.File;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.Algorithms;

public class World{
	private static final int ViewDistance = 20*16;
	public static int SHADER_LOCATION;
	public static int SHADER_LOCATION_2;
	private final Camera camera;
	private final ChunkGenerator generator;
	private final ChunkLoader chunkLoader;
	private final ArrayList<ChunkPainter> painters = new ArrayList();
	private final ArrayList<ChunkVBO> vbos = new ArrayList();
	private final ShaderProgram shader;
	private int step;
	private final int ibo;
	public World(WorldNoiseMachine machine, Camera camera){
		ibo = GL15.glGenBuffers();
		generateIndexBuffer();
		this.camera = camera;
		generator = new ChunkGenerator(machine);
		chunkLoader = new ChunkLoader(generator, ViewDistance);
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "Basic Shader.vert"), null, new File(
				WraithavensConquest.assetFolder, "Basic Shader.frag"));
		SHADER_LOCATION = shader.getAttributeLocation("shade");
		SHADER_LOCATION_2 = shader.getAttributeLocation("isGrass");
		GL20.glEnableVertexAttribArray(SHADER_LOCATION);
		GL20.glEnableVertexAttribArray(SHADER_LOCATION_2);
		shader.loadUniforms("grassShade");
		shader.bind();
		shader.setUniform1I(0, 0);
		chunkLoader.updateLocation(Algorithms.groupLocation((int)camera.x, 16),
			Algorithms.groupLocation((int)camera.y, 16), Algorithms.groupLocation((int)camera.z, 16));
	}
	public void dispose(){
		for(int i = 0; i<vbos.size(); i++)
			vbos.get(i).dispose();
		vbos.clear();
		painters.clear();
	}
	public ChunkVBO generateVBO(){
		for(int i = 0; i<vbos.size(); i++)
			if(vbos.get(i).isOpen)
				return vbos.get(i);
		int vbo = GL15.glGenBuffers();
		ChunkVBO v = new ChunkVBO(vbo, 0);
		vbos.add(v);
		return v;
	}
	public int getHeightAt(int x, int z){
		return generator.getHeightAt(x, z);
	}
	public void render(){
		shader.bind();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		for(ChunkPainter painter : painters)
			painter.render();
	}
	public void update(){
		int x = Algorithms.groupLocation((int)camera.x, 16);
		int y = Algorithms.groupLocation((int)camera.y, 16);
		int z = Algorithms.groupLocation((int)camera.z, 16);
		if(chunkLoader.getX()!=x||chunkLoader.getY()!=y||chunkLoader.getZ()!=z)
			chunkLoader.updateLocation(x, y, z);
		if(step%1==0){
			clearEmpties();
			RawChunk raw = chunkLoader.loadNextChunk(painters);
			if(raw!=null)
				painters.add(new ChunkPainter(this, raw));
		}
		step++;
	}
	private void clearEmpties(){
		for(int i = 0; i<painters.size();)
			if(shouldUnload(painters.get(i))){
				painters.get(i).dispose();
				painters.remove(i);
			}else
				i++;
	}
	private void generateIndexBuffer(){
		int maxQuads = 16*16*16/2*6;
		ShortBuffer indexData = BufferUtils.createShortBuffer(maxQuads*6);
		short e = 0;
		for(int i = 0; i<maxQuads; i++){
			indexData.put(e).put((short)(e+1)).put((short)(e+2));
			indexData.put(e).put((short)(e+2)).put((short)(e+3));
			e += 4;
		}
		indexData.flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
	}
	private boolean shouldUnload(ChunkPainter painter){
		int x = Algorithms.groupLocation((int)camera.x, 16);
		int y = Algorithms.groupLocation((int)camera.y, 16);
		int z = Algorithms.groupLocation((int)camera.z, 16);
		return Math.abs(x-painter.x)>ViewDistance||Math.abs(y-painter.y)>ViewDistance
			||Math.abs(z-painter.z)>ViewDistance;
	}
}
