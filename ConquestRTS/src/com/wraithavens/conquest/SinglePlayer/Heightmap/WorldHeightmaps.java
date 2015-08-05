package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.io.File;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.MatrixUtils;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.Algorithms;

public class WorldHeightmaps{
	private final HeightMap heightmap;
	private final ShaderProgram shader;
	private int indices;
	private int offset;
	public WorldHeightmaps(WorldNoiseMachine machine){
		// ---
		// Prepare the mesh.
		// ---
		heightmap = new HeightMap(machine);
		// ---
		// Prepare the shader. Dang these names are long. o_O
		// ---
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "Heightmap.vert"), null, new File(
				WraithavensConquest.assetFolder, "Heightmap.frag"));
		buildShader();
	}
	public void dispose(){
		shader.dispose();
		heightmap.dispose();
	}
	public void render(boolean skybox){
		MatrixUtils.setupPerspective(skybox?90:70, skybox?1:WraithavensConquest.INSTANCE.getScreenWidth()
			/(float)WraithavensConquest.INSTANCE.getScreenHeight(), 1, 4000000);
		// ---
		// Prepare the shader.
		// ---
		shader.bind();
		// ---
		// Finally, render the heightmap mesh. :)
		// ---
		if(skybox)
			heightmap.render(indices, offset);
		else
			heightmap.render();
	}
	public void setOffset(int indices, int offset){
		this.indices = indices;
		this.offset = offset;
	}
	public void update(float x, float z){
		if(isSafeView(x, z))
			return;
		heightmap.update(Algorithms.groupLocation((int)x, HeightMap.ViewDistance),
			Algorithms.groupLocation((int)z, HeightMap.ViewDistance));
	}
	// ---
	// TODO Make light direction change to match the sun.
	// ---
	private void buildShader(){
		shader.bind();
		shader.loadUniforms("sunDirection");
		Vector3f sunDirection = new Vector3f(1, 2, 0.5f);
		double mag = Math.sqrt(sunDirection.lengthSquared());
		shader.setUniform3f(0, (float)(sunDirection.x/mag), (float)(sunDirection.y/mag),
			(float)(sunDirection.z/mag));
	}
	private boolean isSafeView(float x, float z){
		return x>=heightmap.getX()&&x<heightmap.getX()+HeightMap.ViewDistance&&z>=heightmap.getZ()
			&&z<heightmap.getZ()+HeightMap.ViewDistance;
	}
}
