package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import java.util.ArrayList;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityDatabase;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.Grasslands;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.Algorithms;

public class LandscapeWorld{
	static int ShadeAttribLocation;
	private static final int ViewDistance = 1;
	private final ArrayList<LandscapeChunk> chunks = new ArrayList();
	private final WorldNoiseMachine machine;
	private final ShaderProgram shader;
	private final SpiralGridAlgorithm spiral;
	private final ChunkHeightData chunkHeights;
	private final Camera camera;
	private final EntityDatabase entityDatabase;
	private final Grasslands grassLands;
	private int chunkX;
	private int chunkZ;
	private int frame = 0;
	public LandscapeWorld(
		WorldNoiseMachine machine, EntityDatabase entityDatabase, Grasslands grassLands, Camera camera){
		GlError.out("Building landscape.");
		this.machine = machine;
		this.camera = camera;
		this.entityDatabase = entityDatabase;
		this.grassLands = grassLands;
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "Landscape.vert"), null, new File(
				WraithavensConquest.assetFolder, "Landscape.frag"));
		shader.bind();
		ShadeAttribLocation = shader.getAttributeLocation("shade");
		GL20.glEnableVertexAttribArray(ShadeAttribLocation);
		GlError.dumpError();
		spiral = new SpiralGridAlgorithm();
		spiral.setMaxDistance(ViewDistance+2);
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
		LandscapeChunk c = new LandscapeChunk(machine, entityDatabase, grassLands, x, y, z);
		chunks.add(c);
		return c;
	}
	public void render(){
		shader.bind();
		for(LandscapeChunk c : chunks)
			if(isWithinView(c, ViewDistance)
				&&camera.getFrustum().cubeInFrustum(c.getX(), c.getY(), c.getZ(), LandscapeChunk.LandscapeSize))
				c.render();
		GlError.dumpError();
	}
	public void update(){
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
		// then 1 chunk, every ten frames. I also want to unload chunks, but
		// every tens frame. I do this by flipping off tasks, and doing one or
		// the other every 5 frames. When processing chunks not in immediate
		// view distance, slow chunk loading to once every 40 frames.
		// ---
		frame++;
		if(frame%5==0){
			if(frame%10==0){
				if(!spiral.hasNext())
					return;
				System.out.println("Distance = "+spiral.getDistance());
				if(spiral.getDistance()>=ViewDistance&&frame%40!=0)
					return;
				spiral.next();
				loadChunks(spiral.getX()*LandscapeChunk.LandscapeSize+chunkX, spiral.getY()
					*LandscapeChunk.LandscapeSize+chunkZ);
			}else
				clearDistanceChunks();
		}
	}
	private void clearDistanceChunks(){
		for(int i = 0; i<chunks.size();){
			if(shouldRemove(chunks.get(i))){
				chunks.get(i).dispose();
				chunks.remove(i);
			}else
				i++;
		}
	}
	private boolean isWithinView(LandscapeChunk c, int distance){
		int x = Algorithms.groupLocation((int)camera.x, LandscapeChunk.LandscapeSize);
		int z = Algorithms.groupLocation((int)camera.z, LandscapeChunk.LandscapeSize);
		return Math.abs(x-c.getX())<=distance*LandscapeChunk.LandscapeSize
			&&Math.abs(z-c.getZ())<=distance*LandscapeChunk.LandscapeSize;
	}
	private void loadChunks(int x, int z){
		int[] h = new int[2];
		chunkHeights.getChunkHeight(x, z, h);
		for(int i = 0; i<h[1]; i++)
			getContainingChunk(x, i*LandscapeChunk.LandscapeSize+h[0], z, true);
	}
	private boolean shouldRemove(LandscapeChunk chunk){
		return !isWithinView(chunk, ViewDistance+3);
	}
}
