package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public abstract class Entity{
	protected final EntityMesh mesh;
	public Entity(EntityType type){
		mesh = type.createReference();
	}
	public void dispose(){
		mesh.removeReference();
	}
	public EntityMesh getMesh(){
		return mesh;
	}
	public abstract void render(Camera camera);
}
