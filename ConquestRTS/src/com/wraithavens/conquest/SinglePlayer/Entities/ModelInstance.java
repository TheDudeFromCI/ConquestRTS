package com.wraithavens.conquest.SinglePlayer.Entities;

import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Math.Vector3f;

public class ModelInstance{
	public final Model model;
	public final Vector3f position = new Vector3f();
	public final Vector3f rotation = new Vector3f(-90, 0, 0);
	public final Vector3f scale = new Vector3f(1, 1, 1);
	public ModelInstance(Model model){
		this.model = model;
		model.addReference();
	}
	void render(){
		if(isDefaultMatrix())model.render();
		else{
			GL11.glPushMatrix();
			if(!isDefaultPosition())GL11.glTranslatef(position.x, position.y, position.z);
			if(rotation.z!=0)GL11.glRotatef(rotation.z, 0, 0, 1);
			if(rotation.y!=0)GL11.glRotatef(rotation.y, 0, 1, 0);
			if(rotation.x!=0)GL11.glRotatef(rotation.x, 1, 0, 0);
			if(!isDefaultScale())GL11.glScalef(scale.x, scale.y, scale.z);
			model.render();
			GL11.glPopMatrix();
		}
	}
	private boolean isDefaultMatrix(){
		return isDefaultPosition()
				&&isDefaultRotation()
				&&isDefaultScale();
	}
	private boolean isDefaultPosition(){
		return position.x==0
				&&position.y==0
				&&position.z==0;
	}
	private boolean isDefaultRotation(){
		return rotation.x==0
				&&rotation.y==0
				&&rotation.z==0;
	}
	private boolean isDefaultScale(){
		return scale.x==1
				&&scale.y==1
				&&scale.z==1;
	}
	public void dispose(){
		model.removeReference();
	}
}