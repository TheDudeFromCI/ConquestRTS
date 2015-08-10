package com.wraithavens.conquest.SinglePlayer.Entities;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class EntityDatabase{
	static int SingularShaderAttrib;
	static int BatchShaderAttrib;
	private final ArrayList<Entity> entities = new ArrayList();
	private final Comparator entitySorter = new Comparator<Entity>(){
		public int compare(Entity a, Entity b){
			if(a instanceof EntityBatch&&b instanceof EntityBatch){
				return a.mesh==b.mesh?0:a.mesh.getId()>b.mesh.getId()?1:-1;
			}else if(a instanceof EntityBatch)
				return 1;
			else if(b instanceof EntityBatch)
				return -1;
			return a.mesh==b.mesh?0:a.mesh.getId()>b.mesh.getId()?1:-1;
		}
	};
	private final ShaderProgram shader;
	private final ShaderProgram batchShader;
	public EntityDatabase(){
		GlError.out("Creating entity database.");
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "ModelShader.vert"), null, new File(
				WraithavensConquest.assetFolder, "ModelShader.frag"));
		shader.bind();
		SingularShaderAttrib = shader.getAttributeLocation("shade");
		GL20.glEnableVertexAttribArray(SingularShaderAttrib);
		batchShader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "BatchModelShader.vert"), null,
				new File(WraithavensConquest.assetFolder, "ModelShader.frag"));
		batchShader.bind();
		BatchShaderAttrib = batchShader.getAttributeLocation("shade");
		batchShader.loadUniforms("transform");
		batchShader.setUniform1I(0, 0);
		GL20.glEnableVertexAttribArray(BatchShaderAttrib);
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
	public void render(Camera camera){
		// ---
		// Render all entities. Switching mesh types as nessicary.
		// ---
		EntityMesh mesh = null;
		int mode = 0;
		for(Entity e : entities){
			if(mode!=2&&e instanceof EntityBatch){
				mode = 2;
				batchShader.bind();
				mesh = null;
			}else if(mode!=1&&!(e instanceof EntityBatch)){
				mode = 1;
				shader.bind();
				mesh = null;
			}
			if(mesh==null||e.getMesh()!=mesh){
				mesh = e.getMesh();
				if(mesh!=null)
					mesh.bind(mode==1);
			}
			e.render(camera);
		}
		GlError.dumpError();
	}
}
