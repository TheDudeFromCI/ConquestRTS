package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

import java.io.File;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Texture;

public class WorldRenderer{
	public static int SHADER_LOCATION;
	public static int SHADER_LOCATION_2;
	public static int SHADER_LOCATION_3;
	private final World world;
	private final Camera camera;
	private ShaderProgram shader;
	private final ArrayList<QuadBatch> batches = new ArrayList();
	private Texture texture;
	public Texture shadowTexture;
	public WorldRenderer(World world, Camera camera){
		this.world = world;
		this.camera = camera;
	}
	public void initalize(){
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "Basic Shader.vert"), null, new File(
				WraithavensConquest.assetFolder, "Basic Shader.frag"));
		SHADER_LOCATION = shader.getAttributeLocation("shade");
		SHADER_LOCATION_2 = shader.getAttributeLocation("isGrass");
		SHADER_LOCATION_3 = shader.getAttributeLocation("shadowShift");
		GL20.glEnableVertexAttribArray(SHADER_LOCATION);
		GL20.glEnableVertexAttribArray(SHADER_LOCATION_2);
		GL20.glEnableVertexAttribArray(SHADER_LOCATION_3);
		texture = Texture.getTexture(WraithavensConquest.assetFolder, "Texture.png");
		texture.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL14.GL_MIRRORED_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL14.GL_MIRRORED_REPEAT);
		shader.loadUniforms("grassShade", "shadows");
		shader.bind();
		shader.setUniform1I(0, 0);
		shader.setUniform1I(1, 1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		texture.bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		shadowTexture.bind();
	}
	public void render(){
		shader.bind();
		for(int i = 0; i<batches.size(); i++)
			batches.get(i).renderPart();
	}
	public void update(){
		if(world.needsRebuffer()){
			batches.clear();
			for(int i = 0; i<world.getWorldStorage().voxels.size(); i++)
				processVoxel(world.getWorldStorage().voxels.get(i));
			world.setRebuffered();
		}
	}
	private boolean isVoxelVisible(Voxel voxel){
		if(voxel.getState() instanceof Chunk&&!((Chunk)voxel.getState()).containsBlocks())
			return false;
		return camera.frustum.cubeInFrustum(voxel.x<<Chunk.CHUNK_BITS, voxel.y<<Chunk.CHUNK_BITS,
			voxel.z<<Chunk.CHUNK_BITS, voxel.size<<Chunk.CHUNK_BITS);
	}
	private void processVoxel(Voxel voxel){
		if(!isVoxelVisible(voxel))
			return;
		if(voxel.isSolid()){
			if(voxel.getState()!=Voxel.DEFAULT){
				Chunk c = (Chunk)voxel.getState();
				batches.addAll(c.getBatches());
			}
			return;
		}
		for(int i = 0; i<8; i++)
			processVoxel(voxel.getVoxel(i));
	}
}
