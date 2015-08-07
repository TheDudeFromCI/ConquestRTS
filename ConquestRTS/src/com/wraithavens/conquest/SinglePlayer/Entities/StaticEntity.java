package com.wraithavens.conquest.SinglePlayer.Entities;

import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class StaticEntity extends Entity{
	private final Vector3f position = new Vector3f();
	private float scale = 1f/20;
	private AABB aabb;
	public StaticEntity(EntityType type){
		super(type);
		aabb = new AABB();
		moveTo(0, 0, 0);
	}
	public void moveTo(float x, float y, float z){
		position.set(x, y, z);
		aabb.calculate(x, y, z, mesh.getSize());
	}
	@Override
	public void render(Camera camera){
		if(!aabb.visible(camera))
			return;
		GL11.glPushMatrix();
		GL11.glTranslatef(position.x, position.y, position.z);
		GL11.glScalef(scale, scale, scale);
		mesh.drawStatic();
		GL11.glPopMatrix();
	}
}
