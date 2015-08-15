package com.wraithavens.conquest.SinglePlayer.Entities.Grass;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Texture;

public class Grasslands{
	private final ArrayList<GrassPatch> patches = new ArrayList();
	private final Comparator grassSorter = new Comparator<GrassPatch>(){
		public int compare(GrassPatch a, GrassPatch b){
			return a.getTextureSize()==b.getTextureSize()?0:a.getTextureSize()<b.getTextureSize()?1:-1;
		}
	};
	private final int ibo;
	private final int vbo;
	private final ShaderProgram shader;
	private final Texture texture;
	private final Camera camera;
	private LandscapeWorld landscape;
	private int shaderLastSize = -1;
	public Grasslands(Camera camera){
		this.camera = camera;
		ibo = GL15.glGenBuffers();
		vbo = GL15.glGenBuffers();
		{
			// ---
			// Build the index buffer.
			// ---
			ByteBuffer indexData = BufferUtils.createByteBuffer(24);
			indexData.put((byte)8).put((byte)9).put((byte)10);
			indexData.put((byte)8).put((byte)10).put((byte)11);
			indexData.put((byte)0).put((byte)1).put((byte)2);
			indexData.put((byte)0).put((byte)2).put((byte)3);
			indexData.put((byte)4).put((byte)5).put((byte)6);
			indexData.put((byte)4).put((byte)6).put((byte)7);
			// -
			indexData.put((byte)12).put((byte)13).put((byte)14);
			indexData.put((byte)12).put((byte)14).put((byte)15);
			indexData.flip();
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
			// ---
			// Build the vertex buffer.
			// ---
			FloatBuffer vertexData = BufferUtils.createFloatBuffer(80);
			float h = (float)Math.sqrt(2);
			vertexData.put(-0.5f).put(0.0f).put(-0.5f).put(0.0f).put(1.0f);
			vertexData.put(0.5f).put(0.0f).put(0.5f).put(1.0f).put(1.0f);
			vertexData.put(0.5f).put(h).put(0.5f).put(1.0f).put(0.0f);
			vertexData.put(-0.5f).put(h).put(-0.5f).put(0.0f).put(0.0f);
			vertexData.put(0.5f).put(0.0f).put(-0.5f).put(0.0f).put(1.0f);
			vertexData.put(-0.5f).put(0.0f).put(0.5f).put(1.0f).put(1.0f);
			vertexData.put(-0.5f).put(h).put(0.5f).put(1.0f).put(0.0f);
			vertexData.put(0.5f).put(h).put(-0.5f).put(0.0f).put(0.0f);
			vertexData.put(-0.5f).put(h/2).put(-0.5f).put(0.0f).put(0.0f);
			vertexData.put(-0.5f).put(h/2).put(0.5f).put(0.0f).put(1.0f);
			vertexData.put(0.5f).put(h/2).put(0.5f).put(1.0f).put(1.0f);
			vertexData.put(0.5f).put(h/2).put(-0.5f).put(1.0f).put(0.0f);
			// -
			vertexData.put(0.0f).put(0.0f).put(0.0f).put(0.0f).put(0.0f);
			vertexData.put(0.0f).put(1.0f).put(0.0f).put(0.0f).put(1.0f);
			vertexData.put(1.0f).put(1.0f).put(0.0f).put(1.0f).put(1.0f);
			vertexData.put(1.0f).put(0.0f).put(0.0f).put(1.0f).put(0.0f);
			vertexData.flip();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
		}
		// ---
		// Load the shader.
		// ---
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "Grass.vert"), null, new File(
				WraithavensConquest.assetFolder, "Grass.frag"));
		shader.bind();
		shader.loadUniforms("transform", "texture", "textureSize", "textureSizeHigh", "textureShrink",
			"billboard");
		shader.setUniform1I(0, 0);
		shader.setUniform1I(1, 1);
		shader.setUniform1I(5, 0);
		GlError.dumpError();
		// ---
		// This part is just a test.
		// ---
		texture = new Texture(new File(WraithavensConquest.assetFolder, "Grass.png"), 1);
		GlError.dumpError();
	}
	public void addPatch(GrassPatch patch){
		patches.add(patch);
		patches.sort(grassSorter);
	}
	public void removePatch(GrassPatch patch){
		patches.remove(patch);
	}
	public void render(){
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 20, 0);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 12);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDisable(GL11.GL_CULL_FACE);
		shader.bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		texture.bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		for(GrassPatch grass : patches){
			if(landscape!=null&&!landscape.isWithinView((int)grass.getX(), (int)grass.getZ()))
				continue;
			if(!grass.isVisible(camera))
				continue;
			grass.bind();
			if(grass.getTextureSize()!=shaderLastSize){
				shaderLastSize = grass.getTextureSize();
				shader.setUniform1I(2, grass.getTextureSize());
				shader.setUniform1I(3, grass.getTextureSize()-1);
				shader.setUniform1f(4, 1.0f/grass.getTextureSize());
			}
			GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, 18, GL11.GL_UNSIGNED_BYTE, 0, grass.getCount());
		}
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GlError.dumpError();
	}
	public void setLandscape(LandscapeWorld landscape){
		this.landscape = landscape;
	}
}
