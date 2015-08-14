package com.wraithavens.conquest.SinglePlayer.Entities;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class EntityDatabase{
	static int SingularShaderAttrib;
	private final ArrayList<Entity> entities = new ArrayList();
	private final Comparator entitySorter = new Comparator<Entity>(){
		public int compare(Entity a, Entity b){
			return a.mesh==b.mesh?0:a.mesh.getId()>b.mesh.getId()?1:-1;
		}
	};
	private final ShaderProgram shader;
	private LandscapeWorld landscape;
	public EntityDatabase(){
		GlError.out("Creating entity database.");
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "ModelShader.vert"), null, new File(
				WraithavensConquest.assetFolder, "ModelShader.frag"));
		shader.bind();
		SingularShaderAttrib = shader.getAttributeLocation("shade");
		GL20.glEnableVertexAttribArray(SingularShaderAttrib);
		GlError.dumpError();
	}
	public void addEntity(Entity e){
		entities.add(e);
		// ---
		// Let's sort them so that entities of the same type render together.
		// This allows us to render batches, without have to rebind the same
		// VBOs multiple times per frame.
		// ---
		entities.sort(entitySorter);
		GlError.dumpError();
	}
	public void clear(){
		GlError.out("Clearing entity database.");
		for(Entity e : entities)
			e.dispose();
		entities.clear();
		GlError.dumpError();
	}
	public void dispose(){
		clear();
		shader.dispose();
	}
	public void removeEntity(Entity e){
		entities.remove(e);
	}
	public void render(Camera camera){
		// ---
		// Render all entities. Switching mesh types as nessicary.
		// ---
		EntityMesh mesh = null;
		shader.bind();
		for(Entity e : entities){
			if(!e.canRender(landscape, camera))
				continue;
			if(mesh==null||e.getMesh()!=mesh){
				mesh = e.getMesh();
				mesh.bind();
			}
			e.render();
		}
		GlError.dumpError();
	}
	public void setLandscape(LandscapeWorld landscape){
		this.landscape = landscape;
	}
}
