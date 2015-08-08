package com.wraithavens.conquest.SinglePlayer.Entities;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class EntityDatabase{
	static int ShaderLocation;
	private final ArrayList<Entity> entities = new ArrayList();
	private final Comparator entitySorter = new Comparator<Entity>(){
		public int compare(Entity a, Entity b){
			if(a.mesh==null&&b.mesh==null)
				return 0;
			if(a.mesh==null)
				return 1;
			if(b.mesh==null)
				return -1;
			return a.mesh==b.mesh?0:a.mesh.getId()>b.mesh.getId()?1:-1;
		}
	};
	private final ShaderProgram shader;
	public EntityDatabase(){
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "ModelShader.vert"), null, new File(
				WraithavensConquest.assetFolder, "ModelShader.frag"));
		shader.bind();
		ShaderLocation = shader.getAttributeLocation("shade");
		GL20.glEnableVertexAttribArray(ShaderLocation);
	}
	public void addEntity(Entity e){
		entities.add(e);
		// ---
		// Let's sort them so that entities of the same type render together.
		// This allows us to render batches, without have to rebind the same
		// VBOs multiple times per frame.
		// ---
		entities.sort(entitySorter);
	}
	public void clear(){
		for(Entity e : entities)
			e.dispose();
		entities.clear();
	}
	public void dispose(){
		clear();
		shader.dispose();
	}
	public void render(Camera camera){
		shader.bind();
		// ---
		// Render all entities. Switching mesh types as nessicary.
		// ---
		EntityMesh mesh = null;
		for(Entity e : entities){
			if(mesh==null||e.getMesh()!=mesh){
				mesh = e.getMesh();
				if(mesh!=null)
					mesh.bind();
			}
			e.render(camera);
		}
	}
}
