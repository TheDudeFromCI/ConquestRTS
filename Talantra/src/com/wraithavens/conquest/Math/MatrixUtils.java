package com.wraithavens.conquest.Math;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class MatrixUtils{
	public static void setupImageOrtho(float scaleX, float scaleY, float near, float far){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, scaleX, 0, scaleY, near, far);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	public static void setupPerspective(float fov, float aspect, float near, float far){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float sine, cotangent, deltaZ;
		float radians = fov/2*(float)Math.PI/180;
		deltaZ = far-near;
		sine = (float)Math.sin(radians);
		if(deltaZ==0||sine==0||aspect==0)
			return;
		cotangent = (float)Math.cos(radians)/sine;
		MatrixUtils.makeIdentity(MatrixUtils.matrix);
		MatrixUtils.matrix.put(0, cotangent/aspect);
		MatrixUtils.matrix.put(5, cotangent);
		MatrixUtils.matrix.put(10, -(far+near)/deltaZ);
		MatrixUtils.matrix.put(11, -1);
		MatrixUtils.matrix.put(14, -2*near*far/deltaZ);
		MatrixUtils.matrix.put(15, 0);
		GL11.glMultMatrixf(MatrixUtils.matrix);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	private static void makeIdentity(FloatBuffer m){
		int oldPos = m.position();
		m.put(MatrixUtils.IDENTITY_MATRIX);
		m.position(oldPos);
	}
	private static final float[] IDENTITY_MATRIX = {
		1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1
	};
	private static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
}
