package com.wraithavens.conquest.SinglePlayer.Entities;

import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;

public class StaticEntity extends Entity{
	private final Vector3f position = new Vector3f();
	private float scale = 1/5f;
	private AABB aabb;
	public StaticEntity(EntityType type){
		super(type);
		aabb = new AABB();
		moveTo(0, 0, 0);
	}
	public void moveTo(float x, float y, float z){
		position.set(x, y, z);
		aabb.calculate(mesh.getAabbMin(), mesh.getAabbMax(), scale, position);
	}
	@Override
	public void render(Camera camera){
		aabb.draw();
		if(!aabb.visible(camera))
			return;
		int lod = mesh.getType().lodRadius.getLod(camera, position);
		if(lod==-1)
			return;
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
