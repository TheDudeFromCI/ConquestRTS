package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.util.ArrayList;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.Algorithms;

public class LandscapeWorld{
	static int ShadeAttribLocation;
	private final ArrayList<LandscapeChunk> chunks = new ArrayList();
	private final WorldNoiseMachine machine;
	private final ShaderProgram shader;
	public LandscapeWorld(WorldNoiseMachine machine){
		this.machine = machine;
		getContainingChunk(8192, 3000, 8192, true);
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "Landscape.vert"), null, new File(
				WraithavensConquest.assetFolder, "Landscape.frag"));
		shader.bind();
		ShadeAttribLocation = shader.getAttributeLocation("shade");
		GL20.glEnableVertexAttribArray(ShadeAttribLocation);
		GlError.dumpError();
	}
	public void dispose(){
		for(LandscapeChunk c : chunks)
			c.dispose();
		chunks.clear();
		GlError.dumpError();
	}
	public LandscapeChunk getContainingChunk(int x, int y, int z, boolean load){
		x = Algorithms.groupLocation(x, LandscapeChunk.LandscapeSize);
		y = Algorithms.groupLocation(y, LandscapeChunk.LandscapeSize);
		z = Algorithms.groupLocation(z, LandscapeChunk.LandscapeSize);
		for(LandscapeChunk c : chunks)
			if(c.getX()==x&&c.getY()==y&&c.getZ()==z)
				return c;
		if(!load)
			return null;
		LandscapeChunk c = new LandscapeChunk(machine, x, y, z);
		chunks.add(c);
		return c;
	}
	public void render(Camera camera){
		shader.bind();
		for(LandscapeChunk c : chunks)
			if(camera.getFrustum().cubeInFrustum(c.getX(), c.getY(), c.getZ(), LandscapeChunk.LandscapeSize))
				c.render();
		GlError.dumpError();
	}
	public void update(Camera camera){
		// TODO
	}
}
