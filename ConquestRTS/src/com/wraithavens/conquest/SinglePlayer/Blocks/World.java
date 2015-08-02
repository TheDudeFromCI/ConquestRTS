package com.wraithavens.conquest.SinglePlayer.Blocks;

import java.io.File;
import java.util.ArrayList;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.Algorithms;

public class World{
	private static final int ViewDistance = 128;
	public static int SHADER_LOCATION;
	public static int SHADER_LOCATION_2;
	private final Camera camera;
	private final ChunkGenerator generator;
	private final ChunkLoader chunkLoader;
	private final ArrayList<ChunkPainter> painters = new ArrayList();
	private final ShaderProgram shader;
	private int step;
	public World(WorldNoiseMachine machine, Camera camera){
		this.camera = camera;
		generator = new ChunkGenerator(machine);
		chunkLoader = new ChunkLoader(generator);
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
		chunkLoader.setViewDistance(ViewDistance);
	}
	public int getHeightAt(int x, int z){
		return generator.getHeightAt(x, z);
	}
	public void render(){
		shader.bind();
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
			if(raw!=null){
				painters.add(new ChunkPainter(raw));
			}
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
	private boolean shouldUnload(ChunkPainter painter){
		int x = Algorithms.groupLocation((int)camera.x, 16);
		int y = Algorithms.groupLocation((int)camera.y, 16);
		int z = Algorithms.groupLocation((int)camera.z, 16);
		return Math.abs(x-painter.x)>ViewDistance||Math.abs(y-painter.y)>ViewDistance
			||Math.abs(z-painter.z)>ViewDistance;
	}
}
