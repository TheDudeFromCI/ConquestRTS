package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public abstract class Entity{
	protected final EntityMesh mesh;
	public Entity(EntityType type){
		if(type==null)
			mesh = null;
		else
			mesh = type.createReference();
	}
	public void dispose(){
		if(mesh!=null)
			mesh.removeReference();
	}
	public EntityMesh getMesh(){
		return mesh;
	}
	public abstract void render(Camera camera);
}
