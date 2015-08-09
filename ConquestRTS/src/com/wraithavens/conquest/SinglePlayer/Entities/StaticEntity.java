package com.wraithavens.conquest.SinglePlayer.Entities;

import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;

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
		aabb.calculate(x, y, z, mesh.getSize()*20*scale);
	}
	@Override
	public void render(Camera camera){
		if(!aabb.visible(camera))
			return;
		int lod = getLod(camera);
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
		aabb.calculate(position.x, position.y, position.z, mesh.getSize()*20*scale);
	}
	private int getLod(Camera camera){
		double d =
			Math.pow(camera.x-position.x, 2)+Math.pow(camera.y-position.y, 2)+Math.pow(camera.z-position.z, 2);
		if(d<100*100)
			return 0;
		if(d<150*150)
			return 1;
		if(d<300*300)
			return 2;
		if(d<450*450)
			return 3;
		if(d<600*600)
			return 4;
		if(d<1000*1000)
			return 5;
		return -1;
	}
}
