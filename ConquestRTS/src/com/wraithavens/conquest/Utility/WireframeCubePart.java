package com.wraithavens.conquest.Utility;

import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

class WireframeCubePart{
	final Vector3f position = new Vector3f();
	final Vector3f scale = new Vector3f();
	final Vector3f color = new Vector3f();
	void bind(ShaderProgram shader){
		shader.setUniform3f(0, color.x, color.y, color.z);
		GL11.glTranslatef(position.x, position.y, position.z);
		GL11.glScalef(scale.x, scale.y, scale.z);
	}
}
