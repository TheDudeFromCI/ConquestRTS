package com.wraithavens.conquest.SinglePlayer.Entities;

import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;

public class StaticEntity extends Entity{
	private final Vector3f position = new Vector3f();
	private float scale = 1/5f;
	private AABB aabb;
	private int lod;
	public StaticEntity(EntityType type){
		super(type);
		aabb = new AABB();
		moveTo(0, 0, 0);
	}
	@Override
	public boolean canRender(LandscapeWorld landscape, Camera camera){
		if(landscape!=null&&!landscape.isWithinView((int)position.x, (int)position.z))
			return false;
		lod = mesh.getType().lodRadius.getLod(camera, position);
		if(lod==-1)
			return false;
		if(!aabb.visible(camera))
			return false;
		return true;
	}
	public void moveTo(float x, float y, float z){
		position.set(x, y, z);
		aabb.calculate(mesh.getAabbMin(), mesh.getAabbMax(), scale, position);
	}
	@Override
	public void render(){
		aabb.draw();
		GL11.glPushMatrix();
		GL11.glTranslatef(position.x, position.y, position.z);
		GL11.glScalef(scale, scale, scale);
		mesh.drawStatic(lod);
		GL11.glPopMatrix();
		GlError.dumpError();
	}
	public void scaleTo(float scale){
		this.scale = scale;
		aabb.calculate(mesh.getAabbMin(), mesh.getAabbMax(), scale, position);
	}
}
