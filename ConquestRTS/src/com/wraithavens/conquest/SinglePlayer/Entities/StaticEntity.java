package com.wraithavens.conquest.SinglePlayer.Entities;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class StaticEntity extends Entity{
	private final Vector3f position = new Vector3f();
	private float scale = 1/5f;
	private float yaw;
	private AABB aabb;
	private int lod;
	public StaticEntity(EntityType type){
		super(type);
		aabb = new AABB();
		moveTo(0, 0, 0);
	}
	@Override
	public boolean canRender(LandscapeWorld landscape, Camera camera){
		if(!landscape.isWithinView((int)position.x, (int)position.z))
			return false;
		lod = mesh.getType().lodRadius.getLod(camera, position);
		if(lod==-1)
			return false;
		if(!aabb.visible(camera))
			return false;
		return true;
	}
	@Override
	public int getLod(){
		return lod;
	}
	@Override
	public float getScale(){
		return scale;
	}
	@Override
	public float getX(){
		return position.x;
	}
	@Override
	public float getY(){
		return position.y;
	}
	@Override
	public float getYaw(){
		return yaw;
	}
	@Override
	public float getZ(){
		return position.z;
	}
	public void moveTo(float x, float y, float z){
		position.set(x, y, z);
	}
	@Override
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
}
