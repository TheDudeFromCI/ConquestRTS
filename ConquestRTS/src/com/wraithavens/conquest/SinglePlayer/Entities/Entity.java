package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class Entity{
	final EntityMesh mesh;
	protected final Vector3f position = new Vector3f();
	protected float scale = 1/5f;
	protected float yaw;
	private final AABB aabb;
	public Entity(EntityType type){
		mesh = type.createReference();
		aabb = new AABB();
		updateAABB();
	}
	public boolean canRender(LandscapeWorld landscape, Camera camera){
		return mesh.getType().lodRadius.canSee(camera, position)
			&&landscape.isWithinView((int)position.x, (int)position.z)&&aabb.visible(camera);
	}
	public final void dispose(){
		mesh.removeReference();
	}
	public float getScale(){
		return scale;
	}
	public float getX(){
		return position.x;
	}
	public float getY(){
		return position.y;
	}
	public float getYaw(){
		return yaw;
	}
	public float getZ(){
		return position.z;
	}
	public final boolean isColorBlended(){
		return mesh.getType().colorBlended;
	}
	public void moveTo(float x, float y, float z){
		position.set(x, y, z);
	}
	public void render(){
		mesh.drawStatic();
	}
	public void scaleTo(float scale){
		this.scale = scale;
	}
	public void setYaw(float yaw){
		this.yaw = yaw;
	}
	public void updateAABB(){
		aabb.calculate(mesh.getAabbMin(), mesh.getAabbMax(), scale, position);
	}
	final EntityMesh getMesh(){
		return mesh;
	}
	final boolean sways(){
		return mesh.getType().sways;
	}
	@SuppressWarnings("unused")
	void update(double time){}
}
