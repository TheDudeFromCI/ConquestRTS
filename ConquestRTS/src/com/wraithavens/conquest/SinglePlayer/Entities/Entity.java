package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public abstract class Entity{
	protected final EntityMesh mesh;
	public Entity(EntityType type){
		mesh = type.createReference();
	}
	public abstract boolean canRender(LandscapeWorld landscape, Camera camera);
	public void dispose(){
		mesh.removeReference();
	}
	public abstract int getLod();
	public EntityMesh getMesh(){
		return mesh;
	}
	public abstract float getScale();
	public EntityType getType(){
		return mesh.getType();
	}
	public abstract float getX();
	public abstract float getY();
	public abstract float getYaw();
	public abstract float getZ();
	public boolean isColorBlended(){
		return mesh.getType().colorBlended;
	}
	public abstract void render();
	boolean sways(){
		return mesh.getType().sways;
	}
}
