package com.wraithavens.conquest.SinglePlayer.Entities;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class EntityDatabase{
	static int SingularShaderAttrib;
	private final ArrayList<Entity> entities = new ArrayList();
	private final Comparator entitySorter = new Comparator<Entity>(){
		public int compare(Entity a, Entity b){
			if(a.mesh==b.mesh){
				if(a.getTempDistance()==b.getTempDistance())
					return 0;
				return a.getTempDistance()<b.getTempDistance()?-1:1;
			}
			return a.mesh.getId()>b.mesh.getId()?1:-1;
		}
	};
	private final ShaderProgram shader;
	private final Camera camera;
	private final Vector3f cameraLocation = new Vector3f();
	private LandscapeWorld landscape;
	public EntityDatabase(Camera camera){
		this.camera = camera;
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
		sort();
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
	public void render(){
		// ---
		// Render all entities. Switching mesh types as nessicary.
		// ---
		EntityMesh mesh = null;
		boolean shaderBound = false;
		for(Entity e : entities){
			if(!e.canRender(landscape, camera))
				continue;
			if(e.getLod()>0){
				// ---
				// TODO Make object render more... eh, father away-ish.
				// ---
				continue;
			}
			if(!shaderBound){
				shaderBound = true;
				shader.bind();
			}
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
	public void update(){
		checkForCameraMovement();
	}
	private void checkForCameraMovement(){
		if(Math.abs(camera.x-cameraLocation.x)>0.1f||Math.abs(camera.y-cameraLocation.y)>0.1f
			||Math.abs(camera.z-cameraLocation.z)>0.1f)
			sort();
	}
	private float d(Entity e){
		float x = e.getX()-camera.x;
		float y = e.getY()-camera.y;
		float z = e.getZ()-camera.z;
		return x*x+y*y+z*z;
	}
	private void sort(){
		for(Entity e : entities)
			e.storeTempDistance(d(e));
		entities.sort(entitySorter);
		cameraLocation.set(camera.x, camera.y, camera.z);
	}
}
