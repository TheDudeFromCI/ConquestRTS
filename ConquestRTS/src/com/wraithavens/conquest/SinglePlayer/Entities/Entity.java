package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class Entity{
	private static void updateMinMax(){
		float x1 = tempMin.x;
		float y1 = tempMin.y;
		float z1 = tempMin.z;
		float x2 = tempMax.x;
		float y2 = tempMax.y;
		float z2 = tempMax.z;
		tempMin.x = Math.min(x1, x2);
		tempMin.y = Math.min(y1, y2);
		tempMin.z = Math.min(z1, z2);
		tempMax.x = Math.max(x1, x2);
		tempMax.y = Math.max(y1, y2);
		tempMax.z = Math.max(z1, z2);
	}
	private static final Vector3f tempMin = new Vector3f();
	private static final Vector3f tempMax = new Vector3f();
	final EntityMesh mesh;
	protected final Vector3f position = new Vector3f();
	private float scale = 1/5f;
	private float yaw;
	private final AABB aabb;
	public Entity(EntityType type){
		mesh = type.createReference();
		aabb = new AABB();
		updateAABB();
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
		tempMin.set(mesh.getAabbMin());
		tempMax.set(mesh.getAabbMax());
		double sin = Math.sin(yaw);
		double cos = Math.cos(yaw);
		tempMin.rotateYaw(sin, cos);
		tempMax.rotateYaw(sin, cos);
		updateMinMax();
		aabb.calculate(tempMin, tempMax, scale, position);
	}
	boolean canRender(LandscapeWorld landscape, Camera camera){
		return (mesh.getType().viewDistance==0||camera.distanceSquared(position.x, position.y, position.z)<mesh
			.getType().viewDistance)
			&&landscape.isWithinView((int)position.x, (int)position.z)
			&&aabb.visible(camera);
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
