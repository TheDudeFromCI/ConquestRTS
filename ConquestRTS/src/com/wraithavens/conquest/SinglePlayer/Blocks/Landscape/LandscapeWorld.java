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
	private final SpiralGridAlgorithm spiral;
	private final ChunkHeightData chunkHeights;
	private int chunkX;
	private int chunkZ;
	private int frame = 0;
	public LandscapeWorld(WorldNoiseMachine machine){
		GlError.out("Building landscape.");
		this.machine = machine;
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "Landscape.vert"), null, new File(
				WraithavensConquest.assetFolder, "Landscape.frag"));
		shader.bind();
		ShadeAttribLocation = shader.getAttributeLocation("shade");
		GL20.glEnableVertexAttribArray(ShadeAttribLocation);
		GlError.dumpError();
		spiral = new SpiralGridAlgorithm();
		spiral.setMaxDistance(3);
		chunkHeights = new ChunkHeightData(machine);
	}
	public void dispose(){
		GlError.out("Disposing landscape.");
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
		// ---
		// First, make sure we are loading from the camera's location.
		// ---
		int x = Algorithms.groupLocation((int)camera.x, LandscapeChunk.LandscapeSize);
		int z = Algorithms.groupLocation((int)camera.z, LandscapeChunk.LandscapeSize);
		if(x!=chunkX||z!=chunkZ){
			spiral.reset();
			chunkX = x;
			chunkZ = z;
		}
		// ---
		// Next, load a chunk. Because of their size, I don't want to load more
		// then 1 chunk, every ten frames.
		// ---
		frame++;
		if(frame%1==0){
			if(!spiral.hasNext())
				return;
			spiral.next();
			loadChunks(spiral.getX()*LandscapeChunk.LandscapeSize+chunkX, spiral.getY()
				*LandscapeChunk.LandscapeSize+chunkZ);
		}
	}
	private void loadChunks(int x, int z){
		int[] h = new int[2];
		chunkHeights.getChunkHeight(x, z, h);
		for(int i = 0; i<h[1]; i++)
			getContainingChunk(x, i*LandscapeChunk.LandscapeSize+h[0], z, true);
	}
}
