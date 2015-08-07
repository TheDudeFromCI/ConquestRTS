package com.wraithavens.conquest.SinglePlayer.Entities;

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
	public abstract void render();
}
